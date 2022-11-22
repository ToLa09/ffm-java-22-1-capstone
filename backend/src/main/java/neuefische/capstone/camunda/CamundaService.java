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

    public List<BpmnDiagramRepoModel> fetchBpmnDiagramsFromRepo(){
        ResponseEntity<RepoResponseElement> responseEntity = webClient.get()
                .uri("?recursive=1")
                .retrieve()
                .toEntity(RepoResponseElement.class)
                .block();

        RepoResponseElement responseBody;

        if (responseEntity != null){
            responseBody = responseEntity.getBody();
        } else throw new GithubResponseException("Response Entity is null");
        if (responseBody != null) {
            return responseBody.tree()
                    .stream()
                    .filter(diagram -> diagram.path().endsWith(".bpmn"))
                    .toList();
        } else throw new GithubResponseException("Response Body is null");
    }
}
