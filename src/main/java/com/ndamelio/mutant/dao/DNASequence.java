package com.ndamelio.mutant.dao;

import java.util.Arrays;

public class DNASequence {

    private String[] dna;

    public DNASequence() {
    }

    public String[] getDna() {
        return dna;
    }

    public void setDna(String[] dna) {
        this.dna = dna;
    }

    @Override
    public String toString() {
        return "{\"DNASequence\":{"
                + "\"dna\":" + Arrays.toString(dna)
                + "}}";
    }
}