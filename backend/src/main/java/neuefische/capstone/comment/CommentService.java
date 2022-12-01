package neuefische.capstone.comment;

import lombok.RequiredArgsConstructor;
import neuefische.capstone.ServiceUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
                .withTime(serviceUtils.generateCurrentTime());
        return repository.insert(newCommentWithId);
    }

    public void deleteCommentById(String id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("No Element found with this ID");
        }
        repository.deleteById(id);
    }

    public void deleteCommentsByDiagramId(String diagramId) {
        List<Comment> commentsToDelete = repository.findAllByDiagramId(diagramId);
        repository.deleteAll(commentsToDelete);
    }
}
