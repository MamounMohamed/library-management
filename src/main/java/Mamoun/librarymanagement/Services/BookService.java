package Mamoun.librarymanagement.Services;

import Mamoun.librarymanagement.DTO.BookDTO;
import Mamoun.librarymanagement.Entities.Book;
import Mamoun.librarymanagement.Exceptions.DeleteException;
import Mamoun.librarymanagement.Exceptions.NotFoundException;
import Mamoun.librarymanagement.Mappers.BookMapper;
import Mamoun.librarymanagement.Repositories.BookRepository;
import Mamoun.librarymanagement.Repositories.BorrowingRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BookMapper bookMapper;

    @Autowired
    public BookService(BookRepository bookRepository, BookMapper bookMapper , BorrowingRecordRepository borrowingRecordRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.borrowingRecordRepository = borrowingRecordRepository;
    }


    // Retrieve a list of all books
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();

        return books.stream()
                .map(bookMapper::toBookDTO)
                .collect(Collectors.toList());
    }

    // Retrieve details of a specific book by ID
    public BookDTO getBookById(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            return bookMapper.toBookDTO(book);
        } else {
            throw new NotFoundException("Book not found with id: " + id);
        }

    }

    // Add a new book to the library
    @Transactional
    public BookDTO addBook(BookDTO bookDTO) {
        Book book = bookMapper.toBook(bookDTO);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toBookDTO(savedBook);
    }

    // Update an existing book's information
    @Transactional
    public BookDTO updateBook(Long id, BookDTO updatedBookDTO) {

        if (bookRepository.existsById(id)) {
            Book updatedBook = bookMapper.toBook(updatedBookDTO);
            updatedBook.setId(id);
            Book savedBook= bookRepository.save(updatedBook);
            return bookMapper.toBookDTO(savedBook);
        } else {
            throw new NotFoundException("Book not found with id: " + id);
        }
    }

    @Transactional
    public void deleteBook(Long id) {
        if(borrowingRecordRepository.existsByBookId(id))
            throw new DeleteException("Book with id: " + id + " can't be deleted because it has a borrowing record");
        bookRepository.deleteById(id);
    }
}

