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
                    new Discipline("100m Løb", ResultType.TIME),          // Løbedisciplin med tid som resultat
                    new Discipline("Maraton", ResultType.TIME),           // Langdistanceløb med tid som resultat
                    new Discipline("Højdespring", ResultType.DISTANCE),   // Springdisciplin med afstand som resultat
                    new Discipline("Længdespring", ResultType.DISTANCE),  // Springdisciplin med afstand som resultat
                    new Discipline("Diskoskast", ResultType.DISTANCE)     // Kastdisciplin med afstand som resultat
            );
            disciplineRepository.saveAll(disciplines);
            System.out.println("Disciplines initialized: " + disciplines);
        }
    }


    @Transactional
    public void initParticipants() {
        if (participantRepository.count() == 0) {
            // Gem alle Disciplines først
            List<Discipline> disciplines = disciplineRepository.findAll();

            List<Participant> participants = List.of(
                    new Participant("John", "Doe", Gender.MALE, 8, "Lynhurtig IF"),
                    new Participant("Alice", "Miller", Gender.FEMALE, 10, "Lynhurtig IF"),
                    new Participant("Tom", "Wilson", Gender.MALE, 14, "Lynhurtig IF"),

                    new Participant("Jane", "Smith", Gender.FEMALE, 55, "Storm Klub"),
                    new Participant("Oscar", "Anderson", Gender.MALE, 19, "Storm Klub"),
                    new Participant("Lily", "Taylor", Gender.FEMALE, 32, "Storm Klub"),

                    new Participant("Emily", "Johnson", Gender.FEMALE, 23, "Vikingerne Atletik"),
                    new Participant("Chris", "Green", Gender.MALE, 28, "Vikingerne Atletik"),
                    new Participant("Sarah", "Black", Gender.FEMALE, 11, "Vikingerne Atletik"),

                    new Participant("Michael", "Brown", Gender.MALE, 30, "Nordstjernen IF"),
                    new Participant("James", "Blue", Gender.MALE, 29, "Nordstjernen IF"),
                    new Participant("Linda", "White", Gender.FEMALE, 17, "Nordstjernen IF"),

                    new Participant("Olivia", "Thompson", Gender.FEMALE, 7, "Falke IF"),
                    new Participant("Ethan", "White", Gender.MALE, 22, "Falke IF"),
                    new Participant("Sophia", "Lewis", Gender.FEMALE, 16, "Falke IF")
            );


            // Gem deltagerne uden at tilknytte discipliner
            participantRepository.saveAll(participants);

            // Tildel disciplinerne efterfølgende
            for (int i = 0; i < participants.size(); i++) {
                Participant participant = participants.get(i);
                participant.getDisciplines().add(disciplines.get(i % disciplines.size()));  // Cykl gennem discipliner
                participant.setAgeGroup(determineAgeGroup(participant.getAge()));
            }

            // Gem opdaterede deltagere med discipliner
            participantRepository.saveAll(participants);

            System.out.println("Participants initialized with disciplines and age groups: " + participants);
        }
    }


    @Transactional
    public void initResults() {
        if (resultRepository.count() == 0) {
            List<Participant> participants = participantRepository.findAll();
            List<Discipline> disciplines = disciplineRepository.findAll();
            List<Result> results = new ArrayList<>();

            if (participants.size() >= 15 && disciplines.size() >= 5) {
                // Tildel individuelle resultater til deltagere uden brug af en løkke
                results.add(new Result(participants.get(0), disciplines.get(0), "00:12:34.56", LocalDate.now())); // 100m Løb (TIME)
                results.add(new Result(participants.get(1), disciplines.get(1), "02:45:30.45", LocalDate.now())); // Maraton (TIME)
                results.add(new Result(participants.get(2), disciplines.get(2), "1.90", LocalDate.now())); // Højdespring (DISTANCE)
                results.add(new Result(participants.get(3), disciplines.get(3), "6.20", LocalDate.now())); // Længdespring (DISTANCE)
                results.add(new Result(participants.get(4), disciplines.get(4), "65.78", LocalDate.now())); // Diskoskast (DISTANCE)

                results.add(new Result(participants.get(5), disciplines.get(0), "00:13:22.10", LocalDate.now())); // 100m Løb (TIME)
                results.add(new Result(participants.get(6), disciplines.get(1), "02:50:15.20", LocalDate.now())); // Maraton (TIME)
                results.add(new Result(participants.get(7), disciplines.get(2), "2.05", LocalDate.now())); // Højdespring (DISTANCE)
                results.add(new Result(participants.get(8), disciplines.get(3), "5.95", LocalDate.now())); // Længdespring (DISTANCE)
                results.add(new Result(participants.get(9), disciplines.get(4), "60.45", LocalDate.now())); // Diskoskast (DISTANCE)

                results.add(new Result(participants.get(10), disciplines.get(0), "00:11:50.33", LocalDate.now())); // 100m Løb (TIME)
                results.add(new Result(participants.get(11), disciplines.get(1), "02:40:40.89", LocalDate.now())); // Maraton (TIME)
                results.add(new Result(participants.get(12), disciplines.get(2), "2.10", LocalDate.now())); // Højdespring (DISTANCE)
                results.add(new Result(participants.get(13), disciplines.get(3), "6.10", LocalDate.now())); // Længdespring (DISTANCE)
                results.add(new Result(participants.get(14), disciplines.get(4), "58.90", LocalDate.now())); // Diskoskast (DISTANCE)

                // Gem resultater i databasen
                resultRepository.saveAll(results);

                // Opdater deltagere med deres respektive resultater
                participants.get(0).getResults().add(results.get(0));
                participants.get(1).getResults().add(results.get(1));
                participants.get(2).getResults().add(results.get(2));
                participants.get(3).getResults().add(results.get(3));
                participants.get(4).getResults().add(results.get(4));

                participants.get(5).getResults().add(results.get(5));
                participants.get(6).getResults().add(results.get(6));
                participants.get(7).getResults().add(results.get(7));
                participants.get(8).getResults().add(results.get(8));
                participants.get(9).getResults().add(results.get(9));

                participants.get(10).getResults().add(results.get(10));
                participants.get(11).getResults().add(results.get(11));
                participants.get(12).getResults().add(results.get(12));
                participants.get(13).getResults().add(results.get(13));
                participants.get(14).getResults().add(results.get(14));

                // Opdater discipliner med deres respektive resultater
                disciplines.get(0).getResults().addAll(List.of(results.get(0), results.get(5), results.get(10)));
                disciplines.get(1).getResults().addAll(List.of(results.get(1), results.get(6), results.get(11)));
                disciplines.get(2).getResults().addAll(List.of(results.get(2), results.get(7), results.get(12)));
                disciplines.get(3).getResults().addAll(List.of(results.get(3), results.get(8), results.get(13)));
                disciplines.get(4).getResults().addAll(List.of(results.get(4), results.get(9), results.get(14)));

                // Gem opdaterede deltagere og discipliner
                participantRepository.saveAll(participants);
                disciplineRepository.saveAll(disciplines);

                System.out.println("Results initialized and associated with all participants and disciplines.");
            }
        }
    }




    public AgeGroup determineAgeGroup(int age) {
        if (age >= 6 && age <= 9) {
            return AgeGroup.CHILD;
        } else if (age >= 10 && age <= 13) {
            return AgeGroup.YOUTH;
        } else if (age >= 14 && age <= 22) {
            return AgeGroup.JUNIOR;
        } else if (age >= 23 && age <= 40) {
            return AgeGroup.ADULT;
        } else if (age >= 41) {
            return AgeGroup.SENIOR;
        }
        return null; // Ingen matchende aldersgruppe
    }
}
