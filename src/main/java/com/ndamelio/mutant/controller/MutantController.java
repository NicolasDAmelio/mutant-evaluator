package com.ndamelio.mutant.controller;

import com.ndamelio.mutant.dao.Stats;
import com.ndamelio.mutant.entity.Mutant;
import com.ndamelio.mutant.entity.Human;
import com.ndamelio.mutant.config.MutantInvalidDNAException;
import com.ndamelio.mutant.entity.MutantRepository;
import com.ndamelio.mutant.helper.DNAEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.regex.Pattern;

@Component
public class MutantController {

    @Value("${com.ndamelio.mutant.min-length-sequence-dna}")
    private int minLengthSequenceDna;

    @Value("${com.ndamelio.mutant.sequence-to-found}")
    private int sequenceToFound;

    private final static Logger LOG = LoggerFactory.getLogger(MutantController.class);

    private static final Pattern DNA_BASE_PATTERN = Pattern.compile("[atcg]+", Pattern.CASE_INSENSITIVE);

    private final MutantRepository mutantRepository;

    public MutantController(MutantRepository mutantRepository) {
        this.mutantRepository = mutantRepository;
    }


    /**
     * Este metodo es la primera entrada para evaluar si un ADN es correcto y es mutante o no !
     *
     * @param dnaSequence Array de String para trabajar
     * @return boolean, que determina si es o no Mutante
     * @throws MutantInvalidDNAException
     */
    public boolean isMutant(String[] dnaSequence) throws MutantInvalidDNAException {
        if (dnaSequence.length < minLengthSequenceDna) {
            LOG.info("Longitud minima debe ser mayor al minimo de secuencia del ADN que es {}, para poder ser considerado un Mutante",  minLengthSequenceDna);
            throw new MutantInvalidDNAException(String.format("Longitud minima debe ser mayor al minimo de secuencia del ADN que es %s, para poder ser considerado un Mutante", minLengthSequenceDna));
        }

        char[][] dna = this.checkAndFormatDNA(dnaSequence);
        String dnaAsString = Arrays.deepToString(dna);
        Mutant mutant = mutantRepository.findByHashDna(dnaAsString.hashCode());

        if (mutant != null) {
            LOG.info("Nothing to do!");
            return mutant.getHumanType().equals(Human.MUTANT);
        } else {
            boolean result = analyzeDNA(dna);
            mutant = new Mutant(dna, result);
            mutantRepository.save(mutant);
            return result;
        }
    }

    /**
     * Este metodo analiza el verifica que sea un ADN de sintaxis / estructura correcta, y lo devuelve como 2d char array
     *
     * @param dnaSequence Array de String para trabajar
     * @return char[][], secuencia de adn en formato 2dArray char!
     * @throws MutantInvalidDNAException
     */
    private char[][] checkAndFormatDNA(String[] dnaSequence) throws MutantInvalidDNAException {
        int length = dnaSequence.length;
        char[][] dna = new char[length][length];

        for (int i = 0; i < length; i++) {
            String dnaRow = dnaSequence[i];
            if (dnaRow.length() != length) {
                LOG.info("Longitud de la fila del ADN debe ser igual a la cantidad de filas (NxN). Expected: {} -- Found: {}.", length, dnaRow.length());
                throw new MutantInvalidDNAException("Longitud de la fila " + i + " debe ser igual a cantidad de filas. Debe ser NxN");
            } else if (!DNA_BASE_PATTERN.matcher(dnaRow).matches()) {
                LOG.info("Los unicos caracteres permitidos en el ADN son [A, T, C, G]. Found {}", dnaRow);
                throw new MutantInvalidDNAException("Los unicos caracteres permitidos en el ADN son [A, T, C, G]. Found " + dnaRow);
            }
            dna[i] = dnaRow.toUpperCase().toCharArray();
        }

        return dna;
    }

    /**
     * Este metodo analiza el ADN con las distintas alternativas a encontrar secuencias mutantes
     *
     * @param dnaSequence 2dArray preparado para iterar
     * @return boolean, que determina si es o no Mutante
     */
    private boolean analyzeDNA(char[][] dnaSequence) {
        DNAEvaluator dnaEvaluator = new DNAEvaluator(sequenceToFound, minLengthSequenceDna);

        if (dnaEvaluator.horizontalCheck(dnaSequence)) return true;
        if (dnaEvaluator.verticalCheck(dnaSequence)) return true;
        if (dnaEvaluator.bottomDiagonalsFromLeftCheckWithoutMainDiagonal(dnaSequence)) return true;
        if (dnaEvaluator.bottomDiagonalsFromRightCheckWithoutMainDiagonal(dnaSequence)) return true;
        if (dnaEvaluator.topDiagonalsFromLeftCheckWithMainDiagonal(dnaSequence)) return true;
        return dnaEvaluator.topDiagonalsFromRightCheckWithMainDiagonal(dnaSequence);
    }

    /**
     * Este metodo busca cantidad de humanos verificados, cuantos de ellos son mutantes, y el ratio entre ambos.
     *
     * @return com.ndamelio.mutant.dao.Stats, con informacion estadistica de los humanos evaluados.
     */
    public Stats stats() {
        BigDecimal mutant = mutantRepository.countByHumanType(Human.MUTANT);
        BigDecimal humans = mutantRepository.countAllBy();
        Stats stats = new Stats(mutant, humans);
        return stats;
    }
}
