package Mamoun.librarymanagement.Controllers;

import Mamoun.librarymanagement.DTO.BookDTO;
import Mamoun.librarymanagement.Exceptions.DeleteException;
import Mamoun.librarymanagement.Exceptions.NotFoundException;
import Mamoun.librarymanagement.Services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @MockBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private BookDTO sampleBookDTO;

    @BeforeEach
    void setUp() {
        // Sample BookDTO for testing
        sampleBookDTO = BookDTO.builder()
                .id(1L)
                .title("Sample Title")
                .author("Sample Author")
                .publicationYear(2021)
                .isbn("1234567890").
                isBorrowed(false)
                .build();
    }

    @Test
    void getBookById_BookFound_Successful() throws Exception {
        // Mock service response for a book found scenario
        long bookId = 1;
        when(bookService.getBookById(bookId)).thenReturn(sampleBookDTO);

        ResultActions response = mockMvc.perform(get("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleBookDTO)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(sampleBookDTO.getTitle())))
                .andExpect(jsonPath("$.author", is(sampleBookDTO.getAuthor())))
                .andExpect(jsonPath("$.publicationYear", is(sampleBookDTO.getPublicationYear())))
                .andExpect(jsonPath("$.isbn", is(sampleBookDTO.getIsbn())))
                .andExpect(jsonPath("$.borrowed", is(sampleBookDTO.isBorrowed())));
    }


    @Test
    void getAllBooks_Successful() throws Exception {
        // Mock service response for getting all books
        List<BookDTO> bookList = new ArrayList<>();
        bookList.add(sampleBookDTO);
        bookList.add(sampleBookDTO);
        when(bookService.getAllBooks()).thenReturn(bookList);

        ResultActions response = mockMvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[1].id", is(sampleBookDTO.getId().intValue())))
                .andExpect(jsonPath("$[1].title", is(sampleBookDTO.getTitle())))
                .andExpect(jsonPath("$[1].author", is(sampleBookDTO.getAuthor())))
                .andExpect(jsonPath("$[1].publicationYear", is(sampleBookDTO.getPublicationYear())))
                .andExpect(jsonPath("$[1].isbn", is(sampleBookDTO.getIsbn())))
                .andExpect(jsonPath("$[1].borrowed", is(sampleBookDTO.isBorrowed())));
    }


    @Test
    void getBookById_BookNotFound() throws Exception {
        // Mock service response
        when(bookService.getBookById(1L)).thenThrow(new NotFoundException("Book not found"));
        // Perform GET request
        ResultActions response =  mockMvc.perform(get("/api/books/1").contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isNotFound());
    }
    @Test
    void addBook_Successful() throws Exception {
        // Mock service response for adding a book
        when(bookService.addBook(any(BookDTO.class))).thenReturn(sampleBookDTO);

        ResultActions response = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleBookDTO)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(sampleBookDTO.getTitle())))
                .andExpect(jsonPath("$.author", is(sampleBookDTO.getAuthor())))
                .andExpect(jsonPath("$.publicationYear", is(sampleBookDTO.getPublicationYear())))
                .andExpect(jsonPath("$.isbn", is(sampleBookDTO.getIsbn())))
                .andExpect(jsonPath("$.borrowed", is(sampleBookDTO.isBorrowed())));
    }
    @Test
    void addBook_InvalidRequestBody() throws Exception {
        // Mock service response for adding a book
        when(bookService.addBook(any(BookDTO.class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid request body"));

        ResultActions response = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""));

        response.andExpect(status().isBadRequest());
    }

    @Test
    void updateBook_BookFound_Successful() throws Exception {
        // Mock service response for updating a book
        long bookId = 1;
        when(bookService.updateBook(eq(bookId), any(BookDTO.class))).thenReturn(sampleBookDTO);

        ResultActions response = mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleBookDTO)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(sampleBookDTO.getId().intValue())))
                .andExpect(jsonPath("$.title", is(sampleBookDTO.getTitle())))
                .andExpect(jsonPath("$.author", is(sampleBookDTO.getAuthor())))
                .andExpect(jsonPath("$.publicationYear", is(sampleBookDTO.getPublicationYear())))
                .andExpect(jsonPath("$.isbn", is(sampleBookDTO.getIsbn())))
                .andExpect(jsonPath("$.borrowed", is(sampleBookDTO.isBorrowed())));
    }

    @Test
    void updateBook_BookNotFound() throws Exception {
        // Mock service response for updating a non-existing book
        long nonExistingBookId = 100;
        when(bookService.updateBook(eq(nonExistingBookId), any(BookDTO.class))).thenThrow(new NotFoundException("Book not found"));

        ResultActions response = mockMvc.perform(put("/api/books/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleBookDTO)));

        response.andExpect(status().isNotFound());
    }
    @Test
    void updateBook_InvalidRequestBody() throws Exception {
        // Mock service response for updating a book
        long bookId = 1;
        when(bookService.updateBook(eq(bookId), any(BookDTO.class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid request body"));

        ResultActions response = mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""));

        response.andExpect(status().isBadRequest());
    }

    @Test
    void deleteBook_BookDeletedSuccessfully() throws Exception {
        // Mock service response for deleting a book
        long bookId = 1;
        doNothing().when(bookService).deleteBook(eq(bookId));

        ResultActions response = mockMvc.perform(delete("/api/books/1"));

        response.andExpect(status().isNoContent());
    }
    @Test
    void deleteBook_BookDeletedFail() throws Exception {
        // Mock service response for deleting a book
        long bookId = 1;
        doThrow(new DeleteException("Book can't be deleted")).when(bookService).deleteBook(bookId);
        ResultActions response = mockMvc.perform(delete("/api/books/1"));
        response.andExpect(status().isConflict());
    }



}






