package neuefische.capstone.bpmndiagram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
        BpmnDiagram bpmnDiagramWithId = newBpmnDiagram.withId(id).withCustomDiagram(true);
        return repository.insert(bpmnDiagramWithId);
    }

    public BpmnDiagram updateBpmnDiagram(BpmnDiagram updatedBpmnDiagram) {
        for (BpmnDiagram diagram : getAllDiagrams()) {
            if (diagram.id().equals(updatedBpmnDiagram.id())) {
                return repository.save(updatedBpmnDiagram);
            }
        }
        throw new NoSuchElementException("No Element found with this ID");
    }

    public void deleteBpmnDiagram(String id) {
        repository.deleteById(id);
    }
}
