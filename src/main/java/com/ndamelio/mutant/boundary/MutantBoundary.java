package com.ndamelio.mutant.boundary;

import com.ndamelio.mutant.dao.DNASequence;
import com.ndamelio.mutant.controller.MutantController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/mutant")
public class MutantBoundary {

    private final static Logger LOG = LoggerFactory.getLogger(MutantBoundary.class);

    private final MutantController mutantController;

    public MutantBoundary(MutantController mutantController) {
        this.mutantController = mutantController;
    }

    @PostMapping
    public Object checkDNA(@RequestBody @Valid DNASequence dnaSequence) throws Exception {
        return mutantController.isMutant(dnaSequence.getDna());
    }

    @GetMapping("/stats")
    public Object statsHumans() {
        return mutantController.stats();
    }

}