package com.ndamelio.mutant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndamelio.mutant.boundary.MutantBoundary;
import com.ndamelio.mutant.controller.MutantController;
import com.ndamelio.mutant.dao.DNASequence;
import com.ndamelio.mutant.dao.Stats;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@WebMvcTest(MutantBoundary.class)
public class MutantBoundaryTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MutantController mutantController;

    @Test
    public void checkHumanDNA() throws Exception {
        DNASequence dnaSequence = new DNASequence();
        dnaSequence.setDna(new String[]{"AGCCGA","AGGTGC","AGATGT","AGCAAG","GCTCTA","TCACTG"});

        Mockito.when(mutantController.isMutant(dnaSequence.getDna())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/mutant").content(asJsonString(dnaSequence)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
        Assertions.assertThat(dnaSequence.toString()).containsIgnoringCase("AGCCGA");
    }

    @Test
    public void checkMutantDNA() throws Exception {
        DNASequence dnaSequence = new DNASequence();
        dnaSequence.setDna(new String[]{"AGCCGA","AGGTGC","AGATGT","AGCAAG","GCTCTA","TCACTG"});

        Mockito.when(mutantController.isMutant(dnaSequence.getDna())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/mutant").content(asJsonString(dnaSequence)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    public static byte[] asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writeValueAsBytes(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void statsHumans() throws Exception {
        Stats stats = new Stats(BigDecimal.ONE, BigDecimal.valueOf(5));
        Mockito.when(mutantController.stats()).thenReturn(stats);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/mutant/stats").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"ratio\":0.20,\"count_mutant_dna\":1,\"count_human_dna\":5}"));
    }
}
