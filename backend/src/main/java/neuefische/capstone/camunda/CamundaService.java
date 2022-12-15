package neuefische.capstone.camunda;

import neuefische.capstone.bpmndiagram.BpmnDiagram;
import neuefische.capstone.bpmndiagram.BpmnDiagramRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Service
public class CamundaService {

    private final WebClient webClient;

    private final BpmnDiagramRepository repository;

    private static final String BODY_IS_NULL_ERROR = "Response Body is null";
    private static final String ENTITY_IS_NULL_ERROR = "Response Entity is null";
    public static final String CAMUNDA_PROCESSES_ENDPOINT = "/process-definition/";

    public CamundaService(BpmnDiagramRepository repository, @Value("${camunda.api.baseUrl}") String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
        this.repository = repository;
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void writeCamundaProcessesToDB() {
        List<CamundaProcessModel> processList = fetchFromCamundaEngine(
                CAMUNDA_PROCESSES_ENDPOINT,
                new ParameterizedTypeReference<>() {
                }
        );

        for (CamundaProcessModel camundaProcessModel : processList) {
            repository.findById(camundaProcessModel.id())
                    .ifPresentOrElse(
                            diagram -> repository.save(diagram.withCalledDiagrams(fetchFromCamundaEngine(
                                    CAMUNDA_PROCESSES_ENDPOINT + camundaProcessModel.id() + "/static-called-process-definitions",
                                    new ParameterizedTypeReference<>() {
                                    }
                            )))
                            , () -> repository.insert(new BpmnDiagram(
                                    camundaProcessModel.id(),
                                    camundaProcessModel.name(),
                                    camundaProcessModel.key(),
                                    camundaProcessModel.resource(),
                                    camundaProcessModel.version(),
                                    Collections.emptyList(),
                                    fetchFromCamundaEngine(
                                            CAMUNDA_PROCESSES_ENDPOINT + camundaProcessModel.id() + "/static-called-process-definitions",
                                            new ParameterizedTypeReference<>() {
                                            }
                                    ),
                                    !camundaProcessModel.startableInTasklist(),
                                    false
                            ))
                    );
        }
        Set<String> processIds = processList.stream().map(CamundaProcessModel::id).collect(Collectors.toSet());
        repository.deleteByIdNotIn(processIds);
    }

    public String getXmlFileByDiagramId(String diagramId) {
        return fetchFromCamundaEngine(
                CAMUNDA_PROCESSES_ENDPOINT + diagramId + "/xml",
                new ParameterizedTypeReference<CamundaProcessXmlModel>() {
                }
        ).xml();
    }

    private <T> T fetchFromCamundaEngine(String uri, ParameterizedTypeReference<T> type) {
        ResponseEntity<T> responseEntity = requireNonNull(webClient
                        .get()
                        .uri(uri)
                        .retrieve()
                        .toEntity(type)
                        .block()
                , ENTITY_IS_NULL_ERROR);

        T responseBody = responseEntity.getBody();

        if (responseBody == null) {
            throw new CamundaResponseException(BODY_IS_NULL_ERROR);
        }
        return responseBody;
    }
}
