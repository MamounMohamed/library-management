package Mamoun.librarymanagement.Services;

import Mamoun.librarymanagement.DTO.BookDTO;
import Mamoun.librarymanagement.Entities.Book;
import Mamoun.librarymanagement.Exceptions.NotFoundException;
import Mamoun.librarymanagement.Mappers.BookMapper;
import Mamoun.librarymanagement.Repositories.BookRepository;
import Mamoun.librarymanagement.Services.BookService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllBooks_WhenBooksExist_ReturnListOfBooks() {
        // Arrange
        List<Book> books = new ArrayList<>();
        books.add(Book.builder().id(1L).title("Book 1").author("Author 1").build());
        books.add(Book.builder().id(2L).title("Book 2").author("Author 2").build());

        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.toBookDTO(any())).thenReturn(BookDTO.builder().build());

        // Act
        List<BookDTO> result = bookService.getAllBooks();

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(2);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_WhenBookExists_ReturnBookDTO() {
        // Arrange
        Long id = 1L;
        Book book = Book.builder().id(id).title("Book 1").author("Author 1").build();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toBookDTO(book)).thenReturn(BookDTO.builder().build());

        // Act
        BookDTO result = bookService.getBookById(id);

        // Assert
        assertThat(result).isNotNull();
        verify(bookRepository, times(1)).findById(id);
    }

    @Test
    void getBookById_WhenBookDoesNotExist_ThrowNotFoundException() {
        // Arrange
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> bookService.getBookById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book not found with id: " + id);
    }

    @Test
    @Transactional
    void addBook_WithValidBookDTO_ReturnAddedBookDTO() {
        // Arrange
        BookDTO bookDTO = BookDTO.builder().title("Book 1").author("Author 1").build();
        Book book = Book.builder().id(1L).title("Book 1").author("Author 1").build();
        when(bookMapper.toBook(bookDTO)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        // Act
        BookDTO result = bookService.addBook(bookDTO);

        // Assert
        assertThat(result).isNotNull();
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @Transactional

    void updateBook_WithValidIdAndBookDTO_ReturnUpdatedBookDTO() {
        // Arrange
        Long id = 1L;
        BookDTO updatedBookDTO = BookDTO.builder().title("Book 2").author("Author 2").build();
        Book updatedBook = Book.builder().id(id).title("Book 2").author("Author 2").build();
        when(bookRepository.existsById(id)).thenReturn(true);
        when(bookMapper.toBook(updatedBookDTO)).thenReturn(updatedBook);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(bookMapper.toBookDTO(updatedBook)).thenReturn(updatedBookDTO);

        // Act
        BookDTO result = bookService.updateBook(id, updatedBookDTO);

        // Assert
        assertThat(result).isNotNull();
        verify(bookRepository, times(1)).save(updatedBook);
    }

    @Test
    @Transactional

    void updateBook_WithInvalidId_ThrowNotFoundException() {
        // Arrange
        Long id = 1L;
        BookDTO updatedBookDTO = BookDTO.builder().title("Book 2").author("Author 2").build();
        when(bookRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> bookService.updateBook(id, updatedBookDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book not found with id: " + id);
    }
    @Transactional
    @Test
    void deleteBook_WithValidId_DeleteBook() {
        // Arrange
        Long id = 1L;

        // Act
        bookService.deleteBook(id);

        // Assert
        verify(bookRepository, times(1)).deleteById(id);
    }
}
