package neuefische.capstone.camunda;

import neuefische.capstone.bpmndiagram.BpmnDiagram;
import neuefische.capstone.bpmndiagram.BpmnDiagramRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class CamundaService {

    private final WebClient webClient;

    private final BpmnDiagramRepository repository;

    public CamundaService(BpmnDiagramRepository repository, @Value("${camunda.api.baseUrl}") String baseUrl){
        webClient = WebClient.create(baseUrl);
        this.repository=repository;
    }

    public void writeCamundaProcessesToDB(){
        ResponseEntity<List<CamundaProcessModel>> responseEntity = webClient.get()
                .uri("/process-definition")
                .retrieve()
                .toEntityList(CamundaProcessModel.class)
                .block();

        List<CamundaProcessModel> processList;

        if (responseEntity != null) {
            processList = responseEntity.getBody();
        } else throw new CamundaResponseException("Response Entity is null");
        if(processList != null){
            for(CamundaProcessModel camundaProcessModel : processList){
                BpmnDiagram diagramToInsert = new BpmnDiagram(
                        camundaProcessModel.id(),
                        camundaProcessModel.name(),
                        camundaProcessModel.key(),
                        camundaProcessModel.resource(),
                        camundaProcessModel.version(),
                        null,
                        null,
                        null,
                        null
                );
                if(!repository.existsById(camundaProcessModel.id())){
                    repository.insert(diagramToInsert);
                }
            }
        } else throw new CamundaResponseException("Response Body is null");
    }
}
