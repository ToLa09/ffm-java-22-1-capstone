package neuefische.capstone.bpmndiagram;

import lombok.RequiredArgsConstructor;
import neuefische.capstone.ServiceUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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

    public List<BpmnDiagram> getLatestDiagrams() {
        List<BpmnDiagram> allDiagrams = repository.findAll();
        List<BpmnDiagram> latestDiagrams = new ArrayList<>();

        for (BpmnDiagram diagram : allDiagrams) {
            BpmnDiagram latestVersion = repository.findAllByBusinessKey(diagram.businessKey()).stream()
                    .max(Comparator.comparing(BpmnDiagram::version))
                    .orElseThrow(() -> new NoSuchElementException("No max Version"));
            latestDiagrams.add(latestVersion);
        }
        return latestDiagrams.stream().distinct().toList();
    }

    public List<BpmnDiagram> getHistoryByKey(String key) {
        List<BpmnDiagram> history = new ArrayList<>();
        for (BpmnDiagram diagram : repository.findAll()) {
            if (diagram.businessKey().equals(key)) {
                history.add(diagram);
            }
        }
        return history;
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
        for (BpmnDiagram diagram : repository.findAll()) {
            if (diagram.id().equals(id) && diagram.customDiagram()) {
                repository.deleteById(id);
                return;
            }
        }
        throw new DeleteNotAllowedException("Object can't be deleted because it is synched with Camunda Engine");
    }
}
