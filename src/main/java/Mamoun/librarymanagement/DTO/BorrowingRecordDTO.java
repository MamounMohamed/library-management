package Mamoun.librarymanagement.DTO;

import Mamoun.librarymanagement.Entities.Book;
import Mamoun.librarymanagement.Entities.Patron;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingRecordDTO {

    private Long id;
    private long bookId;

    private long patronId;
    private LocalDate borrowingDate;
    private LocalDate returnDate;

}
