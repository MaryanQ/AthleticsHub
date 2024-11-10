package atletik.rename_me.entity;

import atletik.rename_me.enums.ResultType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ResultType resultType;

    @OneToMany(mappedBy = "discipline", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference(value = "discipline-results")


    @JsonIgnore
    private List<Result> results = new ArrayList<>();

    public Discipline(String name, ResultType resultType) {
        this.name = name;
        this.resultType = resultType;
    }


}
