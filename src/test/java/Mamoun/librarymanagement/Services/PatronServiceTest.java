package Mamoun.librarymanagement.Services;

import Mamoun.librarymanagement.DTO.PatronDTO;
import Mamoun.librarymanagement.Entities.Patron;
import Mamoun.librarymanagement.Exceptions.NotFoundException;
import Mamoun.librarymanagement.Mappers.PatronMapper;
import Mamoun.librarymanagement.Repositories.PatronRepository;
import Mamoun.librarymanagement.Services.PatronService;
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
    private PatronMapper patronMapper;

    @InjectMocks
    private PatronService patronService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllPatrons_WhenPatronsExist_ReturnListOfPatrons() {
        // Arrange
        List<Patron> patrons = new ArrayList<>();
        patrons.add(Patron.builder().id(1L).name("Patron 1").contactInformation("123456789").build());
        when(patronRepository.findAll()).thenReturn(patrons);
        when(patronMapper.toPatronDTO(any())).thenReturn(PatronDTO.builder().build());

        // Act
        List<PatronDTO> result = patronService.getAllPatrons();

        // Assert
        assertThat(result).isNotEmpty();
        verify(patronRepository, times(1)).findAll();
    }

    @Test
    void getPatronById_WhenPatronExists_ReturnPatronDTO() {
        // Arrange
        Long id = 1L;
        Patron patron = Patron.builder().id(id).name("Patron 1").contactInformation("123456789").build();
        when(patronRepository.findById(id)).thenReturn(Optional.of(patron));
        when(patronMapper.toPatronDTO(patron)).thenReturn(PatronDTO.builder().build());

        // Act
        PatronDTO result = patronService.getPatronById(id);

        // Assert
        assertThat(result).isNotNull();
        verify(patronRepository, times(1)).findById(id);
    }

    @Test
    void getPatronById_WhenPatronDoesNotExist_ThrowNotFoundException() {
        // Arrange
        Long id = 1L;
        when(patronRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> patronService.getPatronById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Patron not found with id: " + id);
    }

    @Test
    void addPatron_WithValidPatronDTO_ReturnAddedPatronDTO() {
        // Arrange
        PatronDTO patronDTO = PatronDTO.builder().name("Patron 1").contactInformation("123456789").build();
        Patron patron = Patron.builder().id(1L).name("Patron 1").contactInformation("123456789").build();
        when(patronMapper.toPatron(patronDTO)).thenReturn(patron);
        when(patronRepository.save(patron)).thenReturn(patron);
        when(patronMapper.toPatronDTO(patron)).thenReturn(patronDTO);

        // Act
        PatronDTO result = patronService.addPatron(patronDTO);

        // Assert
        assertThat(result).isNotNull();
        verify(patronRepository, times(1)).save(patron);
    }

    @Test
    void updatePatron_WithValidIdAndPatronDTO_ReturnUpdatedPatronDTO() {
        // Arrange
        Long id = 1L;
        PatronDTO updatedPatronDTO = PatronDTO.builder().name("Patron 2").contactInformation("987654321").build();
        Patron updatedPatron = Patron.builder().id(id).name("Patron 2").contactInformation("987654321").build();
        when(patronRepository.existsById(id)).thenReturn(true);
        when(patronMapper.toPatron(updatedPatronDTO)).thenReturn(updatedPatron);
        when(patronRepository.save(updatedPatron)).thenReturn(updatedPatron);
        when(patronMapper.toPatronDTO(updatedPatron)).thenReturn(updatedPatronDTO);

        // Act
        PatronDTO result = patronService.updatePatron(id, updatedPatronDTO);

        // Assert
        assertThat(result).isNotNull();
        verify(patronRepository, times(1)).save(updatedPatron);
    }

    @Test
    void updatePatron_WithInvalidId_ThrowNotFoundException() {
        // Arrange
        Long id = 1L;
        PatronDTO updatedPatronDTO = PatronDTO.builder().name("Patron 2").contactInformation("987654321").build();
        when(patronRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> patronService.updatePatron(id, updatedPatronDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Patron with id: " + id + " is not found");
    }

    @Test
    void deletePatron_WithValidId_DeletePatron() {
        // Arrange
        Long id = 1L;

        // Act
        patronService.deletePatron(id);

        // Assert
        verify(patronRepository, times(1)).deleteById(id);
    }
}
