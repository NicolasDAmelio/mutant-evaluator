package com.ndamelio.mutant;

import com.ndamelio.mutant.config.MutantInvalidDNAException;
import com.ndamelio.mutant.controller.MutantController;
import com.ndamelio.mutant.dao.Stats;
import com.ndamelio.mutant.entity.Human;
import com.ndamelio.mutant.entity.Mutant;
import com.ndamelio.mutant.entity.MutantRepository;
import org.aspectj.bridge.MessageUtil;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@WebMvcTest(MutantController.class)
@TestPropertySource(properties = {
        "com.ndamelio.mutant.min-length-sequence-dna=4",
        "com.ndamelio.mutant.sequence-to-found=2"
})
public class MutantControllerTest {

    @Autowired
    private MutantController mutantController;

    @MockBean
    private MutantRepository mutantRepository;

    @Test(expected = MutantInvalidDNAException.class)
    public void whenSequenceHasBadLength() throws Exception {
        String[] dnaSequence = new String[]{"AGCAAG","GCTCTA","TCACTG"};
        mutantController.isMutant(dnaSequence);
    }

    @Test(expected = MutantInvalidDNAException.class)
    public void whenSequenceHasGoodLengthButOneStringHasBadLength() throws Exception {
        String[] dnaSequence = new String[]{"AGCCGA","AGGTGC","AGATGT","AGCAAG","GCTCTA","TCA"};
        mutantController.isMutant(dnaSequence);
    }

    @Test(expected = MutantInvalidDNAException.class)
    public void whenSequenceHasGoodLengthButHasABadLetter() throws Exception {
        String[] dnaSequence = new String[]{"AGXCGA","AGGTGC","AGATGT","AGCAAG","GCTCTA","TCACTG"};
        mutantController.isMutant(dnaSequence);
    }

    @Test
    public void whenSequenceHasGoodLengthAndGoodSequencesStrings() throws Exception {
        String[] dnaSequence = new String[]{"AGCCGA", "GCGTGC", "CGGGAT", "AGGGAG", "GGTCGA", "GCACTG"};
        Assertions.assertThat(mutantController.isMutant(dnaSequence)).isTrue();
    }

    @Test
    public void whenSequenceHasGoodLengthAndGoodSequencesStringsJustHorizontalCheck() throws Exception {
        String[] dnaSequence = new String[]{"AAAAGA", "GGGGTC", "CGGGAT", "AGGGAG", "GGTCGA", "GCACTG"};
        Assertions.assertThat(mutantController.isMutant(dnaSequence)).isTrue();
    }

    @Test
    public void whenSequenceHasGoodLengthAndGoodSequencesStringsJustVerticalCheck() throws Exception {
        String[] dnaSequence = new String[]{"AGAAGA", "AGGGTC", "AGGGAT", "AGGGAG", "GGTCGA", "GCACTG"};
        Assertions.assertThat(mutantController.isMutant(dnaSequence)).isTrue();
    }

    @Test
    public void whenSequenceHasGoodLengthAndGoodSequencesStringsJustDiagonaLeftTopCheck() throws Exception {
        String[] dnaSequence = new String[]{"AGCCGA", "GCGTGC", "CGGGAT", "ACGGAG", "GGCGGA", "GCACTG"};
        Assertions.assertThat(mutantController.isMutant(dnaSequence)).isTrue();
    }

    @Test
    public void whenSequenceHasGoodLengthAndGoodSequencesStringsJustDiagonaRightTopCheck() throws Exception {
        String[] dnaSequence = new String[]{"AGCCGA", "GCGTGC", "CGGTCT", "AGCCTG", "GCCTGA", "TATCTG"};
        Assertions.assertThat(mutantController.isMutant(dnaSequence)).isTrue();
    }

    @Test
    public void whenSequenceHasGoodLengthAndGoodSequencesStringsJustDiagonaLeftTopMainCheck() throws Exception {
        String[] dnaSequence = new String[]{"AGGGGA", "GAGTGC", "CGATCT", "AGCATG", "GCCTGA", "TATCTG"};
        Assertions.assertThat(mutantController.isMutant(dnaSequence)).isTrue();
    }

    @Test
    public void whenSequenceHasGoodLengthAndGoodSequencesStringsJustDiagonaRightTopMainCheck() throws Exception {
        String[] dnaSequence = new String[]{"AGCGGA", "GAGTGC", "CGTTCT", "AGCATG", "GCCTGA", "TATCTG"};
        Assertions.assertThat(mutantController.isMutant(dnaSequence)).isFalse();
    }

    @Test
    public void whenSequenceHasGoodLengthAndGoodSequencesStringsJustTopDiagonalsFromLeftCheckWithMainDiagonal() throws Exception {
        String[] dnaSequence = new String[]{"AGCGGA", "GAGTGC", "CGATCT", "AGCATG", "GCCTGA", "TATCTG"};
        Assertions.assertThat(mutantController.isMutant(dnaSequence)).isTrue();
    }

    @Test
    public void whenSequenceHasGoodLengthAndGoodSequencesStringsButExistsInBDD() throws Exception {
        String[] dnaSequence = new String[]{"AGCCGA","AGGTGC","AGATGT","AGCAAG","GCTCTA","TCACTG"};
        String dnaAsString = Arrays.deepToString(dnaSequence);

        Mutant mutant = new Mutant();
        mutant.setDna(dnaAsString);
        mutant.setHashDna(mutant.getDna().hashCode());
        mutant.setHumanType(Human.MUTANT);
        mutant.setMutantId(1);

        Mockito.when(mutantRepository.findByHashDna("[[A, G, C, C, G, A], [A, G, G, T, G, C], [A, G, A, T, G, T], [A, G, C, A, A, G], [G, C, T, C, T, A], [T, C, A, C, T, G]]".hashCode())).thenReturn(mutant);

        Assertions.assertThat(mutant.getMutantId()).isEqualTo(1);
        Assertions.assertThat(mutant.getHashDna()).isEqualTo(dnaAsString.hashCode());
        Assertions.assertThat(mutant.toString()).containsIgnoringCase(Human.MUTANT.name());
        Assertions.assertThat(mutantController.isMutant(dnaSequence)).isTrue();
    }

    @Test
    public void whenExecuteStatsMethod() {
        Mockito.when(mutantRepository.countByHumanType(Human.MUTANT)).thenReturn(BigDecimal.ONE);
        Mockito.when(mutantRepository.countAllBy()).thenReturn(BigDecimal.valueOf(5));
        Stats stats = new Stats();
        stats.setCountHumanDNA(5L);
        stats.setCountMutantDNA(1L);
        stats.setRatio(BigDecimal.valueOf(stats.getCountMutantDNA()).divide(BigDecimal.valueOf(stats.getCountHumanDNA()), 2, RoundingMode.HALF_UP));
        Assertions.assertThat(mutantController.stats().toString()).isEqualToIgnoringCase(stats.toString());
    }

}
