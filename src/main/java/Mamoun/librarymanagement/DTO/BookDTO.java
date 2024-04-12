package Mamoun.librarymanagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String author;
    @NonNull
    private int publicationYear;
    @NonNull
    private String isbn;
    @NonNull
    private boolean isBorrowed;
}