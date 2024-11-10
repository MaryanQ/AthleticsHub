package atletik.rename_me.repository;

import atletik.rename_me.entity.Result;
import atletik.rename_me.enums.AgeGroup;
import atletik.rename_me.enums.Gender;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findByParticipantId(Long participantId);

}