package atletik.rename_me.service;

import atletik.rename_me.entity.Discipline;
import atletik.rename_me.entity.Participant;
import atletik.rename_me.entity.Result;
import atletik.rename_me.repository.DisciplineRepository;
import atletik.rename_me.repository.ParticipantRepository;

import atletik.rename_me.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final DisciplineRepository disciplineRepository;
    private final ResultRepository resultRepository;

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository, DisciplineRepository disciplineRepository, ResultRepository resultRepository) {
        this.participantRepository = participantRepository;
        this.disciplineRepository = disciplineRepository;
        this.resultRepository = resultRepository;
    }

    // 1. Hent alle deltagere
    public List<Participant> getAllParticipants() {
        // Denne metode bruger findAll()-metoden fra participantRepository til at hente en liste over alle participant-objekter fra databasen.
        // Den returnerer en liste (List<Participant>) med alle deltagere.
        return participantRepository.findAll();
    }

    // 2. Hent detaljer om en specifik deltager ved ID
    public Optional<Participant> getParticipantById(Long id) {
        // Denne metode finder en deltager baseret på deres id. Den bruger findById(), som returnerer en Optional<Participant>.
        // Optional bruges her til at håndtere det tilfælde, hvor der ikke findes en deltager med det givne id. Hvis deltageren findes, returneres objektet, ellers returneres en tom Optional.
        return participantRepository.findById(id);
    }

    // 3. Opret en ny deltager
    public Participant createParticipant(Participant participant) {
        // Denne metode opretter en ny deltager ved at bruge save()-metoden fra participantRepository. save() gemmer participant-objektet i databasen og returnerer det gemte objekt.
        // Hvis deltageren allerede findes, opdaterer save() den eksisterende post.
        return participantRepository.save(participant);
    }

    // 4. Opdater en eksisterende deltager
    public Participant updateParticipant(Long id, Participant updatedParticipant) {
        // Denne metode opdaterer en deltager baseret på deres id. Først forsøger den at finde deltageren i databasen ved hjælp af findById().
        // Hvis deltageren findes (altså existingParticipant ikke er null), opdateres deres egenskaber med værdierne fra updatedParticipant.
        // Til sidst gemmes den opdaterede deltager i databasen ved hjælp af save(), og den opdaterede deltager returneres. Hvis deltageren ikke findes, returnerer metoden null.
        Participant existingParticipant = participantRepository.findById(id).orElse(null);

        if (existingParticipant != null) {
            existingParticipant.setFirstName(updatedParticipant.getFirstName());
            existingParticipant.setLastName(updatedParticipant.getLastName());
            existingParticipant.setGender(updatedParticipant.getGender());
            existingParticipant.setAge(updatedParticipant.getAge());
            existingParticipant.setClub(updatedParticipant.getClub());
            return participantRepository.save(existingParticipant);
        }
        return null;
    }

    // 5. Slet en deltager baseret på ID
    public void deleteParticipant(Long id) {
        // Load the Participant from the database
        Optional<Participant> participantOpt = participantRepository.findById(id);

        if (participantOpt.isPresent()) {
            Participant participant = participantOpt.get();

            // Delete all associated Result entries
            List<Result> results = new ArrayList<>(participant.getResults()); // Copy to avoid concurrent modification
            // Delete each result
            resultRepository.deleteAll(results);
            participant.getResults().clear(); // Clear the list of results in Participant

            // Remove associations with Disciplines
            participant.getDisciplines().clear(); // Clear disciplines

            // Save participant to ensure associations are removed
            participantRepository.save(participant);

            // Finally, delete the participant
            participantRepository.delete(participant);
        } else {
            throw new RuntimeException("Participant not found");
        }
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





    // 7. Tilføj en disciplin til en deltager
    public Participant addDisciplineToParticipant(Long participantId, Long disciplineId) {
        Optional<Participant> participantOpt = participantRepository.findById(participantId);
        Optional<Discipline> disciplineOpt = disciplineRepository.findById(disciplineId);

        if (participantOpt.isPresent() && disciplineOpt.isPresent()) {
            Participant participant = participantOpt.get();
            Discipline discipline = disciplineOpt.get();
            participant.getDisciplines().add(discipline);
            return participantRepository.save(participant);
        } else {
            throw new RuntimeException("Participant or Discipline not found");
        }
    }

    // 9. Tilføj et resultat for en deltager i en given disciplin
    public Result addResultToParticipant(Long participantId, Long disciplineId, Result result) {
        Optional<Participant> participantOpt = participantRepository.findById(participantId);
        Optional<Discipline> disciplineOpt = disciplineRepository.findById(disciplineId);

        if (participantOpt.isPresent() && disciplineOpt.isPresent()) {
            result.setParticipant(participantOpt.get());
            result.setDiscipline(disciplineOpt.get());
            return resultRepository.save(result);
        }
        return null;
    }

    public Result updateResult(Long resultId, Result updatedResult) {
        Optional<Result> resultOpt = resultRepository.findById(resultId);

        if (resultOpt.isPresent()) {
            Result existingResult = resultOpt.get();
            existingResult.setResultValue(updatedResult.getResultValue());
            existingResult.setDate(updatedResult.getDate());
            return resultRepository.save(existingResult);
        }
        return null;
    }
    // 11. Slet et resultat
    public void deleteResult(Long resultId) {
        resultRepository.deleteById(resultId);
    }

}
