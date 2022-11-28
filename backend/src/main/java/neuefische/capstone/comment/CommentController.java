package neuefische.capstone.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    Comment addComment(@RequestBody Comment newComment) {
        return service.addComment(newComment);
    }

    @DeleteMapping("/{id}")
    void deleteCommentById(@PathVariable String id) {
        service.deleteCommentById(id);
    }
}
