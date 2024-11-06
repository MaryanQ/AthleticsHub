package atletik.rename_me.repository;

import atletik.rename_me.entity.Result;
import atletik.rename_me.enums.AgeGroup;
import atletik.rename_me.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    // Find og sorter resultater for en disciplin, filtreret efter køn og aldersgruppe
    @Query("SELECT r FROM Result r WHERE r.discipline.id = :disciplineId " +
            "AND (:gender IS NULL OR r.participant.gender = :gender) " +
            "AND (:ageGroup IS NULL OR r.participant.ageGroup = :ageGroup) " +
            "ORDER BY " +
            "CASE WHEN r.discipline.resultType = 'TIME' THEN r.resultValue END ASC, " +
            "CASE WHEN r.discipline.resultType = 'DISTANCE' THEN r.resultValue END DESC, " +
            "CASE WHEN r.discipline.resultType = 'POINTS' THEN r.resultValue END DESC")
    List<Result> findResultsByDisciplineWithSortingAndFilters(
            @Param("disciplineId") Long disciplineId,
            @Param("gender") Gender gender,
            @Param("ageGroup") AgeGroup ageGroup
    );
}