package Mamoun.librarymanagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

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
    @NonNull
    private LocalDate borrowingDate;
    @NonNull
    private LocalDate returnDate;

}
