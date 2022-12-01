package neuefische.capstone.bpmndiagram;

import neuefische.capstone.ServiceUtils;
import neuefische.capstone.comment.Comment;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;


class BpmnDiagramServiceTest {

    private final BpmnDiagramRepository repository = mock(BpmnDiagramRepository.class);
    private final ServiceUtils serviceUtils = mock(ServiceUtils.class);
    private final BpmnDiagramService service = new BpmnDiagramService(repository, serviceUtils);

    @Test
    void getAllDiagrams_returnsList() {
        //given
        when(repository.findAll()).thenReturn(List.of());
        //when
        List<BpmnDiagram> actual = service.getAllDiagrams();
        //then
        verify(repository).findAll();
        assertEquals(List.of(), actual);
    }

    @Test
    void getLatestDiagram_returnsOnlyLatest() {
        //given
        String businessKey = "capstone.order.order-car";
        BpmnDiagram testDiagram1 = new BpmnDiagram(
                "1"
                , "Order Car"
                , businessKey
                , "order-car.bpmn"
                , 1
                , new ArrayList<>()
                , true
        );
        BpmnDiagram testDiagram2 = new BpmnDiagram(
                "2"
                , "Order New Car"
                , businessKey
                , "order-car.bpmn"
                , 2
                , new ArrayList<>()
                , true
        );
        List<BpmnDiagram> diagramList = List.of(testDiagram1, testDiagram2);
        when(repository.findAll()).thenReturn(diagramList);
        when(repository.findAllByBusinessKey(businessKey)).thenReturn(diagramList);
        //when
        List<BpmnDiagram> actual = service.getLatestDiagrams();
        //then
        assertEquals(List.of(testDiagram2), actual);
        verify(repository, times(2)).findAllByBusinessKey(businessKey);
        verify(repository).findAll();
    }

    @Test
    void getLatestDiagrams_throwsExceptionNoMaxVersion() {
        //given
        String businessKey = "capstone.order.order-car";
        BpmnDiagram testDiagram1 = new BpmnDiagram(
                "1"
                , "Order Car"
                , businessKey
                , "order-car.bpmn"
                , 1
                , new ArrayList<>()
                , true
        );
        BpmnDiagram testDiagram2 = new BpmnDiagram(
                "2"
                , "Order New Car"
                , businessKey
                , "order-car.bpmn"
                , 2
                , new ArrayList<>()
                , true
        );
        List<BpmnDiagram> diagramList = List.of(testDiagram1, testDiagram2);
        when(repository.findAll()).thenReturn(diagramList);
        when(repository.findAllByBusinessKey(businessKey)).thenReturn(List.of());
        //when
        try {
            service.getLatestDiagrams();
            fail();
        } catch (NoSuchElementException e) {
            //then
            assertEquals("No max Version", e.getMessage());
            verify(repository).findAll();
            verify(repository).findAllByBusinessKey(businessKey);
        }
    }

    @Test
    void getHistory_returnsVersionList() {
        //given
        String businessKey = "capstone.order.order-car";
        BpmnDiagram testDiagram1 = new BpmnDiagram(
                "1"
                , "Order Car"
                , businessKey
                , "order-car.bpmn"
                , 1
                , new ArrayList<>()
                , true
        );
        BpmnDiagram testDiagram2 = new BpmnDiagram(
                "2"
                , "Order New Car"
                , businessKey
                , "order-car.bpmn"
                , 2
                , new ArrayList<>()
                , true
        );
        List<BpmnDiagram> diagramList = List.of(testDiagram1, testDiagram2);
        when(repository.findAllByBusinessKey(businessKey)).thenReturn(diagramList);
        //when
        List<BpmnDiagram> actual = service.getHistoryByKey(businessKey);
        //then
        assertEquals(List.of(testDiagram1), actual);
        verify(repository).findAllByBusinessKey(businessKey);
    }

    @Test
    void addBpmnDiagram_returnsDiagramWithId() {
        //given
        String testID = "generatedID";
        BpmnDiagram testDiagram = new BpmnDiagram(
                null
                , "create bill"
                , "capstone.bpmn.billing.create-bill"
                , "create-bill.xml"
                , 1
                , new ArrayList<>()
                , true
        );
        BpmnDiagram testDiagramWithId = testDiagram.withId(testID);
        when(serviceUtils.generateCamundaId(testDiagram.businessKey(), testDiagram.version())).thenReturn(testID);
        when(repository.insert(testDiagramWithId)).thenReturn(testDiagramWithId);
        //when
        BpmnDiagram actual = service.addBpmnDiagram(testDiagram);
        //then
        verify(serviceUtils).generateCamundaId(testDiagram.businessKey(), testDiagram.version());
        verify(repository).insert(testDiagramWithId);
        assertEquals(testDiagramWithId, actual);
    }

    @Test
    void updateBpmnDiagramWithExistingId_returnsDiagram() {
        //given
        BpmnDiagram updatedDiagram = new BpmnDiagram(
                "123"
                , "creation of bill"
                , "capstone.bpmn.billing.create-bill"
                , "create-bill.xml"
                , 2
                , new ArrayList<>()
                , true
        );
        when(repository.save(updatedDiagram)).thenReturn(updatedDiagram);
        when(repository.existsById(updatedDiagram.id())).thenReturn(true);
        //when
        BpmnDiagram actual = service.updateBpmnDiagram(updatedDiagram);
        //then
        verify(repository).save(updatedDiagram);
        assertEquals(updatedDiagram, actual);
    }

