package atletik.rename_me.api;

import atletik.rename_me.entity.Result;
import atletik.rename_me.enums.Gender;
import atletik.rename_me.enums.AgeGroup;
import atletik.rename_me.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    private final ResultService resultService;

    @Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    // 1. Hent alle resultater
    @GetMapping
    public ResponseEntity<List<Result>> getAllResults() {
        List<Result> results = resultService.getAllResults();
        return ResponseEntity.ok(results);
    }

    // 2. Hent et specifikt resultat baseret på ID
    @GetMapping("/{id}")
    public ResponseEntity<Result> getResultById(@PathVariable Long id) {
        Optional<Result> result = resultService.getResultById(id);
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Opret et nyt resultat for en deltager i en disciplin
    @PostMapping("/{participantId}/disciplines/{disciplineId}")
    public ResponseEntity<Result> createResult(
            @PathVariable Long participantId,
            @PathVariable Long disciplineId,
            @RequestBody Result result) {
        Result createdResult = resultService.createResult(participantId, disciplineId, result);
        if (createdResult != null) {
            return ResponseEntity.ok(createdResult);
        }
        return ResponseEntity.notFound().build();
    }

    // 4. Batch-oprettelse af resultater for en disciplin
    @PostMapping("/batch")
    public ResponseEntity<List<Result>> createResultsForDiscipline(@RequestBody List<Result> results) {
        List<Result> createdResults = resultService.createResultsForDiscipline(results);
        return ResponseEntity.ok(createdResults);
    }

    // 5. Opdater et eksisterende resultat
    @PutMapping("/{resultId}")
    public ResponseEntity<Result> updateResult(
            @PathVariable Long resultId,
            @RequestBody Result updatedResult) {
        Result result = resultService.updateResult(resultId, updatedResult);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    // 6. Slet et resultat baseret på ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
        resultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }

    // 7. Find og sorter resultater i en disciplin med filtrering efter køn og aldersgruppe
    @GetMapping("/discipline/{disciplineId}/filter")
    public ResponseEntity<List<Result>> findResultsByDisciplineWithSortingAndFilters(
            @PathVariable Long disciplineId,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) AgeGroup ageGroup) {
        List<Result> results = resultService.findResultsByDisciplineWithSortingAndFilters(disciplineId, gender, ageGroup);
        return ResponseEntity.ok(results);
    }

    // 8. Formater en specifik resultatværdi
    @GetMapping("/{id}/format")
    public ResponseEntity<String> formatResultValue(@PathVariable Long id) {
        Optional<Result> resultOpt = resultService.getResultById(id);
        if (resultOpt.isPresent()) {
            String formattedValue = resultService.formatResultValue(resultOpt.get());
            return ResponseEntity.ok(formattedValue);
        }
        return ResponseEntity.notFound().build();
    }
}
