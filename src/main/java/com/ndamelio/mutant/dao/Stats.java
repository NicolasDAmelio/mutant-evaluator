package com.ndamelio.mutant.dao;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Stats {

    private Long countMutantDNA;

    private Long countHumanDNA;

    private BigDecimal ratio;

    public Stats() {
    }

    public Stats(BigDecimal countMutantDNA, BigDecimal countHumanDNA) {
        this.countMutantDNA = countMutantDNA.longValue();
        this.countHumanDNA = countHumanDNA.longValue();

        this.ratio = (countHumanDNA.compareTo(BigDecimal.ZERO) != 0) ? countMutantDNA.divide(countHumanDNA, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }

    @JsonGetter("count_mutant_dna")
    public Long getCountMutantDNA() {
        return countMutantDNA;
    }

    public void setCountMutantDNA(Long countMutantDNA) {
        this.countMutantDNA = countMutantDNA;
    }

    @JsonGetter("count_human_dna")
    public Long getCountHumanDNA() {
        return countHumanDNA;
    }

    public void setCountHumanDNA(Long countHumanDNA) {
        this.countHumanDNA = countHumanDNA;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "{\"StatsObject\":{"
                + "\"countMutantDNA\":\"" + countMutantDNA + "\""
                + ", \"countHumanDNA\":\"" + countHumanDNA + "\""
                + ", \"ratio\":\"" + ratio + "\""
                + "}}";
    }
}
