package Mamoun.librarymanagement.DTO;

import Mamoun.librarymanagement.Entities.Book;
import Mamoun.librarymanagement.Entities.Patron;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowingRecordDTO {

    private long id;
    private long bookId;

    private long patronId;
    private LocalDate borrowingDate;
    private LocalDate returnDate;

}
