package atletik.rename_me.service;

import atletik.rename_me.entity.Discipline;
import atletik.rename_me.enums.ResultType;
import atletik.rename_me.repository.DisciplineRepository;
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
    public void deleteDiscipline(Long id) {
        disciplineRepository.deleteById(id);
    }

    // 6. Find discipliner baseret på resultattype (f.eks. tid, afstand eller point)
    public List<Discipline> findDisciplinesByResultType(ResultType resultType) {
        return disciplineRepository.findByResultType(resultType);
    }
}
