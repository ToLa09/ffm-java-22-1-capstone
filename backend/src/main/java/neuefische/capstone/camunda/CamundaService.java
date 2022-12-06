package neuefische.capstone.camunda;

import neuefische.capstone.bpmndiagram.BpmnDiagram;
import neuefische.capstone.bpmndiagram.BpmnDiagramRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
public class CamundaService {

    private final WebClient webClient;

    private final BpmnDiagramRepository repository;

    private static final String ERROR_MESSAGE = "Response Body is null";

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
                , "Response Entity is null");


        List<CamundaProcessModel> processList = requireNonNull(responseEntity.getBody(), ERROR_MESSAGE);

        if (processList.isEmpty()) {
            throw new CamundaResponseException(ERROR_MESSAGE);
        }

        for (CamundaProcessModel camundaProcessModel : processList) {
            BpmnDiagram diagramToInsert = new BpmnDiagram(
                    camundaProcessModel.id(),
                    camundaProcessModel.name(),
                    camundaProcessModel.key(),
                    camundaProcessModel.resource(),
                    camundaProcessModel.version(),
                    new ArrayList<>(),
                    false
            );
            if (!repository.existsById(camundaProcessModel.id())) {
                repository.insert(diagramToInsert);
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
        ResponseEntity<CamundaProcessXml> responseEntity = requireNonNull(webClient
                        .get()
                        .uri("/process-definition/" + diagramId + "/xml")
                        .retrieve()
                        .toEntity(CamundaProcessXml.class)
                        .block()
                , "Response Entity is null");

        return requireNonNull(responseEntity.getBody(), ERROR_MESSAGE).xml();
    }
}
