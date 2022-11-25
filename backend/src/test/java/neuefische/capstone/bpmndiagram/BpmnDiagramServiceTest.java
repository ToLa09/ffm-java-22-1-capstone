package neuefische.capstone.bpmndiagram;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;


class BpmnDiagramServiceTest {

    private final BpmnDiagramRepository repository = mock(BpmnDiagramRepository.class);
    private final ServiceUtils serviceUtils = mock(ServiceUtils.class);
    private final BpmnDiagramService service = new BpmnDiagramService(repository, serviceUtils);

    @Test
    void getAllDiagrams() {
        //given
        when(repository.findAll()).thenReturn(new ArrayList<>(List.of()));
        //when
        List<BpmnDiagram> actual = service.getAllDiagrams();
        //then
        verify(repository).findAll();
        assertEquals(new ArrayList<>(), actual);
    }

    @Test
    void addBpmnDiagram() {
        //given
        String testID = "generatedID";
        BpmnDiagram testDiagram = new BpmnDiagram(
                null
                , "create bill"
                , "capstone.bpmn.billing.create-bill"
                , "create-bill.xml"
                , 1
                , null
                , "first version of billing"
                , null
                , null
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
    void updateBpmnDiagramWithExistingId() {
        //given
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
                , true
        );
        BpmnDiagram updatedDiagram = new BpmnDiagram(
                "123"
                , "creation of bill"
                , "capstone.bpmn.billing.create-bill"
                , "create-bill.xml"
                , 2
                , null
                , "first version of billing"
                , null
                , null
                , true
        );
        when(repository.save(updatedDiagram)).thenReturn(updatedDiagram);
        when(repository.findAll()).thenReturn(new ArrayList<>(List.of(testDiagram)));
        //when
        BpmnDiagram actual = service.updateBpmnDiagram(updatedDiagram);
        //then
        verify(repository).save(updatedDiagram);
        assertEquals(updatedDiagram, actual);
    }

    @Test
    void updateBpmnDiagramWithNewId() {
        //given
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
                , true
        );
        BpmnDiagram updatedDiagram = new BpmnDiagram(
                "123456"
                , "creation of bill"
                , "capstone.bpmn.billing.create-bill"
                , "create-bill.xml"
                , 2
                , null
                , "first version of billing"
                , null
                , null
                , true
        );
        when(repository.findAll()).thenReturn(new ArrayList<>(List.of(testDiagram)));
        //when
        try {
            service.updateBpmnDiagram(updatedDiagram);
            fail();
        } catch (NoSuchElementException e) {
            //then
            verify(repository, never()).save(updatedDiagram);
            verify(repository).findAll();
        }
    }

    @Test
    void deleteBpmnDiagram() {
        //given
        String id = "123";
        doNothing().when(repository).deleteById(id);
        //when
        service.deleteBpmnDiagram(id);
        //then
        verify(repository).deleteById(id);
    }
}
