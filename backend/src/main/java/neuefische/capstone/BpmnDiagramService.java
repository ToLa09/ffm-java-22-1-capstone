package neuefische.capstone;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BpmnDiagramService {

    private final BpmnDiagramRepository repository;

    public List<BpmnDiagram> getAllDiagrams() {
        return repository.findAll();
    }

    public BpmnDiagram addBpmnDiagram(BpmnDiagram bpmnDiagram) {
        return repository.save(bpmnDiagram);
    }

    public BpmnDiagram updateBpmnDiagram(BpmnDiagram updatedBpmnDiagram) {
        return repository.insert(updatedBpmnDiagram);
    }

    public void deleteBpmnDiagram(String id) {
        repository.deleteById(id);
    }
}
