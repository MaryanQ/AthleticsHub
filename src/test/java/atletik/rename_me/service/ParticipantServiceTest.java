package atletik.rename_me.service;

import atletik.rename_me.entity.Discipline;
import atletik.rename_me.entity.Participant;
import atletik.rename_me.entity.Result;
import atletik.rename_me.enums.Gender;
import atletik.rename_me.enums.ResultType;
import atletik.rename_me.repository.DisciplineRepository;
import atletik.rename_me.repository.ParticipantRepository;
import atletik.rename_me.repository.ResultRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.ExpectedCount.times;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private DisciplineRepository disciplineRepository;

    @Mock
    private ResultRepository resultRepository;

    @InjectMocks
    private ParticipantService participantService;


    @Test
    void getAllParticipants() {
        // Arrange
        when(participantRepository.findAll()).thenReturn(List.of(
                new Participant("John ", "Doe", Gender.MALE, 25, "ABC Klub")));

        // Act
        List<Participant> participants = participantService.getAllParticipants();

        // Assert
        assertNotNull(participants);
        assertEquals(1, participants.size());
        verify(participantRepository).findAll();
    }

    @Test
    void getParticipantById() {

        // Arrange
        Participant participant = new Participant("Jane", "Smith", Gender.FEMALE, 28, "XYZ Klub");
        participant.setId(1L);
        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));

        // Act
        Participant result = participantService.getParticipantById(1L).orElse(null);

        // Assert
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        verify(participantRepository, Mockito.times(1)).findById(1L);
    }
    @Test
    void createParticipant() {
        // Arrange
        Participant newParticipant = new Participant("Emily", "Johnson", Gender.FEMALE, 23, "LMN Klub");
        when(participantRepository.save(newParticipant)).thenReturn(newParticipant);

        // Act
        Participant created = participantService.createParticipant(newParticipant);

        // Assert
        assertNotNull(created);
        assertEquals("Emily", created.getFirstName());
        verify(participantRepository, Mockito.times(1)).save(newParticipant);
    }

    @Test
    void updateParticipant() {
        // Arrange
        Participant existingParticipant = new Participant("Michael", "Brown", Gender.MALE, 30, "DEF Klub");
        existingParticipant.setId(2L);
        when(participantRepository.findById(2L)).thenReturn(Optional.of(existingParticipant));
        when(participantRepository.save(existingParticipant)).thenReturn(existingParticipant);

        // Act
        Participant updated = participantService.updateParticipant(2L, existingParticipant);

        // Assert
        assertNotNull(updated);
        assertEquals("Michael", updated.getFirstName());
        verify(participantRepository, Mockito.times(1)).findById(2L);
        verify(participantRepository, Mockito.times(1)).save(existingParticipant);
    }

    @Test
    void deleteParticipant() {
        // Arrange
        long participantId = 3L;
        doNothing().when(participantRepository).deleteById(participantId);

        // Act
        participantService.deleteParticipant(participantId);

        // Assert
        verify(participantRepository, Mockito.times(1)).deleteById(participantId);
    }

    @Test
    void searchParticipantsByName() {
        // Arrange
        when(participantRepository.findByFirstNameContainingIgnoreCase("John")).thenReturn(List.of(new Participant("John", "Doe", Gender.MALE, 25, "ABC Klub")));

        // Act
        List<Participant> participants = participantService.searchParticipantsByName("John");

        // Assert
        assertNotNull(participants);
        assertEquals(1, participants.size());
        verify(participantRepository, Mockito.times(1)).findByFirstNameContainingIgnoreCase("John");
    }

    @Test
    void listParticipants() {
        // Arrange
        when(participantRepository.findAll()).thenReturn(List.of(new Participant("Linda", "White", Gender.FEMALE, 22, "GHI Klub")));

        // Act
        List<Participant> participants = participantService.listParticipants(null, null, null, null);

        // Assert
        assertNotNull(participants);
        assertEquals(1, participants.size());
        verify(participantRepository, Mockito.times(1)).findAll();

    }


    @Test
    void addDisciplineToParticipant() {
        // Arrange
        Participant participant = new Participant("Sarah", "Black", Gender.FEMALE, 26, "MNO Klub");
        participant.setId(4L);
        Discipline discipline = new Discipline("100m Løb", ResultType.TIME);
        discipline.setId(1L);
        when(participantRepository.findById(4L)).thenReturn(Optional.of(participant));
        when(disciplineRepository.findById(1L)).thenReturn(Optional.of(discipline));
        when(participantRepository.save(participant)).thenReturn(participant);

        // Act
        Participant updatedParticipant = participantService.addDisciplineToParticipant(4L, 1L);

        // Assert
        assertNotNull(updatedParticipant);
        assertTrue(updatedParticipant.getDisciplines().contains(discipline));
        verify(participantRepository, Mockito.times(1)).save(participant);
    }

    @Test
    void updateResult() {
        // Arrange
        Participant participant = new Participant("James", "Blue", Gender.MALE, 29, "PQR Klub");
        participant.setId(5L);
        Discipline discipline = new Discipline("Længdespring", ResultType.DISTANCE);
        discipline.setId(2L);
        Result result = new Result(participant, discipline, "5.20", LocalDate.now());
        when(participantRepository.findById(5L)).thenReturn(Optional.of(participant));
        when(disciplineRepository.findById(2L)).thenReturn(Optional.of(discipline));
        when(resultRepository.save(result)).thenReturn(result);

        // Act
        Result createdResult = participantService.addResultToParticipant(5L, 2L, result);

        // Assert
        assertNotNull(createdResult);
        assertEquals("5.20", createdResult.getResultValue());
        verify(resultRepository, Mockito.times(1)).save(result);
    }

    @Test
    void deleteResult() {
        long resultId = 7L;
        doNothing().when(resultRepository).deleteById(resultId);

        // Act
        participantService.deleteResult(resultId);

        // Assert
        verify(resultRepository, Mockito.times(1)).deleteById(resultId);
    }
}