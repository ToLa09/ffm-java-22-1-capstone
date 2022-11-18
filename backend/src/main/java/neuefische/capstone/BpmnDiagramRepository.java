package neuefische.capstone;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BpmnDiagramRepository extends MongoRepository<BpmnDiagram, String> {
}
