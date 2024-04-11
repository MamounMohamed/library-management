package Mamoun.librarymanagement.Repositories;

import Mamoun.librarymanagement.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}


