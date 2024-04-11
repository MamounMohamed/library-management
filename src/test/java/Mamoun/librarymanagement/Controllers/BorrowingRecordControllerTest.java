package Mamoun.librarymanagement.Controllers;

import Mamoun.librarymanagement.DTO.BorrowingRecordDTO;
import Mamoun.librarymanagement.Exceptions.BookNotAvailableException;
import Mamoun.librarymanagement.Exceptions.NotFoundException;
import Mamoun.librarymanagement.Services.BorrowingRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BorrowingRecordController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BorrowingRecordControllerTest {

    @MockBean
    private BorrowingRecordService borrowingRecordService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private BorrowingRecordDTO borrowingRecordDTO;
    @BeforeEach
    private void init(){
        borrowingRecordDTO = BorrowingRecordDTO.builder()
                .id(1L)
                .bookId(1L)
                .patronId(1L)
                .borrowingDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(7L))
                .build();

    }

    @Test
    void borrowBook_Successful() throws Exception {
        // Mock service response for borrowing a book successfully
        when(borrowingRecordService.borrowBook(1L,1L)).thenReturn(borrowingRecordDTO);
        ResultActions response = mockMvc.perform(post("/api/borrow/{bookId}/patron/{patronId}",1,1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(borrowingRecordDTO)));

        response.andExpect(status().isCreated())

                .andExpect(jsonPath("$.id", is((int) borrowingRecordDTO.getId())))
                .andExpect(jsonPath("$.bookId", is((int) borrowingRecordDTO.getBookId())))
                .andExpect(jsonPath("$.patronId", is((int) borrowingRecordDTO.getPatronId())))
                .andExpect(jsonPath("$.borrowingDate", is(borrowingRecordDTO.getBorrowingDate().toString())))
                .andExpect(jsonPath("$.returnDate", is(borrowingRecordDTO.getReturnDate().toString())));

    }

    @Test
    void borrowBook_BookNotAvailable() throws Exception {
        // Mock service response for borrowing a book that is not available
        when(borrowingRecordService.borrowBook(1L,1L)).thenThrow(new BookNotAvailableException("Book not available"));

        ResultActions response = mockMvc.perform(post("/api/borrow/1/patron/1"));

        response.andExpect(status().isConflict())
                .andExpect(content().string("Book not available"));
    }

    @Test
    void returnBook_Successful() throws Exception {
        // Mock service response for returning a book successfully
        when(borrowingRecordService.returnBook(1L,1L)).thenReturn(borrowingRecordDTO);

        ResultActions response = mockMvc.perform(put("/api/return/1/patron/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(borrowingRecordDTO)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) borrowingRecordDTO.getId())))
                .andExpect(jsonPath("$.bookId", is((int) borrowingRecordDTO.getBookId())))
                .andExpect(jsonPath("$.patronId", is((int) borrowingRecordDTO.getPatronId())))
                .andExpect(jsonPath("$.borrowingDate", is(borrowingRecordDTO.getBorrowingDate().toString())))
                .andExpect(jsonPath("$.returnDate", is(borrowingRecordDTO.getReturnDate().toString())));
    }

    @Test
    void returnBook_NotFoundException() throws Exception {
        // Mock service response for returning a book that is not found
        when(borrowingRecordService.returnBook(1L,1L)).thenThrow(new NotFoundException("Book not found"));

        ResultActions response = mockMvc.perform(put("/api/return/1/patron/1"));

        response.andExpect(status().isNotFound())
                .andExpect(content().string("Book not found"));
    }

    @Test
    void returnBook_BookNotAvailable() throws Exception {
        // Mock service response for returning a book that is not available
        when(borrowingRecordService.returnBook(1L, 1L)).thenThrow(new BookNotAvailableException("Book not available"));

        ResultActions response = mockMvc.perform(put("/api/return/1/patron/1"));

        response.andExpect(status().isConflict())
                .andExpect(content().string("Book not available"));
    }
}
