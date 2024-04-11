package Mamoun.librarymanagement.Controllers;

import Mamoun.librarymanagement.DTO.BookDTO;
import Mamoun.librarymanagement.Exceptions.NotFoundException;
import Mamoun.librarymanagement.Services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Collections;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Mock
    @InjectMocks
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Autowired
    private MockMvc mockMvc;

    private BookDTO sampleBookDTO;

    @BeforeEach
    void setUp() {
        // Sample BookDTO for testing
        sampleBookDTO = BookDTO.builder()
                .id(1L)
                .title("Sample Title")
                .author("Sample Author")
                .publicationYear(2021)
                .isbn("1234567890")
                .build();
    }

    @Test
    void getBookById_Successful() throws Exception {
        // Mock service response
        when(bookService.getBookById(1L)).thenReturn(sampleBookDTO);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(sampleBookDTO.getId()))
                .andExpect(jsonPath("$.title").value(sampleBookDTO.getTitle()))
                .andExpect(jsonPath("$.author").value(sampleBookDTO.getAuthor()))
                .andExpect(jsonPath("$.publicationYear").value(sampleBookDTO.getPublicationYear()))
                .andExpect(jsonPath("$.isbn").value(sampleBookDTO.getIsbn()));
    }

    @Test
    void addBook_Successful() throws Exception {
        // Mock service response
        when(bookService.addBook(any(BookDTO.class))).thenReturn(sampleBookDTO);

        // Perform POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Sample Title\", \"author\": \"Sample Author\", \"publicationYear\": 2021, \"isbn\": \"1234567890\", \"isBorrowed\": false }"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(sampleBookDTO.getId()))
                .andExpect(jsonPath("$.title").value(sampleBookDTO.getTitle()))
                .andExpect(jsonPath("$.author").value(sampleBookDTO.getAuthor()))
                .andExpect(jsonPath("$.publicationYear").value(sampleBookDTO.getPublicationYear()))
                .andExpect(jsonPath("$.isbn").value(sampleBookDTO.getIsbn()));
    }

    @Test
    void updateBook_Successful() throws Exception {
        // Mock service response
        when(bookService.updateBook(anyLong(), any(BookDTO.class))).thenReturn(sampleBookDTO);

        // Perform PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Updated Title\", \"author\": \"Sample Author\", \"publicationYear\": 2021, \"isbn\": \"1234567890\", \"isBorrowed\": false }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(sampleBookDTO.getId()))
                .andExpect(jsonPath("$.title").value(sampleBookDTO.getTitle()))
                .andExpect(jsonPath("$.author").value(sampleBookDTO.getAuthor()))
                .andExpect(jsonPath("$.publicationYear").value(sampleBookDTO.getPublicationYear()))
                .andExpect(jsonPath("$.isbn").value(sampleBookDTO.getIsbn()));
    }

    @Test
    void deleteBook_Successful() throws Exception {
        // Perform DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/{id}", 1))
                .andExpect(status().isNoContent());
    }

    // Add more test cases for other scenarios
}
