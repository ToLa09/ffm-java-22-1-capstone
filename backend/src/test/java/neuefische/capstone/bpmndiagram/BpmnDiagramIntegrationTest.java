package neuefische.capstone.bpmndiagram;

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
    private BpmnDiagramRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void GETAllDiagrams_expect200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/bpmndiagrams"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void GETLatestDiagrams_expect200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Create_Diagram",
                                    "businessKey": "create-diagram",
                                    "filename": "create-diagram.bpmn",
                                    "version": 1,
                                    "calledProcesses": null,
                                    "commentText": null,
                                    "commentTime": null,
                                    "commentAuthor": null,
                                    "customDiagram": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String responseBody = mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Create New Diagram",
                                    "businessKey": "create-diagram",
                                    "filename": "create-new-diagram.bpmn",
                                    "version": 2,
                                    "calledProcesses": null,
                                    "commentText": null,
                                    "commentTime": null,
                                    "commentAuthor": null,
                                    "customDiagram": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BpmnDiagram responseObject = objectMapper.readValue(responseBody, BpmnDiagram.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bpmndiagrams?distinct=true"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{
                            "id": "<id>",
                            "name": "Create New Diagram",
                            "businessKey": "create-diagram",
                            "filename": "create-new-diagram.bpmn",
                            "version": 2,
                            "calledProcesses": null,
                            "commentText": null,
                            "commentTime": null,
                            "commentAuthor": null,
                            "customDiagram": true
                        }]
                        """.replace("<id>", responseObject.id())));
    }

    @Test
    @DirtiesContext
    void GETHistoryByKey_expect200() throws Exception {
        String responseBody1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Create_Diagram",
                                    "businessKey": "create-diagram",
                                    "filename": "create-diagram.bpmn",
                                    "version": 1,
                                    "calledProcesses": null,
                                    "commentText": null,
                                    "commentTime": null,
                                    "commentAuthor": null,
                                    "customDiagram": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String responseBody2 = mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Create New Diagram",
                                    "businessKey": "create-diagram",
                                    "filename": "create-new-diagram.bpmn",
                                    "version": 2,
                                    "calledProcesses": null,
                                    "commentText": null,
                                    "commentTime": null,
                                    "commentAuthor": null,
                                    "customDiagram": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BpmnDiagram responseObject1 = objectMapper.readValue(responseBody1, BpmnDiagram.class);
        BpmnDiagram responseObject2 = objectMapper.readValue(responseBody2, BpmnDiagram.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bpmndiagrams/history/" + responseObject1.businessKey()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        {
                            "id": "<id1>",
                            "name": "Create_Diagram",
                            "businessKey": "create-diagram",
                            "filename": "create-diagram.bpmn",
                            "version": 1,
                            "calledProcesses": null,
                            "commentText": null,
                            "commentTime": null,
                            "commentAuthor": null,
                            "customDiagram": true
                        },
                        {
                            "id": "<id2>",
                            "name": "Create New Diagram",
                            "businessKey": "create-diagram",
                            "filename": "create-new-diagram.bpmn",
                            "version": 2,
                            "calledProcesses": null,
                            "commentText": null,
                            "commentTime": null,
                            "commentAuthor": null,
                            "customDiagram": true
                        }]
                        """.replace("<id1>", responseObject1.id()).replace("<id2>", responseObject2.id())));
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
                            "commentAuthor": null,
                            "customDiagram": true
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
                                "commentAuthor": null,
                                "customDiagram": true
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
                                    "commentAuthor": null,
                                    "customDiagram": true
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
                                "commentAuthor": null,
                                "customDiagram": true
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
                                "commentAuthor": null,
                                "customDiagram": true
                            }
                        """.replace("<id>", responseObject.id())));
    }

    @Test
    @DirtiesContext
    void PUTBpmnDiagram_expect404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/bpmndiagrams/" + "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "id": "1234",
                                        "name": "test",
                                        "businessKey": "Process_create-diagram",
                                        "filename": "create-diagram.bpmn",
                                        "version": 1,
                                        "calledProcesses": null,
                                        "commentText": null,
                                        "commentTime": null,
                                        "commentAuthor": null,
                                        "customDiagram": true
                                    }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("No Element found with this ID"));
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
                                    "commentAuthor": null,
                                    "customDiagram": true
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
                                    "commentAuthor": null,
                                    "customDiagram": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BpmnDiagram responseObject = objectMapper.readValue(responseBody, BpmnDiagram.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/bpmndiagrams/" + responseObject.id()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DirtiesContext
    void DELETEBpmnDiagram_expect400() throws Exception {
        BpmnDiagram testDiagram = new BpmnDiagram(
                "123"
                , "create bill"
                , "capstone.bpmn.billing.create-bill"
                , "create-bill.xml"
                , 1
                , null
                , "first version of billing"
                , null
                , null
                , false
        );

        repository.save(testDiagram);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/bpmndiagrams/123"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Object can't be deleted because it is synched with Camunda Engine"));
    }
}