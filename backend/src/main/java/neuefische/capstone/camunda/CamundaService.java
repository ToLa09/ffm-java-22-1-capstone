package neuefische.capstone.camunda;

import neuefische.capstone.bpmndiagram.BpmnDiagram;
import neuefische.capstone.bpmndiagram.BpmnDiagramCalled;
import neuefische.capstone.bpmndiagram.BpmnDiagramRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Objects.requireNonNull;

@Service
public class CamundaService {

    private final WebClient webClient;

    private final BpmnDiagramRepository repository;

    private static final String BODY_IS_NULL_ERROR = "Response Body is null";
    private static final String ENTITY_IS_NULL_ERROR = "Response Entity is null";

    public CamundaService(BpmnDiagramRepository repository, @Value("${camunda.api.baseUrl}") String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
        this.repository = repository;
    }

    public void writeCamundaProcessesToDB() {
        ResponseEntity<List<CamundaProcessModel>> responseEntity = requireNonNull(webClient
                        .get()
                        .uri("/process-definition")
                        .retrieve()
                        .toEntityList(CamundaProcessModel.class)
                        .block()
                , ENTITY_IS_NULL_ERROR);


        List<CamundaProcessModel> processList = requireNonNull(responseEntity.getBody(), BODY_IS_NULL_ERROR);

        if (processList.isEmpty()) {
            throw new CamundaResponseException(BODY_IS_NULL_ERROR);
        }

        for (CamundaProcessModel camundaProcessModel : processList) {
            if (!repository.existsById(camundaProcessModel.id())) {
                BpmnDiagram diagramToInsert = new BpmnDiagram(
                        camundaProcessModel.id(),
                        camundaProcessModel.name(),
                        camundaProcessModel.key(),
                        camundaProcessModel.resource(),
                        camundaProcessModel.version(),
                        new ArrayList<>(),
                        getCalledBpmnDiagramsByDiagramId(camundaProcessModel.id()),
                        !camundaProcessModel.startableInTasklist(),
                        false
                );
                repository.insert(diagramToInsert);
            } else {
                BpmnDiagram diagramToUpdate = repository
                        .findById(camundaProcessModel.id())
                        .orElseThrow(() -> new NoSuchElementException("No element with this ID found"))
                        .withCalledDiagrams(getCalledBpmnDiagramsByDiagramId(camundaProcessModel.id()));
                repository.save(diagramToUpdate);
            }
        }
        for (BpmnDiagram diagram : repository.findAllByCustomDiagram(false)) {
            for (CamundaProcessModel process : processList) {
                if (diagram.id().equals(process.id())) {
                    return;
                }
                repository.delete(diagram);
            }
        }
    }

    public String getXmlFileByDiagramId(String diagramId) {
        ResponseEntity<CamundaProcessXmlModel> responseEntity = requireNonNull(webClient
                        .get()
                        .uri("/process-definition/" + diagramId + "/xml")
                        .retrieve()
                        .toEntity(CamundaProcessXmlModel.class)
                        .block()
                , ENTITY_IS_NULL_ERROR);

        return requireNonNull(responseEntity.getBody(), BODY_IS_NULL_ERROR).xml();
    }

    public List<BpmnDiagramCalled> getCalledBpmnDiagramsByDiagramId(String diagramId) {
        ResponseEntity<List<CamundaCalledProcessesModel>> responseEntity = requireNonNull(webClient
                        .get()
                        .uri("/process-definition/" + diagramId + "/static-called-process-definitions")
                        .retrieve()
                        .toEntityList(CamundaCalledProcessesModel.class)
                        .block()
                , ENTITY_IS_NULL_ERROR);


        List<CamundaCalledProcessesModel> calledProcesses = requireNonNull(responseEntity.getBody(), BODY_IS_NULL_ERROR);

        if (calledProcesses.isEmpty()) {
            return new ArrayList<>();
        }

        List<BpmnDiagramCalled> calledDiagrams = new ArrayList<>();

        for (CamundaCalledProcessesModel calledProcess : calledProcesses) {
            calledDiagrams.add(new BpmnDiagramCalled(
                    calledProcess.id(),
                    calledProcess.calledFromActivityIds()
            ));
        }
        return calledDiagrams;
    }
}
