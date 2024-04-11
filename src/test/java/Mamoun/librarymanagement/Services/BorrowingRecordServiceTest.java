package Mamoun.librarymanagement.Services;
import Mamoun.librarymanagement.DTO.BorrowingRecordDTO;
import Mamoun.librarymanagement.Entities.Book;
import Mamoun.librarymanagement.Entities.BorrowingRecord;
import Mamoun.librarymanagement.Entities.Patron;
import Mamoun.librarymanagement.Exceptions.BookNotAvailableException;
import Mamoun.librarymanagement.Exceptions.NotFoundException;
import Mamoun.librarymanagement.Mappers.BorrowingRecordMapper;
import Mamoun.librarymanagement.Repositories.BookRepository;
import Mamoun.librarymanagement.Repositories.BorrowingRecordRepository;
import Mamoun.librarymanagement.Repositories.PatronRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class BorrowingRecordServiceTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BorrowingRecordMapper borrowingRecordMapper;

    @InjectMocks
    private BorrowingRecordService borrowingRecordService;

    @Test
    void borrowBook_Successful() {
        long bookId = 1;
        long patronId = 1;
        long borrowingRecordId = 1;
        Book book = Book.builder().id(bookId).title("title1").author("author1").isbn("12345").publicationYear(2024).build();
        Patron patron = Patron.builder().id(patronId).name("name1").contactInformation("123456789").build();
        BorrowingRecord borrowingRecord = new BorrowingRecord(book, patron, LocalDate.now(), LocalDate.now().plusDays(10));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));
        borrowingRecord.setId(borrowingRecordId);
        when(borrowingRecordRepository.findById(borrowingRecordId)).thenReturn(Optional.of(borrowingRecord));
        BorrowingRecordDTO borrowingRecordDTO = borrowingRecordService.borrowBook(bookId, patronId);

        assertThat(book.isBorrowed()).isTrue();
        assertThat(borrowingRecordDTO).isEqualTo(borrowingRecordMapper.toBorrowingRecordDTo(borrowingRecord));
        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
    }

    @Test
    void borrowBook_BookNotFound() {
        long bookId = 1;
        long patronId = 1;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowingRecordService.borrowBook(bookId, patronId)).isInstanceOf(NotFoundException.class);
        verify(borrowingRecordRepository, never()).save(any());
    }

    @Test
    void borrowBook_PatronNotFound() {
        long bookId = 1;
        long patronId = 1;

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));
        when(patronRepository.findById(patronId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowingRecordService.borrowBook(bookId, patronId)).isInstanceOf(NotFoundException.class);
        verify(borrowingRecordRepository, never()).save(any());
    }

    @Test
    void borrowBook_BookAlreadyBorrowed() {
        long bookId = 1;
        long patronId = 1;
        Book book = Book.builder().id(bookId).title("title1").author("author1").isbn("12345").publicationYear(2024).build();
        Patron patron = Patron.builder().id(patronId).name("name1").contactInformation("123456789").build();
        book.setBorrowed(true);
        BorrowingRecord borrowingRecord = new BorrowingRecord(book, patron, LocalDate.now(), LocalDate.now().plusDays(10));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findFirstByBookIdAndPatronIdOrderByBorrowingDateDesc(bookId,patronId)).thenReturn(Optional.of((borrowingRecord)));

        assertThat(book.isBorrowed()).isEqualTo(true);
        assertThatThrownBy(() -> borrowingRecordService.borrowBook(bookId, patronId)).isInstanceOf(BookNotAvailableException.class);
        verify(borrowingRecordRepository, never()).save(any());
    }
    @Test
    void returnBook_Successful() {
        long bookId = 1;
        long patronId = 1;
        Book book = Book.builder().id(bookId).title("title1").author("author1").isbn("12345").publicationYear(2024).isBorrowed(true).build();
        Patron patron = Patron.builder().id(patronId).name("name1").contactInformation("123456789").build();
        BorrowingRecord borrowingRecord = new BorrowingRecord(book, patron, LocalDate.now(), LocalDate.now().plusDays(10));
        borrowingRecord.setId(5L);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findFirstByBookIdAndPatronIdOrderByBorrowingDateDesc(bookId, patronId)).thenReturn(Optional.of(borrowingRecord));
        BorrowingRecordDTO borrowingRecordDTO = borrowingRecordService.returnBook(bookId, patronId);

        assertThat(book.isBorrowed()).isFalse();
        assertThat(borrowingRecord.getReturnDate()).isNotNull();
        assertThat(borrowingRecordDTO).isEqualTo(borrowingRecordMapper.toBorrowingRecordDTo(borrowingRecord));
        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
    }

    @Test
    void returnBook_BookNotFound() {
        long bookId = 1;
        long patronId = 1;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowingRecordService.returnBook(bookId, patronId)).isInstanceOf(NotFoundException.class);
        verify(borrowingRecordRepository, never()).save(any());
    }

    @Test
    void returnBook_PatronNotFound() {
        long bookId = 1;
        long patronId = 1;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));
        when(patronRepository.findById(patronId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowingRecordService.returnBook(bookId, patronId)).isInstanceOf(NotFoundException.class);
        verify(borrowingRecordRepository, never()).save(any());
    }

    @Test
    void returnBook_NoActiveBorrowingRecordFound() {
        long bookId = 5;
        long patronId = 5;
        Book book = Book.builder().id(bookId).title("title1").author("author1").isbn("12345").publicationYear(2024).isBorrowed(true).build();
        Patron patron = Patron.builder().id(patronId).name("name1").contactInformation("123456789").build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findFirstByBookIdAndPatronIdOrderByBorrowingDateDesc(bookId, patronId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowingRecordService.returnBook(bookId, patronId)).isInstanceOf(NotFoundException.class).hasMessage("No active borrowing record found for the book and patron");
        verify(borrowingRecordRepository, never()).save(any());
    }
}
