package neuefische.capstone;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BpmnDiagramIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllDiagrams() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/bpmndiagrams"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DirtiesContext
    void addBpmnDiagram() throws Exception {
        String responseBody = mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "create bill",
                        "businessKey": "capstone.bpmn.billing.create-bill",
                        "xmlFile": "create-bill.xml",
                        "comment": "first version of billing"
                    }
                    """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BpmnDiagram responseObject = objectMapper.readValue(responseBody, BpmnDiagram.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bpmndiagrams"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    [
                        {
                            "id": "<id>",
                            "name": "create bill",
                            "businessKey": "capstone.bpmn.billing.create-bill",
                            "xmlFile": "create-bill.xml",
                            "comment": "first version of billing"
                        }
                    ]
                    """.replace("<id>", responseObject.id())));
    }
}