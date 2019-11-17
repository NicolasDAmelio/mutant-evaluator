package com.ndamelio.mutant.boundary;

import com.ndamelio.mutant.config.MutantForbiddenException;
import com.ndamelio.mutant.dao.DNASequence;
import com.ndamelio.mutant.controller.MutantController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/mutant")
public class MutantBoundary {

    private final MutantController mutantController;

    public MutantBoundary(MutantController mutantController) {
        this.mutantController = mutantController;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public void checkDNA(@RequestBody @Valid DNASequence dnaSequence) throws Exception {
        boolean result = mutantController.isMutant(dnaSequence.getDna());
        if (!result) throw new MutantForbiddenException("Human DNA");
    }

    @GetMapping("/stats")
    public Object statsHumans() {
        return mutantController.stats();
    }

}