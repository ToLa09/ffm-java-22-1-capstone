package capstone.backend;

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
        BpmnDiagram testDiagram = new BpmnDiagram(null,"create bill","capstone.bpmn.billing.create-bill", "create-bill.xml", "first version of billing");
        when(repository.save(testDiagram)).thenReturn(testDiagram.withId("123"));
        //when
        BpmnDiagram actual = service.addBpmnDiagram(testDiagram);
        BpmnDiagram expected = testDiagram.withId("123");
        //then
        verify(repository).save(testDiagram);
        assertEquals(expected, actual);
    }
}