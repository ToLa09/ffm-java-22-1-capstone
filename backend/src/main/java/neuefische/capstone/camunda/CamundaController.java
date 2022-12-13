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
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping(path = "/{diagramId}/xml", produces = "application/xml")
    public String getXmlFileByDiagramId(@PathVariable String diagramId) {
        return service.getXmlFileByDiagramId(diagramId);
    }
}
