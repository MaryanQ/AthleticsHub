package atletik.rename_me.entity;

import atletik.rename_me.enums.AgeGroup;
import atletik.rename_me.enums.Gender;


import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor

public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private int age;
    private String club;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Result> results = new ArrayList<>();


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "participant_discipline",
            joinColumns = @JoinColumn(name = "participant_id"),
            inverseJoinColumns = @JoinColumn(name = "discipline_id")
    )
    private List<Discipline> disciplines = new ArrayList<>();

    // Default konstruktør (nødvendig for JPA)
    public Participant() {
    }

    // Konstruktør med fornavn, efternavn, køn, alder og klub
    public Participant(String firstName, String lastName, Gender gender, int age, String club) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.club = club;
    }
}
