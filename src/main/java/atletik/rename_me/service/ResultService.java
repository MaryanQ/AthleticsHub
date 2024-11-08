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

    // 1. Hent alle resultater
    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }

    // 2. Hent et specifikt resultat baseret på ID
    public Optional<Result> getResultById(Long id) {
        return resultRepository.findById(id);
    }

    // 3. Opret et nyt resultat
    public Result createResult(Long participantId, Long disciplineId, Result result) {
        Optional<Participant> participantOpt = participantRepository.findById(participantId);
        Optional<Discipline> disciplineOpt = disciplineRepository.findById(disciplineId);

        if (participantOpt.isPresent() && disciplineOpt.isPresent()) {
            result.setParticipant(participantOpt.get());
            result.setDiscipline(disciplineOpt.get());
            return resultRepository.save(result);
        }
        return null;
    }

    // 4. Batch-oprettelse af resultater for en disciplin
    public List<Result> createResultsForDiscipline(List<Result> results) {
        return resultRepository.saveAll(results);
    }

    // 5. Opdater et eksisterende resultat
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

    // 6. Slet et resultat baseret på ID
    public void deleteResult(Long id) {
        resultRepository.deleteById(id);
    }

    // 7. Find og sorter resultater i en disciplin med filtrering efter køn og aldersgruppe
    public List<Result> findResultsByDisciplineWithSortingAndFilters(Long disciplineId, Gender gender, AgeGroup ageGroup) {
        return resultRepository.findResultsByDisciplineWithSortingAndFilters(disciplineId, gender, ageGroup);
    }

    // 8. Formater resultatværdien afhængigt af resultat-type
    public String formatResultValue(Result result) {
        ResultType resultType = result.getDiscipline().getResultType();

        if (resultType == ResultType.TIME) {
            return formatTime(result.getResultValue());
        } else if (resultType == ResultType.DISTANCE) {
            return formatDistance(result.getResultValue());
        }

        // Point vises uden ændring
        return result.getResultValue();
    }

    private String formatTime(String timeValue) {
        try {
            double totalSeconds = Double.parseDouble(timeValue);

            int hours = (int) totalSeconds / 3600;
            int minutes = ((int) totalSeconds % 3600) / 60;
            int seconds = (int) totalSeconds % 60;
            int hundredths = (int) ((totalSeconds - (int) totalSeconds) * 100);

            return String.format("%02d:%02d:%02d:%02d", hours, minutes, seconds, hundredths);
        } catch (NumberFormatException e) {
            // Return original value if input cannot be parsed to double
            return timeValue;
        }
    }

    private String formatDistance(String distanceValue) {
        try {
            double totalMeters = Double.parseDouble(distanceValue);

            int meters = (int) totalMeters;
            int centimeters = (int) ((totalMeters - meters) * 100);

            return String.format("%d.%02d m", meters, centimeters);
        } catch (NumberFormatException e) {
            // Return original value if input cannot be parsed to double
            return distanceValue;
        }
    }

    // Method to delete a specific result for a participant
    public void deleteResultFromParticipant(Long participantId, Long resultId) {
        // Find the participant based on participantId
        Optional<Participant> participantOpt = participantRepository.findById(participantId);
        if (!participantOpt.isPresent()) {
            throw new RuntimeException("Participant not found");
        }

        // Find the result based on resultId
        Optional<Result> resultOpt = resultRepository.findById(resultId);
        if (!resultOpt.isPresent()) {
            throw new RuntimeException("Result not found");
        }

        Participant participant = participantOpt.get();
        Result result = resultOpt.get();

        // Check if the result is associated with the participant
        if (!participant.getResults().contains(result)) {
            throw new RuntimeException("Result not associated with participant");
        }

        // Remove the result from the participant's list of results
        participant.getResults().remove(result);

        // Save the participant to update the state in the database
        participantRepository.save(participant);

        // Finally, delete the result from the database
        resultRepository.delete(result);
    }

    public Result addResultToDiscipline(Long participantId, Long disciplineId, Result result) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found"));

        Discipline discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new RuntimeException("Discipline not found"));

        result.setParticipant(participant); // Sætter deltageren
        result.setDiscipline(discipline);   // Sætter disciplinen

        return resultRepository.save(result);
    }

    public Result updateResultForParticipant(Long participantId, Long disciplineId, Long resultId, Result updatedResult) {
        Result existingResult = resultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("Result not found"));

        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found"));

        Discipline discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new RuntimeException("Discipline not found"));

        existingResult.setDate(updatedResult.getDate());
        existingResult.setResultValue(updatedResult.getResultValue());
        existingResult.setDiscipline(discipline);   // Opdaterer disciplinen
        existingResult.setParticipant(participant); // Opdaterer deltageren

        return resultRepository.save(existingResult);
    }



}