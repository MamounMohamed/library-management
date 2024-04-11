package Mamoun.librarymanagement.Repositories;

import Mamoun.librarymanagement.Entities.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord,Long> {
    Optional<BorrowingRecord> findFirstByBookIdAndPatronIdOrderByBorrowingDateDesc(Long bookId, Long patronId);

}