    @Test
    void updateBpmnDiagramWithNewId_throwsException() {
        //given
        BpmnDiagram updatedDiagram = new BpmnDiagram(
                "123456"
                , "creation of bill"
                , "capstone.bpmn.billing.create-bill"
                , "create-bill.xml"
                , 2
                , new ArrayList<>()
                , true
        );
        when(repository.existsById(updatedDiagram.id())).thenReturn(false);
        //when
        try {
            service.updateBpmnDiagram(updatedDiagram);
            fail();
        } catch (NoSuchElementException e) {
            //then
            verify(repository, never()).save(updatedDiagram);
        }
    }

    @Test
    void deleteBpmnCustomDiagram_returnsVoid() {
        //given
        String id = "123";
        BpmnDiagram testDiagram = new BpmnDiagram(
                id
                , "create bill"
                , "capstone.bpmn.billing.create-bill"
                , "create-bill.xml"
                , 1
                , new ArrayList<>()
                , true
        );
        when(repository.findById(id)).thenReturn(Optional.of(testDiagram));
        doNothing().when(repository).deleteById(id);
        //when
        service.deleteBpmnDiagram(id);
        //then
        verify(repository).deleteById(id);
    }

    @Test
    void deleteCamundaBpmnDiagram_throwsException() {
        //given
        String id = "123";
        BpmnDiagram testDiagram = new BpmnDiagram(
                id
                , "create bill"
                , "capstone.bpmn.billing.create-bill"
                , "create-bill.xml"
                , 1
                , new ArrayList<>()
                , false
        );
        when(repository.findById(id)).thenReturn(Optional.of(testDiagram));
        //when
        try {
            service.deleteBpmnDiagram(id);
            fail();
        } catch (DeleteNotAllowedException e) {
            //then
            assertEquals("Object can't be deleted because it is synched with Camunda Engine", e.getMessage());
            verify(repository, never()).deleteById(id);
        }
    }

    @Test
    void deleteCustomBpmnDiagram_throwsException() {
        //given
        String id = "123";
        when(repository.findById(id)).thenReturn(Optional.empty());
        //when
        try {
            service.deleteBpmnDiagram(id);
            fail();
        } catch (NoSuchElementException e) {
            //then
            assertEquals("No Element found with this ID", e.getMessage());
            verify(repository, never()).deleteById(id);
        }
    }


    @Test
    void getCommentsByDiagramId() {
        //given
        String diagramId = "1";
        BpmnDiagram testDiagram = new BpmnDiagram(
                diagramId
                , "create bill"
                , "capstone.bpmn.billing.create-bill"
                , "create-bill.xml"
                , 1
                , new ArrayList<>()
                , false
        );
        when(repository.findById(diagramId)).thenReturn(Optional.of(testDiagram));
        //when
        List<Comment> actual = service.getCommentsByDiagramId(diagramId);
        //then
        assertEquals(List.of(), actual);
        verify(repository).findById(diagramId);
    }

    @Test
    void addComment() {
        //given
        String diagramId = "1";
        Comment testComment = new Comment(
                null,
                "test",
                "author",
                null
        );
        BpmnDiagram testDiagram = new BpmnDiagram(
                diagramId
                , "create bill"
                , "capstone.bpmn.billing.create-bill"
                , "create-bill.xml"
                , 1
                , new ArrayList<>()
                , false
        );
        Comment testCommentWithIdAndDate = testComment.withId("1").withTime(LocalDateTime.of(2022, 11, 30, 1, 1));
        when(serviceUtils.generateUUID()).thenReturn("1");
        when(serviceUtils.generateCurrentTime()).thenReturn(LocalDateTime.of(2022, 11, 30, 1, 1));
        when(repository.findById(diagramId)).thenReturn(Optional.of(testDiagram));
        //when
        Comment actual = service.addCommentToDiagram(diagramId, testComment);
        //then
        assertEquals(testCommentWithIdAndDate, actual);
        verify(repository).save(testDiagram.withComments(List.of(testCommentWithIdAndDate)));
    }

    @Test
    void deleteCommentById_successful() {
        //given
        String commentId = "1";
        String diagramId = "100";
        Comment testComment = new Comment(
                commentId,
                "test",
                "author",
                null
        );
        List<Comment> commentList = new ArrayList<>(Collections.singleton(testComment));
        BpmnDiagram testDiagram = new BpmnDiagram(
                diagramId
                , "create bill"
                , "capstone.bpmn.billing.create-bill"
                , "create-bill.xml"
                , 1
                , commentList
                , false
        );
        BpmnDiagram testDiagramWithoutComment = testDiagram.withComments(List.of());
        when(repository.findById(diagramId)).thenReturn(Optional.of(testDiagram));
        when(repository.save(testDiagramWithoutComment)).thenReturn(testDiagramWithoutComment);
        //when
        service.deleteComment(diagramId, commentId);
        //then
        verify(repository).findById(diagramId);
        verify(repository).save(any());
    }

    @Test
    void deleteCommentWithWrongIdThrowsException() {
        //given
        String diagramId = "1";
        BpmnDiagram testDiagram = new BpmnDiagram(
                diagramId
                , "create bill"
                , "capstone.bpmn.billing.create-bill"
                , "create-bill.xml"
                , 1
                , new ArrayList<>()
                , false
        );
        when(repository.findById(diagramId)).thenReturn(Optional.of(testDiagram));
        //when
        try {
            service.deleteComment(diagramId, "123");
            fail();
        } catch (NoSuchElementException e) {
            //then
            verify(repository).findById(diagramId);
            verify(repository, never()).save(any());
        }
    }
}
