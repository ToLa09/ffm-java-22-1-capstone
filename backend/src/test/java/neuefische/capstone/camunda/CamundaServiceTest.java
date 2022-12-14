package neuefische.capstone.camunda;

import neuefische.capstone.bpmndiagram.BpmnDiagram;
import neuefische.capstone.bpmndiagram.BpmnDiagramRepository;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(3)
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
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        []
                        """)
                .addHeader("Content-Type", "application/json"));

        when(repository.insert(mockProcess)).thenReturn(mockProcess);
        when(repository.findAllByCustomDiagram(false)).thenReturn(List.of());
        //when
        service.writeCamundaProcessesToDB();
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        HttpUrl expectedUrl = mockWebServer.url(String.format("http://localhost:%s", mockWebServer.getPort()) + "/process-definition/");
        //then
        verify(repository).insert(mockProcess);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals(expectedUrl,recordedRequest.getRequestUrl());
    }

    @Test
    @Order(1)
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
            HttpUrl expectedUrl = mockWebServer.url(String.format("http://localhost:%s", mockWebServer.getPort()) + "/process-definition/");
            RecordedRequest recordedRequest = mockWebServer.takeRequest();
            assertEquals("Response Body is null", e.getMessage());
            assertEquals("GET", recordedRequest.getMethod());
            assertEquals(expectedUrl, recordedRequest.getRequestUrl());
        }
    }

    @Test
    @Order(2)
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
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        []
                        """)
                .addHeader("Content-Type", "application/json")
        );

        when(repository.findById("Process_create-diagram:1:31313844-699b-11ed-aa1c-0a424f65c1c0")).thenReturn(Optional.of(mockProcess));
        //when
        service.writeCamundaProcessesToDB();
        //then
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        HttpUrl expectedUrl = mockWebServer.url(String.format("http://localhost:%s", mockWebServer.getPort()) + "/process-definition/");
        verify(repository, never()).insert(mockProcess);
        verify(repository).save(mockProcess);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals(expectedUrl, recordedRequest.getRequestUrl());
        mockWebServer.takeRequest();
    }

    @Test
    @Order(4)
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
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        []
                        """)
                .addHeader("Content-Type", "application/json")
        );

        when(repository.findAllByCustomDiagram(false)).thenReturn(mockDiagramList);
        when(repository.existsById(mockDiagram.id())).thenReturn(true);
        doNothing().when(repository).deleteByIdNotIn(Set.of(mockDiagram.id()));
        when(repository.findById(mockDiagram.id())).thenReturn(Optional.of(mockDiagram));
        when(repository.findById(mockDiagramToDelete.id())).thenReturn(Optional.of(mockDiagramToDelete));
        //when
        service.writeCamundaProcessesToDB();
        //then
        verify(repository).deleteByIdNotIn(Set.of(mockDiagram.id()));
        verify(repository, never()).insert(any(BpmnDiagram.class));
    }

    @Test
    @Order(5)
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
}
