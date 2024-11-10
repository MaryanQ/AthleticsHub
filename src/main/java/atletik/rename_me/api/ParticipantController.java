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

    // Create a new participant
    @PostMapping
    public Participant createParticipant(@RequestBody Participant participant) {
        return participantService.createParticipant(participant);
    }

    // Get all participants
    @GetMapping
    public List<Participant> getAllParticipants() {
        return participantService.getAllParticipants();
    }

    // Get a participant by ID
    @GetMapping("/{id}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable Long id) {
        return participantService.getParticipantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update a participant by ID


    // Delete a participant by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.noContent().build();
    }

    // Add a discipline to a participant
    @PostMapping("/{participantId}/disciplines/{disciplineId}")
    public Participant addDisciplineToParticipant(@PathVariable Long participantId, @PathVariable Long disciplineId) {
        return participantService.addDisciplineToParticipant(participantId, disciplineId);
    }

    // Remove a discipline from a participant
    @DeleteMapping("/{participantId}/disciplines/{disciplineId}")
    public Participant removeDisciplineFromParticipant(@PathVariable Long participantId, @PathVariable Long disciplineId) {
        return participantService.removeDisciplineFromParticipant(participantId, disciplineId);
    }

    // Add a result to a participant
    @PostMapping("/{participantId}/results")
    public Result addResultToParticipant(@PathVariable Long participantId, @RequestBody Result result) {
        return participantService.addResultToParticipant(participantId, result);
    }

    // Remove a result by result ID
    @DeleteMapping("/results/{resultId}")
    public ResponseEntity<Void> removeResult(@PathVariable Long resultId) {
        participantService.removeResult(resultId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateParticipant(
            @PathVariable Long id, @RequestBody Participant updatedParticipant) {
        Participant participant = participantService.updateParticipant(id, updatedParticipant);
        return ResponseEntity.ok(participant);
    }



    // 6. Søg deltagere baseret på navn
    @GetMapping("/search")
    public ResponseEntity<List<Participant>> searchParticipantsByName(@RequestParam String name) {
        List<Participant> participants = participantService.searchParticipantsByName(name);
        return ResponseEntity.ok(participants);
    }

    // 7. List deltagere med sortering og filtreringsmuligheder
    // Endpoint til at liste deltagere med filtreringsmuligheder

    @GetMapping("/participants")
    public ResponseEntity<List<Participant>> listParticipants(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String ageGroup,
            @RequestParam(required = false) String club,
            @RequestParam(required = false) String discipline) {
        List<Participant> participants = participantService.listParticipants(gender, ageGroup, club, discipline);
        return ResponseEntity.ok(participants);
    }





}
