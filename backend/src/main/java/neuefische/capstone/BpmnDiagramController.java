package neuefische.capstone;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
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
    @ResponseStatus(code = HttpStatus.CREATED)
    public BpmnDiagram addBpmnDiagram(@RequestBody @Valid BpmnDiagram newBpmnDiagram) {
        return service.addBpmnDiagram(newBpmnDiagram);
    }

    @PutMapping("/{id}")
    public BpmnDiagram updateBpmnDiagram(@RequestBody @Valid BpmnDiagram updatedBpmnDiagram, @PathVariable String id) {
        if(updatedBpmnDiagram.id().equals(id)){
            return service.updateBpmnDiagram(updatedBpmnDiagram);
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Request ID Mismatch");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteBpmnDiagram(@PathVariable String id) {
        service.deleteBpmnDiagram(id);
    }
}
