package neuefische.capstone.camunda;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CamundaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void GET_fetchCamundaDiagrams_expect204() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/camundaprocesses"))
                .andExpect(status().isNoContent());
    }
}