package neuefische.capstone.camunda;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/camundadiagrams")
@RequiredArgsConstructor
public class CamundaController {

    private final CamundaService service;

    @GetMapping()
    public List<CamundaProcessModel> getAllModels(){
        return service.fetchBpmnDiagramsFromRepo();
    }
}
