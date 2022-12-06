package neuefische.capstone.camunda;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/camundaprocesses")
@RequiredArgsConstructor
public class CamundaController {

    private final CamundaService service;

    @PostMapping()
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void writeCamundaProcessesToDB() {
        try {
            service.writeCamundaProcessesToDB();
        } catch (CamundaResponseException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{diagramId}/xml")
    public BpmnDiagramXML getXmlFileByDiagramId(@PathVariable String diagramId) {
        return service.getXmlFileByDiagramId(diagramId);
    }
}
