package atletik;

import atletik.rename_me.entity.Discipline;
import atletik.rename_me.entity.Participant;
import atletik.rename_me.entity.Result;
import atletik.rename_me.enums.AgeGroup;
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
import java.util.ArrayList;
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
            System.out.println("Disciplines initialized: " + disciplines);
        }
    }

    private void initParticipants() {
        if (participantRepository.count() == 0) {
            List<Discipline> disciplines = disciplineRepository.findAll();

            List<Participant> participants = List.of(
                    new Participant("John", "Doe", Gender.MALE, 25, "ABC Klub"),
                    new Participant("Jane", "Smith", Gender.FEMALE, 28, "XYZ Klub"),
                    new Participant("Emily", "Johnson", Gender.FEMALE, 23, "LMN Klub"),
                    new Participant("Michael", "Brown", Gender.MALE, 30, "DEF Klub"),
                    new Participant("Linda", "White", Gender.FEMALE, 22, "GHI Klub"),
                    new Participant("Chris", "Green", Gender.MALE, 27, "JKL Klub"),
                    new Participant("Sarah", "Black", Gender.FEMALE, 26, "MNO Klub"),
                    new Participant("James", "Blue", Gender.MALE, 29, "PQR Klub")
            );

            // Assign disciplines and age groups
            for (int i = 0; i < participants.size(); i++) {
                Participant participant = participants.get(i);
                participant.getDisciplines().add(disciplines.get(i));  // Assign unique discipline

                // Set age group based on age
                participant.setAgeGroup(determineAgeGroup(participant.getAge()));
            }

            participantRepository.saveAll(participants);  // Persist participants with disciplines and age groups
            System.out.println("Participants initialized with disciplines and age groups: " + participants);
        }
    }

    @Transactional
    public void initResults() {
        if (resultRepository.count() == 0) {
            List<Participant> participants = participantRepository.findAll();
            List<Discipline> disciplines = disciplineRepository.findAll();

            if (participants.size() >= 3 && disciplines.size() >= 3) {
                List<Result> results = new ArrayList<>();

                // Manually create results for specific participants and disciplines
                Result result1 = new Result(participants.get(0), disciplines.get(0), "12.34", LocalDate.now());
                Result result2 = new Result(participants.get(1), disciplines.get(1), "45.67", LocalDate.now());
                Result result3 = new Result(participants.get(2), disciplines.get(2), "89.10", LocalDate.now());

                // Associate results with participants and disciplines
                participants.get(0).getResults().add(result1);
                disciplines.get(0).getResults().add(result1);

                participants.get(1).getResults().add(result2);
                disciplines.get(1).getResults().add(result2);

                participants.get(2).getResults().add(result3);
                disciplines.get(2).getResults().add(result3);

                // Add results to the list
                results.add(result1);
                results.add(result2);
                results.add(result3);

                // Save results and update participants and disciplines
                resultRepository.saveAll(results);
                participantRepository.saveAll(participants);
                disciplineRepository.saveAll(disciplines);

                System.out.println("Fixed results initialized for specific participants and disciplines.");
            }
        }
    }

    private AgeGroup determineAgeGroup(int age) {
        if (age <= 24) {
            return AgeGroup.YOUTH;
        } else if (age <= 34) {
            return AgeGroup.ADULT;
        } else {
            return AgeGroup.SENIOR;
        }
    }
}

