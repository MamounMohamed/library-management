package Mamoun.librarymanagement.Entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity

public class BorrowingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Book book;
    @ManyToOne
    private Patron patron;

    @Column(nullable = false)

    private LocalDate borrowingDate;
    @Column(nullable = false)

    private LocalDate returnDate;

    public BorrowingRecord(Book book, Patron patron, LocalDate borrowingDate, LocalDate returnDate) {
        this.book = book;
        this.patron = patron;
        this.borrowingDate = borrowingDate;
        this.returnDate = returnDate;
    }

}
