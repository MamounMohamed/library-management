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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service

public class BorrowingRecordService {
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final BorrowingRecordMapper borrowingRecordMapper;
    @Autowired

    public BorrowingRecordService(BorrowingRecordRepository borrowingRecordRepository, BookRepository bookRepository, PatronRepository patronRepository, BorrowingRecordMapper borrowingRecordMapper) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.borrowingRecordMapper = borrowingRecordMapper;
    }


@Transactional
    public BorrowingRecordDTO borrowBook(long bookId , long patronId){
       Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found with id: " + bookId));
       Patron patron = patronRepository.findById(patronId).orElseThrow(()->new NotFoundException("patron not found with id: " + patronId));
       if(book.isBorrowed()){
           throw new BookNotAvailableException("book with id: " + bookId +" is already borrowed at the moment");
       }
       book.setBorrowed(true);
       BorrowingRecord borrowingRecord = new BorrowingRecord(book,patron,LocalDate.now(),LocalDate.of(10000,12,31));
       BorrowingRecord savedRecord = borrowingRecordRepository.save(borrowingRecord);
       return borrowingRecordMapper.toBorrowingRecordDTo(savedRecord);
    }
    @Transactional
    public BorrowingRecordDTO returnBook (long bookId ,long patronId ){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found with id: " + bookId));
        Patron patron = patronRepository.findById(patronId).orElseThrow(()->new NotFoundException("patron not found with id: " + patronId));
        if(!book.isBorrowed()){
            throw new BookNotAvailableException("book with id: " + bookId +" is already in stock at the moment");
        }
        BorrowingRecord borrowingRecord = borrowingRecordRepository
                .findFirstByBookIdAndPatronIdOrderByBorrowingDateDesc(bookId, patronId)
                .orElseThrow(() -> new NotFoundException("No active borrowing record found for the book and patron"));

        book.setBorrowed(false);
        borrowingRecord.setReturnDate(LocalDate.now());
        BorrowingRecord savedBorrowingRecord = borrowingRecordRepository.save(borrowingRecord);
        return borrowingRecordMapper.toBorrowingRecordDTo(savedBorrowingRecord);
    }
}

