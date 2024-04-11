package Mamoun.librarymanagement.Mappers;

import Mamoun.librarymanagement.DTO.BookDTO;
import Mamoun.librarymanagement.Entities.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper{

    public  BookDTO toBookDTO(Book book){
        return new BookDTO(
                book.getId(), book.getTitle(),book.getAuthor() ,book.getPublicationYear(),book.getIsbn(), book.isBorrowed()
        );
    }

    public  Book toBook(BookDTO bookDTO){
        return new Book(
                bookDTO.getId(), bookDTO.getTitle(),bookDTO.getAuthor() ,bookDTO.getPublicationYear(),bookDTO.getIsbn(),bookDTO.isBorrowed()
        );
    }
}
