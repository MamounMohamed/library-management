package Mamoun.librarymanagement.Repositories;

import Mamoun.librarymanagement.Entities.Book;
import Mamoun.librarymanagement.Entities.BorrowingRecord;
import Mamoun.librarymanagement.Entities.Patron;
import Mamoun.librarymanagement.Repositories.BorrowingRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BorrowingRecordRepositoryTest {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;
    @Autowired

    private BookRepository bookRepository;
    @Autowired

    private PatronRepository patronRepository;

    @Test
    @Transactional
    public void whenAddBorrowingRecord_thenBorrowingRecordIsPersisted() {
        // Arrange
        Book book = Book.builder().title("Title 1").author("Author 1").isbn("M-123").publicationYear(2024).isBorrowed(false).build();
        Patron patron = Patron.builder().name("Patron 2").contactInformation("+234851456981").build();
        bookRepository.save(book);
        patronRepository.save(patron);

        LocalDate borrowingDate = LocalDate.now();
        LocalDate returnDate = LocalDate.now().plusDays(7);
        BorrowingRecord borrowingRecord = new BorrowingRecord(book, patron, borrowingDate, returnDate);

        // Act
        borrowingRecordRepository.save(borrowingRecord);

        // Assert
        assertThat(borrowingRecordRepository.findById(borrowingRecord.getId())).isPresent();
    }

    @Test
    @Transactional
    public void whenUpdateBorrowingRecord_thenBorrowingRecordIsUpdated() {
        // Arrange
        Book book = Book.builder().title("Title 1").author("Author 1").isbn("M-123").publicationYear(2024).isBorrowed(false).build();
        Patron patron = Patron.builder().name("Patron 2").contactInformation("+234851456981").build();
        bookRepository.save(book);
        patronRepository.save(patron);
        LocalDate borrowingDate = LocalDate.now();
        LocalDate returnDate = LocalDate.now().plusDays(7);
        BorrowingRecord borrowingRecord = new BorrowingRecord(book, patron, borrowingDate, returnDate);
        borrowingRecordRepository.save(borrowingRecord);

        // Act
        borrowingRecord.setReturnDate(LocalDate.now());
        borrowingRecordRepository.save(borrowingRecord);

        // Assert
        assertThat(borrowingRecordRepository.findById(borrowingRecord.getId()))
                .isPresent().get().extracting(BorrowingRecord::getReturnDate)
                .isEqualTo(LocalDate.now());
    }

    @Test
    @Transactional
    public void whenFindFirstByBookIdAndPatronIdOrderByBorrowingDateDesc_thenBorrowingRecordIsFound() {
        // Arrange
        Book book = Book.builder().title("Title 1").author("Author 1").isbn("M-123").publicationYear(2024).isBorrowed(false).build();
        Patron patron = Patron.builder().name("Patron 2").contactInformation("+234851456981").build();
        bookRepository.save(book);
        patronRepository.save(patron);
        LocalDate borrowingDate = LocalDate.now();
        LocalDate returnDate = LocalDate.now().plusDays(7);
        BorrowingRecord borrowingRecord = BorrowingRecord.builder().book(book).patron(patron).borrowingDate(borrowingDate).returnDate(returnDate).build();

        // Check if the book and patron exist before saving the borrowing record
        Optional<Book> optionalBook = bookRepository.findById(book.getId());
        Optional<Patron> optionalPatron = patronRepository.findById(patron.getId());
        assertThat(optionalBook).isPresent();
        assertThat(optionalPatron).isPresent();

        // Act
        BorrowingRecord savedRecord = borrowingRecordRepository.save(borrowingRecord);
        Optional<BorrowingRecord> foundRecord = borrowingRecordRepository.findFirstByBookIdAndPatronIdOrderByBorrowingDateDesc(book.getId(), patron.getId());

        // Assert
        assertThat(foundRecord).isPresent();
        assertThat(foundRecord.get().getId()).isEqualTo(savedRecord.getId());
        assertThat(foundRecord.get().getBorrowingDate()).isEqualTo(savedRecord.getBorrowingDate());
        assertThat(foundRecord.get().getReturnDate()).isEqualTo(savedRecord.getReturnDate());
        assertThat(foundRecord.get().getBook()).isEqualTo(savedRecord.getBook());
        assertThat(foundRecord.get().getPatron()).isEqualTo(savedRecord.getPatron());
    }
}
