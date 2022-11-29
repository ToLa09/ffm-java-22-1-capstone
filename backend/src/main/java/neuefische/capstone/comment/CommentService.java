package neuefische.capstone.comment;

import lombok.RequiredArgsConstructor;
import neuefische.capstone.ServiceUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repository;
    private final ServiceUtils serviceUtils;

    public List<Comment> getCommentsByDiagramId(String diagramId) {
        return repository.findAllByDiagramId(diagramId);
    }

    public Comment addComment(Comment newComment) {
        Comment newCommentWithId = newComment
                .withId(serviceUtils.generateUUID())
                .withTime(LocalDateTime.now());
        return repository.insert(newCommentWithId);
    }

    public void deleteCommentById(String id) {
        repository.deleteById(id);
    }

    public void deleteCommentsByDiagramId(String diagramId) {
        List<Comment> commentsToDelete = repository.findAllByDiagramId(diagramId);
        repository.deleteAll(commentsToDelete);
    }
}
