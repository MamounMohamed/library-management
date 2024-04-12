package Mamoun.librarymanagement.Controllers;

import Mamoun.librarymanagement.DTO.PatronDTO;
import Mamoun.librarymanagement.Exceptions.DeleteException;
import Mamoun.librarymanagement.Exceptions.NotFoundException;
import Mamoun.librarymanagement.Services.PatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    private final PatronService patronService;

    @Autowired
    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPatrons() {
        try {
            List<PatronDTO> patrons = patronService.getAllPatrons();
            return ResponseEntity.ok(patrons);
        }
        catch (Exception e){
            return new ResponseEntity<>("Internal Server Error occurred while serving the request",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatronById(@PathVariable Long id) {
        try {
            PatronDTO patron = patronService.getPatronById(id);
            return ResponseEntity.ok(patron);
        }
        catch (NotFoundException e ){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>("Internal Server Error occurred while serving the request",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping
    public ResponseEntity<?> addPatron(@RequestBody PatronDTO patronDTO) {
        try {
            PatronDTO addedPatron = patronService.addPatron(patronDTO);
            return new ResponseEntity<>(addedPatron, HttpStatus.CREATED);
        }
        catch (HttpClientErrorException.BadRequest e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST );
        }
        catch (Exception e){
            return new ResponseEntity<>("Internal Server Error occurred while serving the request",HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatron(@PathVariable Long id, @RequestBody PatronDTO updatedPatronDTO) {
        try {
            PatronDTO updatedPatron = patronService.updatePatron(id, updatedPatronDTO);
            return ResponseEntity.ok(updatedPatron);
        }catch (HttpClientErrorException.BadRequest e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST );
        }
        catch (NotFoundException e ){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>("Internal Server Error occurred while serving the request",HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatron(@PathVariable Long id) {
        try {
            patronService.deletePatron(id);
            return ResponseEntity.noContent().build();
        }catch (DeleteException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
        catch (Exception e){
            return new ResponseEntity<>("Internal Server Error occurred while serving the request",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
