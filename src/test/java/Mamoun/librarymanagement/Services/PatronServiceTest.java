package Mamoun.librarymanagement.Services;

import Mamoun.librarymanagement.DTO.PatronDTO;
import Mamoun.librarymanagement.Entities.Patron;
import Mamoun.librarymanagement.Exceptions.DeleteException;
import Mamoun.librarymanagement.Exceptions.NotFoundException;
import Mamoun.librarymanagement.Mappers.PatronMapper;
import Mamoun.librarymanagement.Repositories.BorrowingRecordRepository;
import Mamoun.librarymanagement.Repositories.PatronRepository;
import Mamoun.librarymanagement.Services.PatronService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private PatronMapper patronMapper;

    @InjectMocks
    private PatronService patronService;

    private PatronDTO updatedPatronDTO , patronDTO;
    private Patron updatedPatron , patron;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
         updatedPatronDTO = PatronDTO.builder().name("Patron 2").contactInformation("987654321").build();
         updatedPatron = Patron.builder().id(1L).name("Patron 2").contactInformation("987654321").build();
         patron = Patron.builder().id(1L).name("Patron 1").contactInformation("123456789").build();
        patronDTO = PatronDTO.builder().id(1L).name("Patron 1").contactInformation("123456789").build();

    }

    @Test
    void getAllPatrons_WhenPatronsExist_ReturnListOfPatrons() {
        // Arrange
        List<Patron> patrons = new ArrayList<>();
        patrons.add(patron);

        when(patronRepository.findAll()).thenReturn(patrons);
        when(patronMapper.toPatronDTO(any())).thenReturn(patronDTO);

        // Act
        List<PatronDTO> result = patronService.getAllPatrons();

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(patronDTO);
        verify(patronRepository, times(1)).findAll();
    }

    @Test
    void getPatronById_WhenPatronExists_ReturnPatronDTO() {
        // Arrange
        Long id = 1L;
        when(patronRepository.findById(id)).thenReturn(Optional.of(patron));
        when(patronMapper.toPatronDTO(patron)).thenReturn(patronDTO);

        // Act
        PatronDTO result = patronService.getPatronById(id);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(patronDTO);

        verify(patronRepository, times(1)).findById(id);
    }

    @Test
    void getPatronById_WhenPatronDoesNotExist_ThrowNotFoundException() {
        // Arrange
        Long id = 1L;
        when(patronRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> patronService.getPatronById(id))
                .isInstanceOf(NotFoundException.class);
    }

    @Transactional
    @Test
    void addPatron_WithValidPatronDTO_ReturnAddedPatronDTO() {
        // Arrange
        when(patronMapper.toPatron(patronDTO)).thenReturn(patron);
        when(patronRepository.save(patron)).thenReturn(patron);
        when(patronMapper.toPatronDTO(patron)).thenReturn(patronDTO);

        // Act
        PatronDTO result = patronService.addPatron(patronDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(patronDTO);
        verify(patronRepository, times(1)).save(patron);
    }

    @Test
    @Transactional
    void updatePatron_WithValidIdAndPatronDTO_ReturnUpdatedPatronDTO() {
        // Arrange
        Long id = 1L;
        when(patronRepository.existsById(id)).thenReturn(true);
        when(patronMapper.toPatron(updatedPatronDTO)).thenReturn(updatedPatron);
        when(patronRepository.save(updatedPatron)).thenReturn(updatedPatron);
        when(patronMapper.toPatronDTO(updatedPatron)).thenReturn(updatedPatronDTO);

        // Act
        PatronDTO result = patronService.updatePatron(id, updatedPatronDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(updatedPatronDTO);
        verify(patronRepository, times(1)).save(updatedPatron);
    }

    @Transactional
    @Test
    void updatePatron_WithInvalidId_ThrowNotFoundException() {
        // Arrange
        Long id = 1L;
        when(patronRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> patronService.updatePatron(id, updatedPatronDTO))
                .isInstanceOf(NotFoundException.class);
    }

    @Transactional
    @Test
    void deletePatron_WithValidId_DeletePatron() {
        // Arrange
        Long id = 1L;

        // Act
        when(borrowingRecordRepository.existsByBookId(1L)).thenReturn(false);
        patronService.deletePatron(id);
        // Assert
        verify(patronRepository, times(1)).deleteById(id);
    }
    @Transactional
    @Test
    void deletePatron_FailedDeletion_DeleteBook() {
        // Arrange
        Long id = 1L;
        when(borrowingRecordRepository.existsByPatronId(id)).thenReturn(true);
        // Assert
        assertThatThrownBy(() -> patronService.deletePatron(id))
                .isInstanceOf(DeleteException.class);
    }

}
