package com.ndamelio.mutant.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface MutantRepository extends CrudRepository<Mutant, Integer> {
    Mutant findByHashDna(int hash);

    BigDecimal countByHumanType(Human human);

    BigDecimal countAllBy();

}