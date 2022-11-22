package neuefische.capstone.camunda;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class CamundaService {

    private final WebClient webClient;

    public CamundaService(){
        webClient = WebClient.create("https://api.github.com/repos/tola09/ffm-java-22-1-camunda-engine/git/trees/main");
    }

    public List<CamundaProcessModel> fetchBpmnDiagramsFromRepo(){
        ResponseEntity<List<CamundaProcessModel>> responseEntity = webClient.get()
                .uri("?recursive=1")
                .retrieve()
                .toEntityList(CamundaProcessModel.class)
                .block();

        if (responseEntity != null) {
            return responseEntity.getBody();
        } else throw new GithubResponseException("Response Entity is null");
    }
}
