package atletik.rename_me.service;

import atletik.rename_me.entity.Discipline;
import atletik.rename_me.entity.Participant;
import atletik.rename_me.entity.Result;
import atletik.rename_me.enums.AgeGroup;
import atletik.rename_me.enums.Gender;
import atletik.rename_me.exception.ParticipantNotFoundException;
import atletik.rename_me.repository.DisciplineRepository;
import atletik.rename_me.repository.ParticipantRepository;

import atletik.rename_me.repository.ResultRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final DisciplineRepository disciplineRepository;
    private final ResultRepository resultRepository;
    private static final Logger logger = LoggerFactory.getLogger(ParticipantService.class);
    private static final Map<String, AgeGroup> ageGroupMap = Map.of(
            "6-9", AgeGroup.CHILD,
            "10-13", AgeGroup.YOUTH,
            "14-22", AgeGroup.JUNIOR,
            "23-40", AgeGroup.ADULT,
            "41+", AgeGroup.SENIOR
    );

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository, DisciplineRepository disciplineRepository, ResultRepository resultRepository) {
        this.participantRepository = participantRepository;
        this.disciplineRepository = disciplineRepository;
        this.resultRepository = resultRepository;
    }

    // Existing methods...

    // 1. Create a new participant
    public Participant createParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    // 2. Read all participants
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    // 3. Read a participant by ID
    public Optional<Participant> getParticipantById(Long id) {
        return participantRepository.findById(id);
    }

    public Participant updateParticipant(Long id, Participant updatedParticipant) {
        return participantRepository.findById(id)
                .map(participant -> {
                    participant.setFirstName(updatedParticipant.getFirstName());
                    participant.setLastName(updatedParticipant.getLastName());
                    participant.setGender(updatedParticipant.getGender());
                    participant.setAge(updatedParticipant.getAge());
                    participant.setClub(updatedParticipant.getClub());
                    participant.setAgeGroup(updatedParticipant.getAgeGroup());

                    // Update disciplines
                    participant.getDisciplines().clear();
                    participant.getDisciplines().addAll(updatedParticipant.getDisciplines());

                    // Clear and update results, ensuring discipline is set
                    participant.getResults().clear();
                    for (Result result : updatedParticipant.getResults()) {
                        if (result.getDiscipline() == null) {
                            throw new IllegalArgumentException("Each result must have a discipline.");
                        }
                        result.setParticipant(participant); // Set back-reference
                        participant.getResults().add(result); // Add to participant's results
                    }

                    return participantRepository.save(participant);
                })
                .orElseThrow(() -> new ParticipantNotFoundException("Participant not found with ID: " + id));
    }


    // 5. Delete a participant by ID
    public void deleteParticipant(Long id) {
        participantRepository.deleteById(id);
    }

    public List<Participant> searchParticipantsByName(String name) {
        return participantRepository.findByFirstNameContainingIgnoreCase(name);
    }

    // 6. Add a discipline to a participant
    @Transactional
    public Participant addDisciplineToParticipant(Long participantId, Long disciplineId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found with ID: " + participantId));
        Discipline discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new RuntimeException("Discipline not found with ID: " + disciplineId));

        participant.getDisciplines().add(discipline);
        return participantRepository.save(participant);
    }

    // 7. Remove a discipline from a participant
    @Transactional
    public Participant removeDisciplineFromParticipant(Long participantId, Long disciplineId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found with ID: " + participantId));
        Discipline discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new RuntimeException("Discipline not found with ID: " + disciplineId));

        participant.getDisciplines().remove(discipline);
        return participantRepository.save(participant);
    }

    // 8. Add a result to a participant
    public Result addResultToParticipant(Long participantId, Result result) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found with ID: " + participantId));
        result.setParticipant(participant);
        return resultRepository.save(result);
    }

    // 9. Remove a result by result ID
    public void removeResult(Long resultId) {
        resultRepository.deleteById(resultId);
    }




    public List<Participant> listParticipants(String genderStr, String ageGroupStr, String club, String discipline) {
        // Safely parse and map gender and age group with null checks
        Gender gender = (genderStr != null) ? Gender.valueOf(genderStr.toUpperCase()) : null;
        AgeGroup ageGroup = (ageGroupStr != null) ? ageGroupMap.get(ageGroupStr) : null;

        // Logging for debugging
        logger.info("Filtering with parameters: gender={}, ageGroup={}, club={}, discipline={}", gender, ageGroup, club, discipline);

        // Call the repository to fetch participants based on filters
        return participantRepository.filterParticipants(gender, ageGroup, club, discipline);
    }





}





