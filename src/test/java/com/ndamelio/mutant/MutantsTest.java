package com.ndamelio.mutant;

import com.ndamelio.mutant.boundary.MutantBoundary;
import com.ndamelio.mutant.config.MutantInvalidDNAException;
import com.ndamelio.mutant.controller.MutantController;
import com.ndamelio.mutant.dao.Stats;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MutantsTest {

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String PATH_MUTANT = "/mutant";
    private static final String PATH_MUTANT_STATS = "/mutant/stats";
    @InjectMocks
    private MutantBoundary mutantBoundary;
    @Mock
    private MutantController mutantController;
    private MockMvc mockMvc;

    private String[] dnaMutant = new String[]{"GGGGTA", "ATGTGC", "AGTTGG", "AGATGG", "AAAATA", "TCGCTG"};

    private String[] dnaHuman = new String[]{"AAAAAA", "TCCTTC", "GTCTGG", "TGTTTG", "ACAGTA", "ACTCAG"};

    private String[] invalidDNA = new String[]{"ABGCGA", "CAGTGC", "TTATGG", "AGAAGG", "CCCCTA"};

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mutantBoundary)
                .build();
    }

    @Test
    public void testDNAMutant() throws Exception {

        String mockMutantBody = "{\"dna\":[\"GGGGTA\",\"ATGTGC\",\"AGTTGG\",\"AGATGG\",\"AAAATA\",\"TCGCTG\"]}";

        mockIsMutant(dnaMutant, true);

        ResultActions resultActions = mockMvc.perform(post(PATH_MUTANT).contentType(CONTENT_TYPE_JSON).content(mockMutantBody));

        MvcResult result = resultActions.andExpect(status().isOk()).andReturn();
        Assert.isTrue(result.getResponse().getContentAsString().isEmpty(), "Response body must be empty");
    }

    @Test
    public void testDNAHuman() throws Exception {

        String mockHumanBody = "{\"dna\":[\"AAAAAA\",\"TCCTTC\",\"GTCTGG\",\"TGTTTG\",\"ACAGTA\",\"ACTCAG\"]}";

        mockIsMutant(dnaHuman, false);

        ResultActions resultActions = mockMvc.perform(post(PATH_MUTANT).contentType(CONTENT_TYPE_JSON).content(mockHumanBody));

        MvcResult result = resultActions.andExpect(status().isForbidden()).andReturn();
        Assert.isTrue(result.getResponse().getContentAsString().isEmpty(), "Response body must be empty");
    }

    @Test
    public void testBadDNA() throws Exception {

        String mockAlienBody = "{\"dna\":[\"ABGCGA\",\"CAGTGC\",\"TTATGG\",\"AGAAGG\",\"CCCCTA\"]}";

        Mockito.doThrow(new MutantInvalidDNAException("Bad DNA")).when(mutantController).isMutant(invalidDNA);

        ResultActions resultActions = mockMvc.perform(post(PATH_MUTANT).contentType(CONTENT_TYPE_JSON).content(mockAlienBody));

        MvcResult result = resultActions.andExpect(status().isForbidden()).andReturn();
        Assert.isTrue(result.getResponse().getContentAsString().isEmpty(), "Response body must be empty");
    }

    @Test
    public void testStatistics() throws Exception {
        Mockito.when(mutantController.stats()).thenReturn(new Stats(BigDecimal.valueOf(3), BigDecimal.valueOf(5)));

        ResultActions resultActions = mockMvc.perform(get(PATH_MUTANT_STATS));

        MvcResult result = resultActions.andExpect(status().isOk()).andReturn();
        Assert.isTrue(!result.getResponse().getContentAsString().isEmpty(), "Response body must not be empty");
    }

    private void mockIsMutant(String[] dna, boolean expectedResult) throws MutantInvalidDNAException {
        Mockito.when(mutantController.isMutant(dna)).thenReturn(expectedResult);
    }

}
