package atletik.rename_me.api;

import atletik.rename_me.entity.Discipline;
import atletik.rename_me.enums.ResultType;
import atletik.rename_me.service.DisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/disciplines")
public class DisciplineController {

    private final DisciplineService disciplineService;

    @Autowired
    public DisciplineController(DisciplineService disciplineService) {
        this.disciplineService = disciplineService;
    }

    // 1. Hent alle discipliner
    @GetMapping
    public ResponseEntity<List<Discipline>> getAllDisciplines() {
        List<Discipline> disciplines = disciplineService.getAllDisciplines();
        return ResponseEntity.ok(disciplines);
    }

    // 2. Hent en specifik disciplin baseret på ID
    @GetMapping("/{id}")
    public ResponseEntity<Discipline> getDisciplineById(@PathVariable Long id) {
        Optional<Discipline> discipline = disciplineService.getDisciplineById(id);
        return discipline.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Opret en ny disciplin
    @PostMapping
    public ResponseEntity<Discipline> createDiscipline(@RequestBody Discipline discipline) {
        Discipline createdDiscipline = disciplineService.createDiscipline(discipline);
        return ResponseEntity.ok(createdDiscipline);
    }

    // 4. Opdater en eksisterende disciplin
    @PutMapping("/{id}")
    public ResponseEntity<Discipline> updateDiscipline(
            @PathVariable Long id,
            @RequestBody Discipline updatedDiscipline) {
        Discipline discipline = disciplineService.updateDiscipline(id, updatedDiscipline);
        if (discipline != null) {
            return ResponseEntity.ok(discipline);
        }
        return ResponseEntity.notFound().build();
    }

    // 5. Slet en disciplin baseret på ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscipline(@PathVariable Long id) {
        disciplineService.deleteDiscipline(id);
        return ResponseEntity.noContent().build();
    }

    // 6. Find discipliner baseret på resultattype (f.eks. tid, afstand eller point)
    @GetMapping("/filter")
    public ResponseEntity<List<Discipline>> findDisciplinesByResultType(@RequestParam ResultType resultType) {
        List<Discipline> disciplines = disciplineService.findDisciplinesByResultType(resultType);
        return ResponseEntity.ok(disciplines);
    }
}
