package Mamoun.librarymanagement.Mappers;

import Mamoun.librarymanagement.DTO.BorrowingRecordDTO;
import Mamoun.librarymanagement.Entities.Book;
import Mamoun.librarymanagement.Entities.BorrowingRecord;
import Mamoun.librarymanagement.Entities.Patron;
import org.springframework.stereotype.Component;

@Component
public class BorrowingRecordMapper {
    public BorrowingRecord toBorrowingRecord(BorrowingRecordDTO borrowingRecordDTO , Book book , Patron patron){

        return new BorrowingRecord(
                borrowingRecordDTO.getId(),book,patron,
                borrowingRecordDTO.getBorrowingDate(),borrowingRecordDTO.getReturnDate());
    }
    public BorrowingRecordDTO toBorrowingRecordDTo(BorrowingRecord borrowingRecord){
        return new BorrowingRecordDTO(
                borrowingRecord.getId(),borrowingRecord.getBook().getId(),borrowingRecord.getPatron().getId(),
                borrowingRecord.getBorrowingDate(),borrowingRecord.getReturnDate());
    }


}
