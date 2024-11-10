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

    @Autowired
    public DisciplineService(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }

    // Create a new discipline
    public Discipline createDiscipline(Discipline discipline) {
        return disciplineRepository.save(discipline);
    }

    // Get all disciplines
    public List<Discipline> getAllDisciplines() {
        return disciplineRepository.findAll();
    }

    // Get a discipline by ID
    public Optional<Discipline> getDisciplineById(Long id) {
        return disciplineRepository.findById(id);
    }

    // Update an existing discipline
    public Discipline updateDiscipline(Long id, Discipline updatedDiscipline) {
        return disciplineRepository.findById(id)
                .map(discipline -> {
                    discipline.setName(updatedDiscipline.getName());
                    discipline.setResultType(updatedDiscipline.getResultType());
                    return disciplineRepository.save(discipline);
                })
                .orElseThrow(() -> new RuntimeException("Discipline not found with ID: " + id));
    }

    // Delete a discipline by ID
    public void deleteDiscipline(Long id) {
        disciplineRepository.deleteById(id);
    }

    // 6. Find discipliner baseret på resultattype (f.eks. tid, afstand eller point)
    public List<Discipline> findDisciplinesByResultType(ResultType resultType) {
        return disciplineRepository.findByResultType(resultType);
    }
}
