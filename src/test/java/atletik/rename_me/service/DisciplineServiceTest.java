package atletik.rename_me.service;

import atletik.rename_me.entity.Discipline;
import atletik.rename_me.enums.ResultType;
import atletik.rename_me.repository.DisciplineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DisciplineServiceTest {

    @Mock
    private DisciplineRepository disciplineRepository;




    @Test
    void getAllDisciplines() {
        // Arrange. i dette trin forbereder vi alt, hvad vi skal bruge til testen.
//vi mocker disciplinerepositpry til at rturnere en liste
        when(disciplineRepository.findAll()).thenReturn(List.of(
                new Discipline("100m Løb", ResultType.TIME),
                new Discipline("200m Løb", ResultType.TIME),
                new Discipline("400m Løb", ResultType.TIME),
                new Discipline("Højdespring", ResultType.DISTANCE),
                new Discipline("Længdespring", ResultType.DISTANCE),
                new Discipline("Spydkast", ResultType.DISTANCE),
                new Discipline("10-Kamp", ResultType.POINTS),
                new Discipline("5-Kamp", ResultType.POINTS)
        ));

        // Act - udførelse
        //vi kalder den metode vi tester, og gemmer resultatet.
        List<Discipline> disciplines = disciplineRepository.findAll();

        // Assert - Bekræftelse
        // vi tjekker om metoden returnere det forventede resultat.

        assertNotNull(disciplines); // bekræft at resultatet ikke er null
        assertEquals(8, disciplines.size()); // bekræft at der er 8 discipliner i listen
        assertEquals("100m Løb", disciplines.get(0).getName()); // bekræft at det første disciplin i listen er 100m løb

// Vi tjekker også, at findAll() blev kaldt én gang på repositoryet        ve
verify(disciplineRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getDisciplineById() {

        //arrange
        Discipline discipline = new Discipline("100m Løb", ResultType.TIME);
        when(disciplineRepository.findById(1L)).thenReturn(java.util.Optional.of(discipline));
    }

    @Test
    void createDiscipline() {
    }

    @Test
    void updateDiscipline() {
    }

    @Test
    void deleteDiscipline() {
    }

    @Test
    void findDisciplinesByResultType() {
    }
}