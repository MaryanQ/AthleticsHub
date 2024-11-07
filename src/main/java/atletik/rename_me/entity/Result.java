package atletik.rename_me.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@NoArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String resultValue;

    @ManyToOne
    @JoinColumn(name = "participant_id", nullable = false)
    @JsonBackReference(value = "participant-results")
    private Participant participant;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    @JsonBackReference(value = "discipline-results")

    private Discipline discipline;

    public Result(Participant participant, Discipline discipline, String resultValue, LocalDate date) {
        this.participant = participant;
        this.discipline = discipline;
        this.resultValue = resultValue;
        this.date = date;
    }
}
