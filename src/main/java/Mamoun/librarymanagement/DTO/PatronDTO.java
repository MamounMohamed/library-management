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

public class PatronDTO {
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String contactInformation;

}
