package com.ndamelio.mutant.entity;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "mutant")
public class Mutant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mutant_id")
    private Integer mutantId;

    @Column(name = "dna")
    private String dna;

    @Enumerated(EnumType.STRING)
    @Column(name = "human_type")
    private Human humanType;

    @Column(name = "hash_dna", unique = true)
    private int hashDna;

    public Mutant() {
    }

    public Mutant(char[][] dna, boolean isMutant) {
        this.dna = Arrays.deepToString(dna);
        this.humanType = isMutant ? Human.MUTANT : Human.NOT_MUTANT;
        this.hashDna = this.dna.hashCode();
    }

    public Integer getMutantId() {
        return mutantId;
    }

    public void setMutantId(Integer mutantId) {
        this.mutantId = mutantId;
    }

    public String getDna() {
        return dna;
    }

    public void setDna(String dna) {
        this.dna = dna;
    }

    public Human getHumanType() {
        return humanType;
    }

    public void setHumanType(Human humanType) {
        this.humanType = humanType;
    }

    public int getHashDna() {
        return hashDna;
    }

    public void setHashDna(int hashDna) {
        this.hashDna = hashDna;
    }

    @Override
    public String toString() {
        return "{\"Mutant\":{"
                + "\"mutantId\":\"" + mutantId + "\""
                + ", \"dna\":" + dna
                + ", \"humanType\":\"" + humanType + "\""
                + ", \"hashDna\":\"" + hashDna + "\""
                + "}}";
    }
}