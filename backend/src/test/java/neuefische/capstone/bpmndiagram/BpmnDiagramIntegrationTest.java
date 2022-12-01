package neuefische.capstone.bpmndiagram;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import neuefische.capstone.comment.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BpmnDiagramIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BpmnDiagramRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DirtiesContext
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
                .andExpect(status().isCreated());

        String responseBody = mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Create New Diagram",
                                    "businessKey": "create-diagram",
                                    "filename": "create-new-diagram.bpmn",
                                    "version": 2,
                                    "comments": [],
                                    "customDiagram": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BpmnDiagram responseObject = objectMapper.readValue(responseBody, BpmnDiagram.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bpmndiagrams?onlylatestversions=true"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{
                            "id": "<id>",
                            "name": "Create New Diagram",
                            "businessKey": "create-diagram",
                            "filename": "create-new-diagram.bpmn",
                            "version": 2,
                            "comments": [],
                            "customDiagram": true
                        }]
                        """.replace("<id>", responseObject.id())));
    }

    @Test
    @DirtiesContext
    void GETHistoryByKey_expect200() throws Exception {
        String responseBody = mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams")
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

        mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams")
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
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Another Diagram",
                                    "businessKey": "test",
                                    "filename": "test.bpmn",
                                    "version": 1,
                                    "comments": [],
                                    "customDiagram": true
                                }
                                """))
                .andExpect(status().isCreated());

        BpmnDiagram responseObject1 = objectMapper.readValue(responseBody, BpmnDiagram.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bpmndiagrams/" + responseObject1.businessKey() + "/history"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{
                            "id": "<id>",
                            "name": "Create_Diagram",
                            "businessKey": "create-diagram",
                            "filename": "create-diagram.bpmn",
                            "version": 1,
                            "comments": [],
                            "customDiagram": true
                        }]
                        """.replace("<id>", responseObject1.id())));
    }

    @Test
    @DirtiesContext
    void POSTandGETBpmnDiagram_expect201and200() throws Exception {
        String responseBody = mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Create_Diagram",
                                    "businessKey": "Process_create-diagram",
                                    "filename": "create-diagram.bpmn",
                                    "version": 1,
                                    "comments": [],
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
                                "comments": [],
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
                                    "comments": [],
                                    "customDiagram": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BpmnDiagram responseObject = objectMapper.readValue(responseBody, BpmnDiagram.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/bpmndiagrams/" + responseObject.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "id": "<id>",
                                        "name": "test",
                                        "businessKey": "Process_create-diagram",
                                        "filename": "create-diagram.bpmn",
                                        "version": 1,
                                        "comments": [],
                                        "customDiagram": true
                                    }
                                """.replace("<id>", responseObject.id())))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            {
                                "id": "<id>",
                                "name": "test",
                                "businessKey": "Process_create-diagram",
                                "filename": "create-diagram.bpmn",
                                "version": 1,
                                "comments": [],
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
                , new ArrayList<>()
                , false
        );

        repository.save(testDiagram);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/bpmndiagrams/123"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Object can't be deleted because it is synched with Camunda Engine"));
    }


    @Test
    @DirtiesContext
    void POSTCommentAndGETByDiagramId_expect201and200() throws Exception {
        String responseBodyDiagram = mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Create_Diagram",
                                    "businessKey": "Process_create-diagram",
                                    "filename": "create-diagram.bpmn",
                                    "version": 1,
                                    "comments": [],
                                    "customDiagram": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BpmnDiagram responseDiagram = objectMapper.readValue(responseBodyDiagram, BpmnDiagram.class);

        String responseBodyComment = mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams/" + responseDiagram.id() + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "content": "test comment",
                                    "author": "testauthor"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Comment responseComment = objectMapper.readValue(responseBodyComment, Comment.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bpmndiagrams/" + responseDiagram.id() + "/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(responseComment.id()))
                .andExpect(jsonPath("$.[0].content").value("test comment"))
                .andExpect(jsonPath("$.[0].author").value("testauthor"));
    }

    @Test
    void POSTComment_expect404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams/1234/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "content": "test comment",
                                    "author": "testauthor"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("No diagram found with this ID"));
    }

    @Test
    void GETComment_expect404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/bpmndiagrams/123/comments"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("No diagram with this ID found"));
    }

    @Test
    @DirtiesContext
    void DELETEComment_expect204() throws Exception {
        String responseBodyDiagram = mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Create_Diagram",
                                    "businessKey": "Process_create-diagram",
                                    "filename": "create-diagram.bpmn",
                                    "version": 1,
                                    "comments": [],
                                    "customDiagram": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BpmnDiagram responseDiagram = objectMapper.readValue(responseBodyDiagram, BpmnDiagram.class);

        String responseBodyComment = mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams/" + responseDiagram.id() + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "content": "test comment",
                                    "author": "testauthor"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Comment responseComment = objectMapper.readValue(responseBodyComment, Comment.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/bpmndiagrams/" + responseDiagram.id() + "/comments/" + responseComment.id()))
                .andExpect(status().isNoContent());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/bpmndiagrams/" + responseDiagram.id() + "/comments"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DirtiesContext
    void DELETEComment_expect404() throws Exception {
        String responseBodyDiagram = mockMvc.perform(MockMvcRequestBuilders.post("/api/bpmndiagrams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Create_Diagram",
                                    "businessKey": "Process_create-diagram",
                                    "filename": "create-diagram.bpmn",
                                    "version": 1,
                                    "comments": [],
                                    "customDiagram": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BpmnDiagram responseDiagram = objectMapper.readValue(responseBodyDiagram, BpmnDiagram.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/bpmndiagrams/" + responseDiagram.id() + "/comments/123"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("No comment found with this Id to this diagramId"));
    }
}