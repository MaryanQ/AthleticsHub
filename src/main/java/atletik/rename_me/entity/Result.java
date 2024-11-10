package atletik.rename_me.entity;

import atletik.rename_me.enums.ResultType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "discipline_id", nullable = false)
    @JsonBackReference(value = "discipline-results")
    private Discipline discipline;

    public Result(Participant participant, Discipline discipline, String resultValue, LocalDate date) {
        this.participant = participant;
        this.discipline = discipline;
        this.resultValue = resultValue;
        this.date = date;
    }

    // Tilføj formattedResult til JSON-output
    @JsonProperty("formattedResult")
    public String getFormattedResult() {
        if (discipline.getResultType() == ResultType.TIME) {
            return formatTimeResult();
        } else if (discipline.getResultType() == ResultType.DISTANCE) {
            return formatDistanceResult();
        }
        return resultValue;
    }

    private boolean deleted = false;

    // Formatér tidsresultat som timer, minutter, sekunder og hundrededele sekunder
    private String formatTimeResult() {
        try {
            Duration duration = Duration.parse("PT" + resultValue);  // "PT" er ISO-8601 format for varighed
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            long seconds = duration.toSecondsPart();
            long milliseconds = duration.toMillisPart() / 10;  // Hundrededele sekunder

            return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, milliseconds);
        } catch (Exception e) {
            return resultValue;  // Returnér rå værdi, hvis formatet er forkert
        }
    }


    // Formatér afstandsresultat som meter og centimeter
    private String formatDistanceResult() {
        try {
            double meters = Double.parseDouble(resultValue);
            int wholeMeters = (int) meters;
            int centimeters = (int) Math.round((meters - wholeMeters) * 100);

            return String.format("%d m %d cm", wholeMeters, centimeters);
        } catch (NumberFormatException e) {
            return resultValue;  // Return raw value if format is incorrect
        }
    }
}
