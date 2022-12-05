package neuefische.capstone.bpmndiagram;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BpmnDiagramRepository extends MongoRepository<BpmnDiagram, String> {
    List<BpmnDiagram> findAllByBusinessKey(String businessKey);

    List<BpmnDiagram> findAllByCustomDiagram(boolean customDiagram);
}
