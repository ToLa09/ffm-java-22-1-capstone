package neuefische.capstone.bpmndiagram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BpmnDiagramService {

    private final BpmnDiagramRepository repository;
    private final ServiceUtils serviceUtils;

    public List<BpmnDiagram> getAllDiagrams() {
        return repository.findAll();
    }

    public BpmnDiagram addBpmnDiagram(BpmnDiagram newBpmnDiagram) {
        String id = serviceUtils.generateCamundaId(newBpmnDiagram.businessKey(), newBpmnDiagram.version());
        BpmnDiagram bpmnDiagramWithId = newBpmnDiagram.withId(id);
        return repository.insert(bpmnDiagramWithId);
    }

    public BpmnDiagram updateBpmnDiagram(BpmnDiagram updatedBpmnDiagram) {
        return repository.save(updatedBpmnDiagram);
    }

    public void deleteBpmnDiagram(String id) {
        repository.deleteById(id);
    }
}
