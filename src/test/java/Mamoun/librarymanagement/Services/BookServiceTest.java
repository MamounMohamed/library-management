package Mamoun.librarymanagement.Services;

import Mamoun.librarymanagement.DTO.BookDTO;
import Mamoun.librarymanagement.Entities.Book;
import Mamoun.librarymanagement.Exceptions.DeleteException;
import Mamoun.librarymanagement.Exceptions.NotFoundException;
import Mamoun.librarymanagement.Mappers.BookMapper;
import Mamoun.librarymanagement.Repositories.BookRepository;
import Mamoun.librarymanagement.Repositories.BorrowingRecordRepository;
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
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;
    private BookDTO bookDTO , updatedBookDTO;

    private Book book , updatedBook;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        book = Book.builder().id(1L).title("Book 1").author("Author 1").publicationYear(2022).isbn("978-3-16-148410-0").isBorrowed(false).build();
        bookDTO = BookDTO.builder().title("Book 1").author("Author 1").publicationYear(2022).isbn("978-3-16-148410-0").isBorrowed(false).build();
        updatedBook =  Book.builder().id(2L).title("Book Updated").author("Author 1").publicationYear(2022).isbn("978-3-16-148410-0").isBorrowed(false).build();
        updatedBookDTO =BookDTO.builder().id(2L).title("Book Updated").author("Author 1").publicationYear(2022).isbn("978-3-16-148410-0").isBorrowed(false).build();
    }

    @Test
    void getAllBooks_WhenBooksExist_ReturnListOfBooks() {
        // Arrange
        List<Book> books = new ArrayList<>();
        books.add(book);

        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.toBookDTO(any())).thenReturn(bookDTO);

        // Act
        List<BookDTO> result = bookService.getAllBooks();

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        verify(bookRepository, times(1)).findAll();
    }


    @Test
    void getBookById_WhenBookExists_ReturnBookDTO() {
        // Arrange
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        // Act
        BookDTO result = bookService.getBookById(id);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(bookDTO.getTitle());
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
        when(borrowingRecordRepository.existsByBookId(1L)).thenReturn(false);
        bookService.deleteBook(id);
        // Assert
        verify(bookRepository, times(1)).deleteById(id);
    }

    @Transactional
    @Test
    void deleteBook_FailedDeletion_DeleteBook() {
        // Arrange
        Long id = 1L;
        when(borrowingRecordRepository.existsByBookId(1L)).thenReturn(true);

        // Assert
        assertThatThrownBy(() -> bookService.deleteBook(id))
                .isInstanceOf(DeleteException.class);
    }


}
