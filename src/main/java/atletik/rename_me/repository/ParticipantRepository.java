package atletik.rename_me.repository;

import atletik.rename_me.entity.Participant;

import atletik.rename_me.enums.AgeGroup;
import atletik.rename_me.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    // Find participants by name (case-insensitive)
    List<Participant> findByFirstNameContainingIgnoreCase(String name);

    // Søger deltagere ud fra en specifik disciplin
    @Query("SELECT p FROM Participant p JOIN p.disciplines d WHERE "
            + "(:gender IS NULL OR p.gender = :gender) AND "
            + "(:ageGroup IS NULL OR p.ageGroup = :ageGroup) AND "
            + "(:club IS NULL OR p.club = :club) AND "
            + "(:discipline IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :discipline, '%')))")
    List<Participant> filterParticipants(@Param("gender") Gender gender,
                                         @Param("ageGroup") AgeGroup ageGroup,
                                         @Param("club") String club,
                                         @Param("discipline") String discipline);

    // Finds participants that reference the specified result
    @Query("SELECT p FROM Participant p JOIN p.results r WHERE r.id = :resultId")
    List<Participant> findAllByResultsId(@Param("resultId") Long resultId);

}
