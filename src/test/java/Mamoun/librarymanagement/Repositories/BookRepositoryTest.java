package Mamoun.librarymanagement.Repositories;

import Mamoun.librarymanagement.Entities.Book;
import Mamoun.librarymanagement.Repositories.BookRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    @Test
    public void whenFindAll_thenAllBooksReturned() {
        // Arrange
        Book book1 = Book.builder().title("Title 1").author("Author 1").isbn("M-123").publicationYear(2024).isBorrowed(false).build();
        Book book2 = Book.builder().title("Title 2").author("Author 2").isbn("M-124").publicationYear(2024).isBorrowed(false).build();
        bookRepository.save(book1);
        bookRepository.save(book2);

        // Act
        Iterable<Book> books = bookRepository.findAll();

        // Assert
        assertThat(books).isNotEmpty();
        assertThat(books).hasSize(2);
    }

    @Test
    public void whenFindById_thenReturnBook() {
        // Arrange
        Book book = Book.builder().title("Title 1").author("Author 1").isbn("M-123").publicationYear(2024).isBorrowed(false).build();
        bookRepository.save(book);

        // Act
        Optional<Book> foundBook = bookRepository.findById(book.getId());

        // Assert
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
        assertThat(foundBook.get().getPublicationYear()).isEqualTo(book.getPublicationYear());
        assertThat(foundBook.get().isBorrowed()).isEqualTo(book.isBorrowed());

    }

    @Test
    @Transactional
    public void whenAddBook_thenBookIsPersisted() {
        Book book = Book.builder().title("Title 1").author("Author 1").isbn("M-123").publicationYear(2024).isBorrowed(false).build();

        bookRepository.save(book);

        assertThat(bookRepository.findById(book.getId())).isPresent();
    }

    @Test
    @Transactional
    public void whenUpdateBook_thenBookIsUpdated() {

        Book book = Book.builder().title("Title 1").author("Author 1").isbn("M-123").publicationYear(2024).isBorrowed(false).build();
        bookRepository.save(book);
        String updatedTitle = "Updated Title";
        String updatedAuthor = "Updated Author";
        book.setTitle(updatedTitle);
        book.setAuthor(updatedAuthor);

        bookRepository.save(book);

        assertThat(bookRepository.findById(book.getId())).isPresent().get()
                .extracting(Book::getTitle, Book::getAuthor)
                .containsExactly(updatedTitle, updatedAuthor);
    }

    @Test
    @Transactional
    public void whenDeleteBook_thenBookIsDeleted() {
        Book book = Book.builder().title("Title 1").author("Author 1").isbn("M-123").publicationYear(2024).isBorrowed(false).build();
        bookRepository.save(book);

        bookRepository.deleteById(book.getId());

        assertThat(bookRepository.findById(book.getId())).isEmpty();
    }
}


