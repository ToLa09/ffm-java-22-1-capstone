package neuefische.capstone;

import neuefische.capstone.bpmndiagram.BpmnDiagram;
import neuefische.capstone.bpmndiagram.BpmnDiagramRepository;
import neuefische.capstone.bpmndiagram.BpmnDiagramService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class BpmnDiagramServiceTest {

    private final BpmnDiagramRepository repository = mock(BpmnDiagramRepository.class);

    private final BpmnDiagramService service = new BpmnDiagramService(repository);

    @Test
    void getAllDiagrams() {
        //given
        when(repository.findAll()).thenReturn(new ArrayList<>());
        //when
        List<BpmnDiagram> actual = service.getAllDiagrams();
        //then
        verify(repository).findAll();
        assertEquals(new ArrayList<>(), actual);
    }

    @Test
    void addBpmnDiagram() {
        //given
        BpmnDiagram testDiagram = new BpmnDiagram(
                null
                ,"create bill"
                ,"capstone.bpmn.billing.create-bill"
                , "create-bill.xml"
                , 1
                ,null
                ,"first version of billing"
                ,null
                ,null
        );
        when(repository.insert(testDiagram)).thenReturn(testDiagram.withId("123"));
        //when
        BpmnDiagram actual = service.addBpmnDiagram(testDiagram);
        BpmnDiagram expected = testDiagram.withId("123");
        //then
        verify(repository).insert(testDiagram);
        assertEquals(expected, actual);
    }

    @Test
    void updateBpmnDiagram() {
        //given
        BpmnDiagram testDiagram = new BpmnDiagram(
                null
                ,"create bill"
                ,"capstone.bpmn.billing.create-bill"
                , "create-bill.xml"
                , 1
                ,null
                ,"first version of billing"
                ,null
                ,null
        );
        when(repository.save(testDiagram)).thenReturn(testDiagram);
        //when
        BpmnDiagram actual = service.updateBpmnDiagram(testDiagram);
        //then
        verify(repository).save(testDiagram);
        assertEquals(testDiagram, actual);
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