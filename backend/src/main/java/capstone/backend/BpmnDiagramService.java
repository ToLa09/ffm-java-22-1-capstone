package capstone.backend;

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
}
