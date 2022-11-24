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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void GET_fetchCamundaDiagrams_expect204() throws Exception {
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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/camundaprocesses"))
                .andExpect(status().isNoContent());
    }

    @Test
    void GET_fetchCamundaDiagrams_expect404() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/camundaprocesses"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(""));
    }
}