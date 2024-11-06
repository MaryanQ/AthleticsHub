package atletik.rename_me.repository;

import atletik.rename_me.entity.Participant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    // Find participants by name (case-insensitive)
    List<Participant> findByFirstNameContainingIgnoreCase(String firstname);

    // Find participants by discipline
    @Query("SELECT p FROM Participant p JOIN p.disciplines d WHERE d.name = :disciplineName")
    List<Participant> findByDisciplineName(@Param("disciplineName") String disciplineName);

    // Filter participants by gender, age group, and club
    @Query("SELECT p FROM Participant p WHERE " +
            "(?1 IS NULL OR p.gender = ?1) AND " +
            "(?2 IS NULL OR p.ageGroup = ?2) AND " +
            "(?3 IS NULL OR p.club = ?3)")
    List<Participant> filterParticipants(String gender, String ageGroup, String club);
}


