package Mamoun.librarymanagement.Controllers;

import Mamoun.librarymanagement.DTO.BookDTO;
import Mamoun.librarymanagement.Exceptions.DeleteException;
import Mamoun.librarymanagement.Exceptions.NotFoundException;
import Mamoun.librarymanagement.Services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Get all books
    @GetMapping
    public ResponseEntity<?> getAllBooks() {
        try {
            List<BookDTO> books = bookService.getAllBooks();
            return ResponseEntity.ok(books);
        } catch (Exception e){
            return new ResponseEntity<>("Internal Server Error occurred while serving the request\nMessage: " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // Get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        try {
            BookDTO book = bookService.getBookById(id);
            return ResponseEntity.ok(book);
        }
        catch (NotFoundException e ){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>("Internal Server Error occurred while serving the request\nMessage: " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    // Add a new book
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody BookDTO bookDTO) {
        try {
            BookDTO addedBook = bookService.addBook(bookDTO);
            return new ResponseEntity<>(addedBook, HttpStatus.CREATED);
        }catch (HttpClientErrorException.BadRequest e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST );
        }
        catch (Exception e){
            return new ResponseEntity<>("Internal Server Error occurred while serving the request\nMessage: " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // Update a book
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody BookDTO updatedBookDTO) {
        try {

            BookDTO updatedBook = bookService.updateBook(id, updatedBookDTO);
            return ResponseEntity.ok(updatedBook);
            }catch (HttpClientErrorException.BadRequest e) {
              return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST );
        }
        catch (NotFoundException e ){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>("Internal Server Error occurred while serving the request\nMessage: " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    // Delete a book
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        }catch (DeleteException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
        catch (Exception e){
            return new ResponseEntity<>("Internal Server Error occurred while serving the request\nMessage: " + e.getMessage() ,HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
