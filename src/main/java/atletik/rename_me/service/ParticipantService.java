package atletik.rename_me.service;

import atletik.rename_me.entity.Discipline;
import atletik.rename_me.entity.Participant;
import atletik.rename_me.entity.Result;
import atletik.rename_me.exception.ParticipantNotFoundException;
import atletik.rename_me.repository.DisciplineRepository;
import atletik.rename_me.repository.ParticipantRepository;

import atletik.rename_me.repository.ResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final DisciplineRepository disciplineRepository;
    private final ResultRepository resultRepository;
    private static final Logger logger = LoggerFactory.getLogger(ParticipantService.class);


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




    // 6. Søg deltagere baseret på navn
    public List<Participant> searchParticipantsByName(String name) {
        // Denne metode søger efter deltagere, hvis navne indeholder en bestemt søgestreng (name).
        // Metoden findByNameContainingIgnoreCase() antages her at være en metode i participantRepository, der kan finde deltagere,
        // hvor navnet indeholder søgestrengen uanset store eller små bogstaver.
        // Denne slags metode kræver, at participantRepository har en brugerdefineret søgning baseret på navn, f.eks. gennem Spring Data JPA’s query derivation.
        return participantRepository.findByFirstNameContainingIgnoreCase(name);
    }

    // 7. List deltagere med sortering og filtreringsmuligheder
    public List<Participant> listParticipants(String gender, String ageGroup, String club, String discipline) {
        // Denne metode giver mulighed for at filtrere deltagere baseret på flere parametre som køn, aldersgruppe, klub og disciplin.
        // Den tjekker først, om der er en værdi i discipline. Hvis discipline ikke er tomt, kalder den findByDisciplineName() for at finde deltagere i den pågældende disciplin.
        // Hvis discipline er tomt, men mindst én af de andre parametre (gender, ageGroup, club) er angivet, kalder den filterParticipants() for at filtrere baseret på de angivne kriterier.
        // Hvis ingen parametre er angivet, kalder den blot findAll() for at returnere alle deltagere.
        // Både findByDisciplineName() og filterParticipants() antages at være brugerdefinerede metoder i participantRepository, som ville kræve specifikke databaseforespørgsler.
        if (discipline != null && !discipline.isEmpty()) {
            return participantRepository.findByDisciplineName(discipline); // Brugerdefineret query
        } else if (gender != null || ageGroup != null || club != null) {
            return participantRepository.filterParticipants(gender, ageGroup, club); // Brugerdefineret query for flere filtre
        } else {
            return participantRepository.findAll();
        }
    }




}
