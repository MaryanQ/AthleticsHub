package atletik.rename_me.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter

@AllArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String resultValue;

    @ManyToOne
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;


    public Result(){}

    public Result(Participant participant, Discipline discipline, String resultValue, LocalDate date) {
        this.participant = participant;
        this.discipline = discipline;
        this.resultValue = resultValue;
        this.date = date;
    }

}