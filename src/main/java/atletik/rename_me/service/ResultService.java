package atletik.rename_me.service;

import atletik.rename_me.entity.Discipline;
import atletik.rename_me.entity.Participant;
import atletik.rename_me.entity.Result;
import atletik.rename_me.enums.Gender;
import atletik.rename_me.enums.AgeGroup;
import atletik.rename_me.enums.ResultType;
import atletik.rename_me.repository.DisciplineRepository;
import atletik.rename_me.repository.ParticipantRepository;
import atletik.rename_me.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ResultService {

    private final ResultRepository resultRepository;
    private final ParticipantRepository participantRepository;
    private final DisciplineRepository disciplineRepository;

    @Autowired
    public ResultService(ResultRepository resultRepository, ParticipantRepository participantRepository, DisciplineRepository disciplineRepository) {
        this.resultRepository = resultRepository;
        this.participantRepository = participantRepository;
        this.disciplineRepository = disciplineRepository;
    }

    // Create a new result
    public Result createResult(Result result, Long participantId, Long disciplineId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found with ID: " + participantId));
        Discipline discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new RuntimeException("Discipline not found with ID: " + disciplineId));

        result.setParticipant(participant);
        result.setDiscipline(discipline);
        return resultRepository.save(result);
    }

    // Get all results
    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }

    // Get a result by ID
    public Optional<Result> getResultById(Long id) {
        return resultRepository.findById(id);
    }

    // Update a result
    public Result updateResult(Long id, Result updatedResult) {
        return resultRepository.findById(id)
                .map(result -> {
                    result.setResultValue(updatedResult.getResultValue());
                    result.setDate(updatedResult.getDate());
                    return resultRepository.save(result);
                })
                .orElseThrow(() -> new RuntimeException("Result not found with ID: " + id));
    }

    // Delete a result by ID

    @Transactional
    public void deleteResult(Long resultId) {
        // Find all participants that reference this result
        List<Participant> participants = participantRepository.findAllByResultsId(resultId);

        // Remove the result reference from each participant
        for (Participant participant : participants) {
            participant.getResults().removeIf(result -> result.getId().equals(resultId));
            participantRepository.save(participant); // Save each participant after removing the result
        }

        // Permanently delete the result
        resultRepository.deleteById(resultId);
    }
}
