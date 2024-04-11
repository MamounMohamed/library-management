package Mamoun.librarymanagement.Controllers;

import Mamoun.librarymanagement.DTO.PatronDTO;
import Mamoun.librarymanagement.Exceptions.DeleteException;
import Mamoun.librarymanagement.Exceptions.NotFoundException;
import Mamoun.librarymanagement.Services.PatronService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PatronController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PatronControllerTest {

    @MockBean
    private PatronService patronService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private PatronDTO samplePatronDTO;

    @BeforeEach
    void setUp() {
        // Sample PatronDTO for testing
        samplePatronDTO = PatronDTO.builder()
                .id(1L)
                .name("Sample Patron")
                .contactInformation("1234567890")
                .build();
    }
    @Test
    void getAllPatrons_Successful() throws Exception {
        // Mock service response for getting all patrons
        List<PatronDTO> patronList = new ArrayList<>();
        patronList.add(samplePatronDTO);
        patronList.add(samplePatronDTO);
        when(patronService.getAllPatrons()).thenReturn(patronList);

        ResultActions response = mockMvc.perform(get("/api/patrons")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[1].id", is(samplePatronDTO.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(samplePatronDTO.getName())))
                .andExpect(jsonPath("$[1].contactInformation", is(samplePatronDTO.getContactInformation())))
                .andExpect(jsonPath("$[1].id", is(samplePatronDTO.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(samplePatronDTO.getName())))
                .andExpect(jsonPath("$[1].contactInformation", is(samplePatronDTO.getContactInformation())));
    }

    @Test
    void getPatronById_PatronFound_Successful() throws Exception {
        // Mock service response for a patron found scenario
        long patronId = 1;
        when(patronService.getPatronById(patronId)).thenReturn(samplePatronDTO);

        ResultActions response = mockMvc.perform(get("/api/patrons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(samplePatronDTO)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(samplePatronDTO.getName())))
                .andExpect(jsonPath("$.contactInformation", is(samplePatronDTO.getContactInformation())));
    }
    @Test
    void getPatronById_PatronFound_NotFound() throws Exception {
        // Mock service response for a patron found scenario
        long patronId = 1;
        when(patronService.getPatronById(patronId)).thenThrow(new NotFoundException("Patron not found"));

        ResultActions response = mockMvc.perform(get("/api/patrons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(samplePatronDTO)));

        response.andExpect(status().isNotFound());

    }

    @Test
    void addPatron_Successful() throws Exception {
        // Mock service response for adding a patron
        when(patronService.addPatron(any(PatronDTO.class))).thenReturn(samplePatronDTO);

        ResultActions response = mockMvc.perform(post("/api/patrons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(samplePatronDTO)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(samplePatronDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(samplePatronDTO.getName())))
                .andExpect(jsonPath("$.contactInformation", is(samplePatronDTO.getContactInformation())));
    }
    @Test
    void addPatron_InvalidRequestBody() throws Exception {
        // Mock service response for adding a patron with invalid request body
        when(patronService.addPatron(any(PatronDTO.class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid request body"));

        ResultActions response = mockMvc.perform(post("/api/patrons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""));

        response.andExpect(status().isBadRequest());
    }
    @Test
    void updatePatron_PatronFound_Successful() throws Exception {
        // Mock service response for updating a patron
        long patronId = 1;
        when(patronService.updatePatron(eq(patronId), any(PatronDTO.class))).thenReturn(samplePatronDTO);

        ResultActions response = mockMvc.perform(put("/api/patrons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(samplePatronDTO)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(samplePatronDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(samplePatronDTO.getName())))
                .andExpect(jsonPath("$.contactInformation", is(samplePatronDTO.getContactInformation())));
    }

    @Test
    void updatePatron_PatronNotFound() throws Exception {
        // Mock service response for updating a non-existing patron
        long nonExistingPatronId = 100;
        when(patronService.updatePatron(eq(nonExistingPatronId), any(PatronDTO.class))).thenThrow(new NotFoundException("Patron not found"));

        ResultActions response = mockMvc.perform(put("/api/patrons/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(samplePatronDTO)));

        response.andExpect(status().isNotFound());
    }

    @Test
    void updatePatron_InvalidRequestBody() throws Exception {
        // Mock service response for updating a patron with invalid request body
        long patronId = 1;
        when(patronService.updatePatron(eq(patronId), any(PatronDTO.class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid request body"));

        ResultActions response = mockMvc.perform(put("/api/patrons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""));

        response.andExpect(status().isBadRequest());
    }

    @Test
    void deletePatron_PatronDeletedSuccessfully() throws Exception {
        // Mock service response for deleting a patron
        long patronId = 1;
        doNothing().when(patronService).deletePatron(eq(patronId));

        ResultActions response = mockMvc.perform(delete("/api/patrons/1"));

        response.andExpect(status().isNoContent());
    }

    @Test
    void deletePatron_PatronDeletedFail() throws Exception {
        // Mock service response for deleting a patron failure
        long patronId = 1;
        doThrow(new DeleteException("Patron can't be deleted")).when(patronService).deletePatron(patronId);
        ResultActions response = mockMvc.perform(delete("/api/patrons/1"));
        response.andExpect(status().isConflict());
    }




}
