package atletik.rename_me.repository;

import atletik.rename_me.entity.Discipline;
import atletik.rename_me.enums.ResultType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
    // Find discipliner baseret på resultattype (f.eks. tid, afstand eller point)
    List<Discipline> findByResultType(ResultType resultType);
}
