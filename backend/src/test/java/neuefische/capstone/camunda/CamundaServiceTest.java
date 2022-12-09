package neuefische.capstone.camunda;

import neuefische.capstone.bpmndiagram.BpmnDiagram;
import neuefische.capstone.bpmndiagram.BpmnDiagramCalled;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
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
                , "Create_Diagram"
                , "Process_create-diagram"
                , "create-diagram.bpmn"
                , 1
                , new ArrayList<>()
                , new ArrayList<>()
                , true
                , false
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
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        []
                        """)
                .addHeader("Content-Type", "application/json"));

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
        } catch (CamundaResponseException e) {
            //then
            RecordedRequest recordedRequest = mockWebServer.takeRequest();
            HttpUrl expectedUrl = mockWebServer.url(String.format("http://localhost:%s", mockWebServer.getPort()) + "/process-definition");
            assertEquals("Response Body is null", e.getMessage());
            assertEquals("GET", recordedRequest.getMethod());
            assertEquals(expectedUrl, recordedRequest.getRequestUrl());
        }
    }

    @Test
    void writeCamundaProcessesToDB_IDexistsAlready() throws InterruptedException {
        //given
        BpmnDiagram mockProcess = new BpmnDiagram(
                "Process_create-diagram:1:31313844-699b-11ed-aa1c-0a424f65c1c0"
                , "Create_Diagram"
                , "Process_create-diagram"
                , "create-diagram.bpmn"
                , 1
                , new ArrayList<>()
                , new ArrayList<>()
                , false
                , false
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

        when(repository.existsById("Process_create-diagram:1:31313844-699b-11ed-aa1c-0a424f65c1c0")).thenReturn(true);
        //when
        service.writeCamundaProcessesToDB();
        //then
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        HttpUrl expectedUrl = mockWebServer.url(String.format("http://localhost:%s", mockWebServer.getPort()) + "/process-definition");
        verify(repository).existsById("Process_create-diagram:1:31313844-699b-11ed-aa1c-0a424f65c1c0");
        verify(repository, never()).insert(mockProcess);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals(expectedUrl, recordedRequest.getRequestUrl());
    }

    @Test
    void deleteProcessesWhichAreDeletedInCamundaEngine() {
        //given
        BpmnDiagram mockDiagramToDelete = new BpmnDiagram(
                "Process_create-diagram:1:31313844-699b-11ed-aa1c-0a424f65c1c0"
                , "Create_Diagram"
                , "Process_create-diagram"
                , "create-diagram.bpmn"
                , 1
                , new ArrayList<>()
                , new ArrayList<>()
                , false
                , false
        );
        BpmnDiagram mockDiagram = new BpmnDiagram(
                "ReviewInvoice:1:49eb58c6-6994-11ed-996b-0a424f65c1c0"
                , "Review Invoice"
                , "ReviewInvoice"
                , "reviewInvoice.bpmn"
                , 1
                , new ArrayList<>()
                , new ArrayList<>()
                , false
                , false
        );

        List<BpmnDiagram> mockDiagramList = List.of(mockDiagramToDelete, mockDiagram);

        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        [{
                             "id": "ReviewInvoice:1:49eb58c6-6994-11ed-996b-0a424f65c1c0",
                             "key": "ReviewInvoice",
                             "category": "http://bpmn.io/schema/bpmn",
                             "description": null,
                             "name": "Review Invoice",
                             "version": 1,
                             "resource": "reviewInvoice.bpmn",
                             "deploymentId": "49856b00-6994-11ed-996b-0a424f65c1c0",
                             "diagram": null,
                             "suspended": false,
                             "tenantId": null,
                             "versionTag": null,
                             "historyTimeToLive": 45,
                             "startableInTasklist": false
                        }]
                        """)
                .addHeader("Content-Type", "application/json")
        );

        when(repository.findAllByCustomDiagram(false)).thenReturn(mockDiagramList);
        when(repository.existsById(mockDiagram.id())).thenReturn(true);
        doNothing().when(repository).delete(mockDiagramToDelete);
        //when
        service.writeCamundaProcessesToDB();
        //then
        verify(repository).delete(mockDiagramToDelete);
        verify(repository, never()).insert(any(BpmnDiagram.class));
    }

    @Test
    void getXmlByDiagramId() {
        //given
        String diagramId = "create-user:2:c29dba0b-6a5e-11ed-aa1c-0a424f65c1c0";
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        {
                            "id": "create-user:2:c29dba0b-6a5e-11ed-aa1c-0a424f65c1c0",
                            "bpmn20Xml": "<?xml><bpmn:definitions><bpmn:process id=\\"create-user\\" name=\\"create-user\\" isExecutable=\\"true\\"></bpmn:definitions>"
                        }
                        """)
                .addHeader("Content-Type", "application/json"));
        //when
        String actual = service.getXmlFileByDiagramId(diagramId);
        String expected = "<?xml><bpmn:definitions><bpmn:process id=\"create-user\" name=\"create-user\" isExecutable=\"true\"></bpmn:definitions>";
        //then
        assertEquals(expected, actual);
    }

    @Test
    void getCalledBpmnDiagramsById_ReturnsList() {
        //given
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        [
                                {
                                    "id": "validate-password:1:b64b46f8-6a5e-11ed-aa1c-0a424f65c1c0",
                                        "key": "validate-password",
                                        "category": "http://bpmn.io/schema/bpmn",
                                        "description": null,
                                        "name": "validate-password",
                                        "version": 1,
                                        "resource": "validate-password.bpmn",
                                        "deploymentId": "b643a5d6-6a5e-11ed-aa1c-0a424f65c1c0",
                                        "diagram": null,
                                        "suspended": false,
                                        "tenantId": null,
                                        "versionTag": null,
                                        "historyTimeToLive": null,
                                        "calledFromActivityIds": [
                                    "Activity_1ka0o51"
                            ],
                                    "callingProcessDefinitionId": "create-user:2:c29dba0b-6a5e-11ed-aa1c-0a424f65c1c0",
                                        "startableInTasklist": true
                                }
                        ]
                        """)
                .addHeader("Content-Type", "application/json"));
        //when
        List<BpmnDiagramCalled> actual = service.getCalledBpmnDiagramsByDiagramId("create-user:2:c29dba0b-6a5e-11ed-aa1c-0a424f65c1c0");
        List<BpmnDiagramCalled> expected = List.of(new BpmnDiagramCalled(
                "validate-password:1:b64b46f8-6a5e-11ed-aa1c-0a424f65c1c0",
                List.of("Activity_1ka0o51")
        ));
        //then
        assertEquals(expected, actual);
    }

    @Test
    void getCalledBpmnDiagramsById_ReturnsEmptyList() {
        //given
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        []
                        """)
                .addHeader("Content-Type", "application/json"));
        //when
        List<BpmnDiagramCalled> actual = service.getCalledBpmnDiagramsByDiagramId("create-user:2:c29dba0b-6a5e-11ed-aa1c-0a424f65c1c0");
        List<BpmnDiagramCalled> expected = List.of();
        //then
        assertEquals(expected, actual);
    }
}
