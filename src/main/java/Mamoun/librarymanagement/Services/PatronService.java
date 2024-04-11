package Mamoun.librarymanagement.Services;

import Mamoun.librarymanagement.Entities.Patron;
import Mamoun.librarymanagement.DTO.PatronDTO;
import Mamoun.librarymanagement.Exceptions.DeleteException;
import Mamoun.librarymanagement.Exceptions.NotFoundException;
import Mamoun.librarymanagement.Mappers.PatronMapper;
import Mamoun.librarymanagement.Repositories.PatronRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class PatronService {
    private final PatronRepository patronRepository;
    private final PatronMapper patronMapper;
    @Autowired
    public PatronService(PatronRepository patronRepository, PatronMapper patronMapper) {
        this.patronRepository = patronRepository;
        this.patronMapper = patronMapper;
    }

    public List<PatronDTO> getAllPatrons(){
        List<Patron> patrons = patronRepository.findAll();
        return patrons.stream().map(patronMapper::toPatronDTO).collect(Collectors.toList());
    }
    public PatronDTO getPatronById(long id) {
        Optional<Patron> patronOptional = patronRepository.findById(id);
        if (patronOptional.isPresent()) {
            Patron patron = patronOptional.get();
            return patronMapper.toPatronDTO(patron);
        }
        throw new NotFoundException("Patron not found with id: " + id);

    }

        public PatronDTO addPatron(PatronDTO patronDTO) {
            Patron patron = patronMapper.toPatron(patronDTO);
            Patron savedPatron = patronRepository.save(patron);
            return patronMapper.toPatronDTO(savedPatron);
        }
    @Transactional
        public PatronDTO updatePatron(long id , PatronDTO patronDTO){
            if(patronRepository.existsById(id)){
                Patron patron = patronMapper.toPatron(patronDTO);
                patron.setId(id);
                Patron savedPatron = patronRepository.save(patron);
                return patronMapper.toPatronDTO(savedPatron);
            }
            throw new NotFoundException("Patron with id: " + id + " is not found");
        }
        @Transactional
    public void deletePatron(long id){
        try{
        patronRepository.deleteById(id);
        }
            catch (Exception e) {
                throw new DeleteException("book with id: " + id + " can't be deleted");
            }
        }
    }


