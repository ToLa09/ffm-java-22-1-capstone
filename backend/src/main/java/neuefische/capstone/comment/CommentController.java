package neuefische.capstone.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;

    @GetMapping("/{diagramId}")
    List<Comment> getCommentsByDiagramId(@PathVariable String diagramId) {
        return service.getCommentsByDiagramId(diagramId);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    Comment addComment(@RequestBody @Valid Comment newComment) {
        return service.addComment(newComment);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void deleteCommentById(@PathVariable String id) {
        try {
            service.deleteCommentById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
