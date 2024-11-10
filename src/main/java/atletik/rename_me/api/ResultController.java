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

    @Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    // Create a new result
    @PostMapping
    public Result createResult(@RequestBody Result result,
                               @RequestParam Long participantId,
                               @RequestParam Long disciplineId) {
        return resultService.createResult(result, participantId, disciplineId);
    }

    // Get all results
    @GetMapping
    public List<Result> getAllResults() {
        return resultService.getAllResults();
    }

    // Get a result by ID
    @GetMapping("/{id}")
    public ResponseEntity<Result> getResultById(@PathVariable Long id) {
        return resultService.getResultById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update a result
    @PutMapping("/{id}")
    public ResponseEntity<Result> updateResult(@PathVariable Long id, @RequestBody Result updatedResult) {
        return ResponseEntity.ok(resultService.updateResult(id, updatedResult));
    }

    // Delete a result
    //@DeleteMapping("/{id}")
    //public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
      //  resultService.deleteResult(id);
        //return ResponseEntity.noContent().build();
    //}


    @DeleteMapping("/{resultId}")
    public ResponseEntity<String> deleteResult(@PathVariable Long resultId) {
        try {
            resultService.deleteResult(resultId);
            return ResponseEntity.ok("Result deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting result: " + e.getMessage());
        }
    }
}
