package neuefische.capstone;

import com.fasterxml.jackson.databind.ObjectMapper;
import neuefische.capstone.bpmndiagram.BpmnDiagram;
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
    void GETAllDiagrams_expect200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/bpmndiagrams"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DirtiesContext
    void POSTBpmnDiagram_expect201() throws Exception {
        String responseBody = mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "Create_Diagram",
                        "businessKey": "Process_create-diagram",
                        "filename": "create-diagram.bpmn",
                        "version": 1,
                        "calledProcesses": null,
                        "commentText": null,
                        "commentTime": null,
                        "commentAuthor": null
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
                            "name": "Create_Diagram",
                            "businessKey": "Process_create-diagram",
                            "filename": "create-diagram.bpmn",
                            "version": 1,
                            "calledProcesses": null,
                            "commentText": null,
                            "commentTime": null,
                            "commentAuthor": null
                        }
                    ]
                    """.replace("<id>", responseObject.id())));
    }

    @Test
    @DirtiesContext
    void PUTBpmnDiagram_expect200() throws Exception {
        String responseBody = mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "name": "Create_Diagram",
                        "businessKey": "Process_create-diagram",
                        "filename": "create-diagram.bpmn",
                        "version": 1,
                        "calledProcesses": null,
                        "commentText": null,
                        "commentTime": null,
                        "commentAuthor": null
                    }
                    """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BpmnDiagram responseObject = objectMapper.readValue(responseBody, BpmnDiagram.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/bpmndiagrams/"+responseObject.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "id": "<id>",
                            "name": "test",
                            "businessKey": "Process_create-diagram",
                            "filename": "create-diagram.bpmn",
                            "version": 1,
                            "calledProcesses": null,
                            "commentText": null,
                            "commentTime": null,
                            "commentAuthor": null
                        }
                    """.replace("<id>",responseObject.id())))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id": "<id>",
                            "name": "test",
                            "businessKey": "Process_create-diagram",
                            "filename": "create-diagram.bpmn",
                            "version": 1,
                            "calledProcesses": null,
                            "commentText": null,
                            "commentTime": null,
                            "commentAuthor": null
                        }
                    """.replace("<id>",responseObject.id())));
    }

    @Test
    @DirtiesContext
    void PUTBpmnDiagram_expect400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/bpmndiagrams/1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "id": "1111",
                        "name": "Create_Diagram",
                        "businessKey": "Process_create-diagram",
                        "filename": "create-diagram.bpmn",
                        "version": 1,
                        "calledProcesses": null,
                        "commentText": null,
                        "commentTime": null,
                        "commentAuthor": null
                    }
                    """))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Request ID Mismatch"));
    }

    @Test
    @DirtiesContext
    void DELETEBpmnDiagram_expect204() throws Exception {
        String responseBody = mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "name": "Create_Diagram",
                        "businessKey": "Process_create-diagram",
                        "filename": "create-diagram.bpmn",
                        "version": 1,
                        "calledProcesses": null,
                        "commentText": null,
                        "commentTime": null,
                        "commentAuthor": null
                    }
                    """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BpmnDiagram responseObject = objectMapper.readValue(responseBody, BpmnDiagram.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/bpmndiagrams/"+responseObject.id()))
                .andExpect(status().isNoContent());
    }
}