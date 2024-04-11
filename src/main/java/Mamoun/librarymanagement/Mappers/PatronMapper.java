package Mamoun.librarymanagement.Mappers;

import Mamoun.librarymanagement.DTO.PatronDTO;
import Mamoun.librarymanagement.Entities.Patron;
import org.springframework.stereotype.Component;

@Component
public class PatronMapper {
    public PatronDTO toPatronDTO (Patron patron){
        return new PatronDTO(patron.getId(),patron.getName(),patron.getContactInformation());
    }
    public Patron toPatron (PatronDTO patronDTO){
        return new Patron(patronDTO.getId(),patronDTO.getName(),patronDTO.getContactInformation());

    }
}
