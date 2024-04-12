package Mamoun.librarymanagement.Controllers;


import Mamoun.librarymanagement.DTO.BorrowingRecordDTO;
import Mamoun.librarymanagement.Exceptions.BookNotAvailableException;
import Mamoun.librarymanagement.Exceptions.NotFoundException;
import Mamoun.librarymanagement.Services.BorrowingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/api")
public class BorrowingRecordController {

    private final BorrowingRecordService borrowingRecordService;

    @Autowired
    public BorrowingRecordController(BorrowingRecordService borrowingRecordService) {
        this.borrowingRecordService = borrowingRecordService;
    }

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<?> borrowBook(
            @PathVariable("bookId") long bookId,
            @PathVariable("patronId") long patronId) {
        try {
            BorrowingRecordDTO borrowedBook = borrowingRecordService.borrowBook(bookId, patronId);
            return new ResponseEntity<>(borrowedBook, HttpStatus.CREATED);
        }catch (HttpClientErrorException.BadRequest e) {
            return new ResponseEntity<>("Bad Request ",HttpStatus.BAD_REQUEST);
        }
        catch (BookNotAvailableException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error occurred while serving the request\nMessage: ",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<?> returnBook(
            @PathVariable("bookId") long bookId,
            @PathVariable("patronId") long patronId) {
        try {
            BorrowingRecordDTO returnedBook = borrowingRecordService.returnBook(bookId, patronId);
            return new ResponseEntity<>(returnedBook, HttpStatus.OK);
        }
        catch (HttpClientErrorException.BadRequest e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST );
        }
        catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (BookNotAvailableException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error occurred while serving the request\nMessage: ",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
