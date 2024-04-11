package Mamoun.librarymanagement.Repositories;
import Mamoun.librarymanagement.Entities.Book;
import Mamoun.librarymanagement.Entities.Patron;
import Mamoun.librarymanagement.Repositories.BookRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
public class PatronRepositoryTest {
    @Autowired
    private PatronRepository patronRepository;


    @Test
    public void whenFindAll_thenReturnAllPatrons() {
        // Arrange
        Patron patron1 = Patron.builder().name("Patron 1").contactInformation("+012042368123").build();
        Patron patron2 = Patron.builder().name("Patron 2").contactInformation("+234851456981").build();
        patronRepository.save(patron1);
        patronRepository.save(patron2);

        // Act
        Iterable<Patron> patrons = patronRepository.findAll();

        // Assert
        assertThat(patrons).isNotEmpty().hasSize(2);
    }

    @Test
    public void whenFindById_thenReturnPatron() {
        // Arrange
        Patron patron = Patron.builder().name("Patron 1").contactInformation("+012042368123").build();
        patronRepository.save(patron);

        // Act
        Optional<Patron> foundPatron = patronRepository.findById(patron.getId());

        // Assert
        assertThat(foundPatron).isPresent();
        assertThat(foundPatron.get().getName()).isEqualTo(patron.getName());
        assertThat(foundPatron.get().getContactInformation()).isEqualTo(patron.getContactInformation());

    }

    @Test
    @Transactional
    public void whenAddPatron_thenPatronIsPersisted() {
        // Arrange
        Patron patron = Patron.builder().name("Patron 1").contactInformation("+012042368123").build();

        // Act
        patronRepository.save(patron);

        // Assert
        assertThat(patronRepository.findById(patron.getId())).isPresent();
    }

    @Test
    @Transactional
    public void whenUpdatePatron_thenPatronIsUpdated() {
        // Arrange
        Patron patron = Patron.builder().name("Patron 1").contactInformation("+012042368123").build();
        patronRepository.save(patron);
        String updatedName = "Updated Patron";
        patron.setName(updatedName);

        // Act
        patronRepository.save(patron);

        // Assert
        assertThat(patronRepository.findById(patron.getId())).isPresent().get()
                .extracting(Patron::getName)
                .isEqualTo(updatedName);
    }

    @Test
    @Transactional
    public void whenDeletePatron_thenPatronIsDeleted() {
        // Arrange
        Patron patron = Patron.builder().name("Patron 1").contactInformation("+012042368123").build();
        patronRepository.save(patron);

        // Act
        patronRepository.deleteById(patron.getId());

        // Assert
        assertThat(patronRepository.findById(patron.getId())).isEmpty();
    }
}
