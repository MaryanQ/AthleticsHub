package atletik;

import atletik.rename_me.entity.Discipline;
import atletik.rename_me.entity.Participant;
import atletik.rename_me.entity.Result;
import atletik.rename_me.enums.Gender;
import atletik.rename_me.enums.ResultType;
import atletik.rename_me.repository.DisciplineRepository;
import atletik.rename_me.repository.ParticipantRepository;
import atletik.rename_me.repository.ResultRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer {

    private final DisciplineRepository disciplineRepository;
    private final ParticipantRepository participantRepository;
    private final ResultRepository resultRepository;

    @Autowired
    public DataInitializer(DisciplineRepository disciplineRepository, ParticipantRepository participantRepository, ResultRepository resultRepository) {
        this.disciplineRepository = disciplineRepository;
        this.participantRepository = participantRepository;
        this.resultRepository = resultRepository;
    }

    @PostConstruct
    @Transactional
    public void initData() {
        initDisciplines();
        initParticipants();
        initResults();
    }

    private void initDisciplines() {
        if (disciplineRepository.count() == 0) {
            List<Discipline> disciplines = List.of(
                    new Discipline("100m Løb", ResultType.TIME),
                    new Discipline("200m Løb", ResultType.TIME),
                    new Discipline("400m Løb", ResultType.TIME),
                    new Discipline("Højdespring", ResultType.DISTANCE),
                    new Discipline("Længdespring", ResultType.DISTANCE),
                    new Discipline("Spydkast", ResultType.DISTANCE),
                    new Discipline("10-Kamp", ResultType.POINTS),
                    new Discipline("5-Kamp", ResultType.POINTS)
            );
            disciplineRepository.saveAll(disciplines);
        }
    }

    private void initParticipants() {
        if (participantRepository.count() == 0) {
            List<Participant> participants = List.of(
                    new Participant("John", "Doe", Gender.MALE, 25, "ABC Klub"),
                    new Participant("Jane", "Smith", Gender.FEMALE, 28, "XYZ Klub"),
                    new Participant("Emily", "Johnson", Gender.FEMALE, 23, "LMN Klub"),
                    new Participant("Michael", "Brown", Gender.MALE, 30, "DEF Klub")
            );
            participantRepository.saveAll(participants);
        }
    }



    @Transactional
    public void initResults() {
        if (resultRepository.count() == 0) {
            List<Discipline> disciplines = disciplineRepository.findAll();
            List<Participant> participants = participantRepository.findAll();

            if (!disciplines.isEmpty() && !participants.isEmpty()) {
                Result result1 = new Result(participants.get(0), disciplines.get(0), "12.34", LocalDate.now());
                Result result2 = new Result(participants.get(1), disciplines.get(1), "24.56", LocalDate.now());
                Result result3 = new Result(participants.get(2), disciplines.get(3), "1.75", LocalDate.now());

                participants.get(0).getResults().add(result1);
                disciplines.get(0).getResults().add(result1);

                participants.get(1).getResults().add(result2);
                disciplines.get(1).getResults().add(result2);

                participants.get(2).getResults().add(result3);
                disciplines.get(3).getResults().add(result3);

                // Gem resultaterne
                resultRepository.saveAll(List.of(result1, result2, result3));
            }
        }
    }

}