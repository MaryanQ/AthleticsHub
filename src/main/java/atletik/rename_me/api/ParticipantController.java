package atletik.rename_me.api;



import atletik.rename_me.entity.Discipline;
import atletik.rename_me.entity.Participant;
import atletik.rename_me.entity.Result;
import atletik.rename_me.enums.Gender;
import atletik.rename_me.enums.AgeGroup;
import atletik.rename_me.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    @Autowired
    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    // 1. Hent alle deltagere
    @GetMapping
    public ResponseEntity<List<Participant>> getAllParticipants() {
        List<Participant> participants = participantService.getAllParticipants();
        return ResponseEntity.ok(participants);
    }

    // 2. Hent detaljer om en specifik deltager ved ID
    @GetMapping("/{id}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable Long id) {
        Optional<Participant> participant = participantService.getParticipantById(id);
        return participant.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Opret en ny deltager
    @PostMapping
    public ResponseEntity<Participant> createParticipant(@RequestBody Participant participant) {
        Participant createdParticipant = participantService.createParticipant(participant);
        return ResponseEntity.ok(createdParticipant);
    }

    // 4. Opdater en eksisterende deltager
    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable Long id, @RequestBody Participant updatedParticipant) {
        Participant participant = participantService.updateParticipant(id, updatedParticipant);
        if (participant != null) {
            return ResponseEntity.ok(participant);
        }
        return ResponseEntity.notFound().build();
    }

    // 5. Slet en deltager baseret på ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.noContent().build();
    }

    // 6. Søg deltagere baseret på navn
    @GetMapping("/search")
    public ResponseEntity<List<Participant>> searchParticipantsByName(@RequestParam String name) {
        List<Participant> participants = participantService.searchParticipantsByName(name);
        return ResponseEntity.ok(participants);
    }

    // 7. List deltagere med sortering og filtreringsmuligheder
    @GetMapping("/filter")
    public ResponseEntity<List<Participant>> listParticipants(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String ageGroup,
            @RequestParam(required = false) String club,
            @RequestParam(required = false) String discipline) {

        List<Participant> participants = participantService.listParticipants(gender, ageGroup, club, discipline);
        return ResponseEntity.ok(participants);
    }

    // 8. Tilføj en disciplin til en deltager
    @PostMapping("/{participantId}/disciplines/{disciplineId}")
    public ResponseEntity<Participant> addDisciplineToParticipant(
            @PathVariable Long participantId, @PathVariable Long disciplineId) {
        try {
            Participant updatedParticipant = participantService.addDisciplineToParticipant(participantId, disciplineId);
            return ResponseEntity.ok(updatedParticipant);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    // 9. Tilføj et resultat for en deltager i en given disciplin
    @PostMapping("/{participantId}/disciplines/{disciplineId}/results")
    public ResponseEntity<Result> addResultToParticipant(
            @PathVariable Long participantId,
            @PathVariable Long disciplineId,
            @RequestBody Result result) {
        Result createdResult = participantService.addResultToParticipant(participantId, disciplineId, result);
        if (createdResult != null) {
            return ResponseEntity.ok(createdResult);
        }
        return ResponseEntity.notFound().build();
    }



    // 10. Opdater et resultat
    @PutMapping("/results/{resultId}")
    public ResponseEntity<Result> updateResult(@PathVariable Long resultId, @RequestBody Result updatedResult) {
        Result result = participantService.updateResult(resultId, updatedResult);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    // 11. Slet et resultat
    @DeleteMapping("/results/{resultId}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long resultId) {
        participantService.deleteResult(resultId);
        return ResponseEntity.noContent().build();
    }
}
