package Mamoun.librarymanagement.Services;

import Mamoun.librarymanagement.Entities.Patron;
import Mamoun.librarymanagement.DTO.PatronDTO;
import Mamoun.librarymanagement.Exceptions.DeleteException;
import Mamoun.librarymanagement.Exceptions.NotFoundException;
import Mamoun.librarymanagement.Mappers.PatronMapper;
import Mamoun.librarymanagement.Repositories.BorrowingRecordRepository;
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

    private final BorrowingRecordRepository borrowingRecordRepository;
    @Autowired
    public PatronService(PatronRepository patronRepository, PatronMapper patronMapper , BorrowingRecordRepository borrowingRecordRepository) {
        this.patronRepository = patronRepository;
        this.patronMapper = patronMapper;
        this.borrowingRecordRepository = borrowingRecordRepository;
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
    @Transactional
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
        if(borrowingRecordRepository.existsByPatronId(id))
            throw new DeleteException("Patron with id: " + id + " can't be deleted because it has a borrowing record");
        patronRepository.deleteById(id);
    }

}


