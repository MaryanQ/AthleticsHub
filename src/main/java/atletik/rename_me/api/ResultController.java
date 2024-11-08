package atletik.rename_me.api;

import atletik.rename_me.entity.Discipline;
import atletik.rename_me.entity.Participant;
import atletik.rename_me.entity.Result;
import atletik.rename_me.enums.Gender;
import atletik.rename_me.enums.AgeGroup;
import atletik.rename_me.repository.DisciplineRepository;
import atletik.rename_me.repository.ParticipantRepository;
import atletik.rename_me.repository.ResultRepository;
import atletik.rename_me.service.ParticipantService;
import atletik.rename_me.service.ResultService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    private final ResultService resultService;
    private final ParticipantService participantService;
    private final ParticipantRepository participantRepository;
    private final DisciplineRepository disciplineRepository;
    private final ResultRepository resultRepository;

    @Autowired
    public ResultController(ResultService resultService, ParticipantService participantService,
                            ParticipantRepository participantRepository, ResultRepository resultRepository, DisciplineRepository disciplineRepository) {
        this.resultService = resultService;
        this.participantService = participantService;
        this.participantRepository = participantRepository;
        this.resultRepository = resultRepository;
        this.disciplineRepository = disciplineRepository;
    }

    // 1. Get all results
    @GetMapping
    public ResponseEntity<List<Result>> getAllResults() {
        List<Result> results = resultService.getAllResults();
        return ResponseEntity.ok(results);
    }

    // 2. Get a specific result by ID
    @GetMapping("/{id}")
    public ResponseEntity<Result> getResultById(@PathVariable Long id) {
        Optional<Result> result = resultService.getResultById(id);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 3. Create a new result for a participant in a discipline
    @PostMapping("/participants/{participantId}/disciplines/{disciplineId}")
    public ResponseEntity<Result> createResult(
            @PathVariable Long participantId,
            @PathVariable Long disciplineId,
            @RequestBody Result result) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new EntityNotFoundException("Participant not found"));
        Discipline discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new EntityNotFoundException("Discipline not found"));
        result.setParticipant(participant);
        result.setDiscipline(discipline);

        Result savedResult = resultRepository.save(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedResult);
    }

    // 4. Batch creation of results for a discipline
    @PostMapping("/batch")
    public ResponseEntity<List<Result>> createResultsForDiscipline(@RequestBody List<Result> results) {
        List<Result> createdResults = resultService.createResultsForDiscipline(results);
        return ResponseEntity.ok(createdResults);
    }

    // 5. Update an existing result
    @PutMapping("/{resultId}")
    public ResponseEntity<Result> updateResult(
            @PathVariable Long resultId,
            @RequestBody Result updatedResult) {
        Result result = resultService.updateResult(resultId, updatedResult);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    // 6. Delete a result by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
        resultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }

    // 7. Find and sort results by discipline with optional filters for gender and age group
    @GetMapping("/discipline/{disciplineId}/filter")
    public ResponseEntity<List<Result>> findResultsByDisciplineWithSortingAndFilters(
            @PathVariable Long disciplineId,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) AgeGroup ageGroup) {
        List<Result> results = resultService.findResultsByDisciplineWithSortingAndFilters(disciplineId, gender, ageGroup);
        return ResponseEntity.ok(results);
    }

    // 8. Format a specific result value
    @GetMapping("/{id}/format")
    public ResponseEntity<String> formatResultValue(@PathVariable Long id) {
        Optional<Result> resultOpt = resultService.getResultById(id);
        return resultOpt.map(result -> ResponseEntity.ok(resultService.formatResultValue(result)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 9. Update a specific result for a participant in a discipline
    @PutMapping("/participants/{participantId}/disciplines/{disciplineId}/results/{resultId}")
    public ResponseEntity<Result> updateResultForParticipant(
            @PathVariable Long participantId,
            @PathVariable Long disciplineId,
            @PathVariable Long resultId,
            @RequestBody Result updatedResult) {

        Result result = participantService.updateResultForParticipant(participantId, disciplineId, resultId, updatedResult);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    // 10. Delete a specific result for a participant
    @DeleteMapping("/participants/{participantId}/results/{resultId}")
    public ResponseEntity<Void> deleteResultFromParticipant(
            @PathVariable Long participantId,
            @PathVariable Long resultId) {
        try {
            resultService.deleteResultFromParticipant(participantId, resultId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }






    }
}
