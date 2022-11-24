package neuefische.capstone.camunda;

import neuefische.capstone.bpmndiagram.BpmnDiagram;
import neuefische.capstone.bpmndiagram.BpmnDiagramRepository;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CamundaServiceTest {
    private static MockWebServer mockWebServer;
    private final BpmnDiagramRepository repository = mock(BpmnDiagramRepository.class);
    private CamundaService service;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockWebServer.getPort());
        service = new CamundaService(repository,baseUrl);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void writeCamundaProcessesToDB_correctResponse() throws InterruptedException {
        //given
        BpmnDiagram mockProcess = new BpmnDiagram(
                "Process_create-diagram:1:31313844-699b-11ed-aa1c-0a424f65c1c0"
                ,"Create_Diagram"
                ,"Process_create-diagram"
                , "create-diagram.bpmn"
                , 1
                ,null
                ,null
                ,null
                ,null
        );

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

        when(repository.existsById("Process_create-diagram:1:31313844-699b-11ed-aa1c-0a424f65c1c0")).thenReturn(false);
        when(repository.insert(mockProcess)).thenReturn(mockProcess);

        //when
        service.writeCamundaProcessesToDB();
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        HttpUrl expectedUrl = mockWebServer.url(String.format("http://localhost:%s", mockWebServer.getPort()) + "/process-definition");
        //then
        verify(repository).existsById("Process_create-diagram:1:31313844-699b-11ed-aa1c-0a424f65c1c0");
        verify(repository).insert(mockProcess);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals(expectedUrl,recordedRequest.getRequestUrl());
    }

    @Test
    void writeCamundaProcessesToDB_BodyNull() throws InterruptedException {
        //given
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
        );
        //when
        try {
            service.writeCamundaProcessesToDB();
            fail();
        } catch (CamundaResponseException e){
            RecordedRequest recordedRequest = mockWebServer.takeRequest();
            HttpUrl expectedUrl = mockWebServer.url(String.format("http://localhost:%s", mockWebServer.getPort()) + "/process-definition");
            //then
            assertEquals("Response Body is null",e.getMessage());
            assertEquals("GET", recordedRequest.getMethod());
            assertEquals(expectedUrl,recordedRequest.getRequestUrl());
        }
    }
}