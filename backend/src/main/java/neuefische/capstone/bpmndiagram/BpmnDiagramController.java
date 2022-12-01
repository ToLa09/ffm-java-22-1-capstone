package neuefische.capstone.bpmndiagram;

import lombok.RequiredArgsConstructor;
import neuefische.capstone.comment.Comment;
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
    List<BpmnDiagram> getAllDiagrams(@RequestParam Optional<Boolean> onlylatestversions) {
        if (onlylatestversions.orElse(false)) {
            return service.getLatestDiagrams();
        } else {
            return service.getAllDiagrams();
        }
    }

    @GetMapping("/{key}/history")
    List<BpmnDiagram> getHistoryOfDiagram(@PathVariable String key) {
        return service.getHistoryByKey(key);
    }

    @GetMapping("/{diagramId}/comments")
    List<Comment> getCommentsByDiagramId(@PathVariable String diagramId) {
        try {
            return service.getCommentsByDiagramId(diagramId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    BpmnDiagram addBpmnDiagram(@RequestBody @Valid BpmnDiagram newBpmnDiagram) {
        return service.addBpmnDiagram(newBpmnDiagram);
    }

    @PostMapping("/{diagramId}/comments")
    Comment addCommentToDiagram(@PathVariable String diagramId, @RequestBody Comment newComment) {
        try {
            return service.addCommentToDiagram(diagramId, newComment);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{diagramId}")
    BpmnDiagram updateBpmnDiagram(@RequestBody @Valid BpmnDiagram updatedBpmnDiagram, @PathVariable String diagramId) {
        if (!updatedBpmnDiagram.id().equals(diagramId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request ID Mismatch");
        }
        try {
            return service.updateBpmnDiagram(updatedBpmnDiagram);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{diagramId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void deleteBpmnDiagram(@PathVariable String diagramId) {
        try {
            service.deleteBpmnDiagram(diagramId);
        } catch (DeleteNotAllowedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{diagramId}/comments/{commentId}")
    void deleteCommentFromDiagram(@PathVariable String diagramId, @PathVariable String commentId) {
        try {
            service.deleteComment(diagramId, commentId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
