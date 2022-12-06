package neuefische.capstone.camunda;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CamundaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static MockWebServer mockWebServer;

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @DynamicPropertySource
    static void backendProperties(DynamicPropertyRegistry registry) {
        registry.add("camunda.api.baseUrl", () -> mockWebServer.url("/").toString());
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void POST_fetchCamundaDiagrams_expect204() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        [{
                            "id": "Process_create-diagram:1:31313844-699b-11ed-aa1c-0a424f65c1c0",
                            "key": "Process_create-diagram",
                            "category": "http://bpmn.io/schema/bpmn",
                            "description": null,
                            "name": "Create_Diagram",
                            "version": 1,
                            "resource": "create-diagram.bpmn",
                            "deploymentId": "31243ff2-699b-11ed-aa1c-0a424f65c1c0",
                            "diagram": null,
                            "suspended": false,
                            "tenantId": null,
                            "versionTag": null,
                            "historyTimeToLive": null,
                            "startableInTasklist": true
                          }]
                        """)
                .addHeader("Content-Type", "application/json")
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/camundaprocesses"))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bpmndiagrams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Create_Diagram"))
                .andExpect(jsonPath("$.[0].version").value(1))
                .andExpect(jsonPath("$.[0].businessKey").value("Process_create-diagram"))
                .andExpect(jsonPath("$.[0].filename").value("create-diagram.bpmn"));
    }

    @Test
    void POST_fetchCamundaDiagrams_expect404() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/camundaprocesses"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Response Body is null"));
    }

    @Test
    void GET_getXmlByDiagramId_expect200() throws Exception {
        String diagramId = "create-user:2:c29dba0b-6a5e-11ed-aa1c-0a424f65c1c0";

        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        {
                            "id": "create-user:2:c29dba0b-6a5e-11ed-aa1c-0a424f65c1c0",
                            "bpmn20Xml": "<?xml version=\\"1.0\\" encoding=\\"UTF-8\\"?><bpmn><process></process></bpmn>"
                        }
                        """)
                .addHeader("Content-Type", "application/json"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/camundaprocesses/" + diagramId + "/xml"))
                .andExpect(status().isOk())
                .andExpect(xpath("/bpmn/process").nodeCount(1));
    }
}
