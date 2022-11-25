package neuefische.capstone.bpmndiagram;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/bpmndiagrams")
@RequiredArgsConstructor
public class BpmnDiagramController {

    private final BpmnDiagramService service;

    @GetMapping
    List<BpmnDiagram> getAllDiagrams(@RequestParam Optional<Boolean> distinct) {
        if (distinct.orElse(false)) {
            return service.getLatestDiagrams();
        } else {
            return service.getAllDiagrams();
        }
    }

    @GetMapping("/history/{key}")
    List<BpmnDiagram> getHistoryOfDiagram(@PathVariable String key) {
        return service.getHistoryByKey(key);
    }


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public BpmnDiagram addBpmnDiagram(@RequestBody @Valid BpmnDiagram newBpmnDiagram) {
        return service.addBpmnDiagram(newBpmnDiagram);
    }

    @PutMapping("/{id}")
    public BpmnDiagram updateBpmnDiagram(@RequestBody @Valid BpmnDiagram updatedBpmnDiagram, @PathVariable String id) {
        if(updatedBpmnDiagram.id().equals(id)) {
            try {
                return service.updateBpmnDiagram(updatedBpmnDiagram);
            } catch (NoSuchElementException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Request ID Mismatch");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteBpmnDiagram(@PathVariable String id) {
        try {
            service.deleteBpmnDiagram(id);
        } catch (DeleteNotAllowedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
