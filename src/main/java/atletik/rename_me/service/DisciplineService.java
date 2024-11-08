package atletik.rename_me.service;

import atletik.rename_me.entity.Discipline;
import atletik.rename_me.entity.Participant;
import atletik.rename_me.enums.ResultType;
import atletik.rename_me.repository.DisciplineRepository;
import atletik.rename_me.repository.ParticipantRepository;
import atletik.rename_me.repository.ResultRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;
private final ParticipantRepository participantRepository;
private final ResultRepository resultRepository;

    @Autowired
    public DisciplineService(DisciplineRepository disciplineRepository, ParticipantRepository participantRepository, ResultRepository resultRepository) {
        this.disciplineRepository = disciplineRepository;
this.participantRepository = participantRepository;
this.resultRepository = resultRepository;
    }

    // 1. Hent alle discipliner
    public List<Discipline> getAllDisciplines() {
        return disciplineRepository.findAll();
    }

    // 2. Hent en specifik disciplin baseret på ID
    public Optional<Discipline> getDisciplineById(Long id) {
        return disciplineRepository.findById(id);
    }

    // 3. Opret en ny disciplin
    public Discipline createDiscipline(Discipline discipline) {
        return disciplineRepository.save(discipline);
    }

    // 4. Opdater en eksisterende disciplin
    public Discipline updateDiscipline(Long id, Discipline updatedDiscipline) {
        Optional<Discipline> optionalDiscipline = disciplineRepository.findById(id);

        if (optionalDiscipline.isPresent()) {
            Discipline existingDiscipline = optionalDiscipline.get();
            existingDiscipline.setName(updatedDiscipline.getName());
            existingDiscipline.setResultType(updatedDiscipline.getResultType());
            return disciplineRepository.save(existingDiscipline);
        }
        return null;
    }

    // 5. Slet en disciplin baseret på ID
    public void deleteDiscipline(Long disciplineId) {
        Discipline discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new EntityNotFoundException("Discipline not found"));

        // Fjern alle resultater relateret til denne disciplin
        resultRepository.deleteAll(discipline.getResults());

        // Slet derefter disciplinen
        disciplineRepository.delete(discipline);
    }

    // Fjern en disciplin fra en deltager
    public void deleteDisciplineFromParticipant(Long participantId, Long disciplineId) {
        // Find deltageren baseret på participantId
        Optional<Participant> participantOpt = participantRepository.findById(participantId);
        if (!participantOpt.isPresent()) {
            throw new RuntimeException("Participant not found");
        }

        // Find disciplinen baseret på disciplineId
        Optional<Discipline> disciplineOpt = disciplineRepository.findById(disciplineId);
        if (!disciplineOpt.isPresent()) {
            throw new RuntimeException("Discipline not found");
        }

        Participant participant = participantOpt.get();
        Discipline discipline = disciplineOpt.get();

        // Fjern disciplinen fra deltagerens liste over discipliner
        boolean removed = participant.getDisciplines().remove(discipline);
        if (!removed) {
            throw new RuntimeException("Discipline not associated with participant");
        }

        // Gem deltageren for at opdatere tilstanden i databasen
        participantRepository.save(participant);
    }
    // 6. Find discipliner baseret på resultattype (f.eks. tid, afstand eller point)
    public List<Discipline> findDisciplinesByResultType(ResultType resultType) {
        return disciplineRepository.findByResultType(resultType);
    }
}
