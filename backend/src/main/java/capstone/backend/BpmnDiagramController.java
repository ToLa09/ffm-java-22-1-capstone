package capstone.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bpmndiagrams")
@RequiredArgsConstructor
public class BpmnDiagramController {

    private final BpmnDiagramService service;

    @GetMapping
    List<BpmnDiagram> getAllDiagrams() {
        return service.getAllDiagrams();
    }

    @PostMapping
    public BpmnDiagram addBpmnDiagram(@RequestBody BpmnDiagram bpmnDiagram) {
        return service.addBpmnDiagram(bpmnDiagram);
    }
}
