package neuefische.capstone.bpmndiagram;

import lombok.RequiredArgsConstructor;
import neuefische.capstone.ServiceUtils;
import neuefische.capstone.comment.Comment;
import neuefische.capstone.comment.CommentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BpmnDiagramService {

    private final BpmnDiagramRepository repository;
    private final ServiceUtils serviceUtils;

    private final CommentService commentService;

    public List<BpmnDiagram> getAllDiagrams() {
        return repository.findAll();
    }

    public List<BpmnDiagram> getLatestDiagrams() {
        List<BpmnDiagram> allDiagrams = repository.findAll();
        List<BpmnDiagram> latestDiagrams = new ArrayList<>();

        for (BpmnDiagram diagram : allDiagrams) {
            BpmnDiagram latestVersion = repository.findAllByBusinessKey(diagram.businessKey()).stream()
                    .max(Comparator.comparing(BpmnDiagram::version))
                    .orElseThrow(() -> new NoSuchElementException("No max Version"));
            latestDiagrams.add(latestVersion);
        }
        return latestDiagrams.stream().distinct().toList();
    }

    public List<BpmnDiagram> getHistoryByKey(String key) {
        List<BpmnDiagram> history = new ArrayList<>(repository.findAllByBusinessKey(key));
        history.sort((diagram1, diagram2) -> {
            if (diagram1.version() > diagram2.version()) {
                return -1;
            } else return 0;
        });
        history.remove(0);
        return history;
    }

    public BpmnDiagram addBpmnDiagram(BpmnDiagram newBpmnDiagram) {
        String id = serviceUtils.generateCamundaId(newBpmnDiagram.businessKey(), newBpmnDiagram.version());
        BpmnDiagram bpmnDiagramWithId = newBpmnDiagram.withId(id).withCustomDiagram(true);
        return repository.insert(bpmnDiagramWithId);
    }

    public BpmnDiagram updateBpmnDiagram(BpmnDiagram updatedBpmnDiagram) {
        if (!repository.existsById(updatedBpmnDiagram.id())) {
            throw new NoSuchElementException("No Element found with this ID");
        }
        return repository.save(updatedBpmnDiagram);
    }

    public void deleteBpmnDiagram(String id) {
        BpmnDiagram diagramToDelete = repository.findById(id).orElseThrow(() -> new NoSuchElementException("No Element found with this ID"));
        if (!diagramToDelete.customDiagram()) {
            throw new DeleteNotAllowedException("Object can't be deleted because it is synched with Camunda Engine");
        }
        commentService.deleteCommentsByDiagramId(diagramToDelete.id());
        repository.deleteById(id);
    }

    public List<Comment> getCommentsByDiagramId(String id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("No Diagram with this Id found"))
                .comments();
    }

    public Comment addCommentToDiagram(String diagramId, Comment newComment) {
        Comment newCommentWithIdAndTime = newComment.withId(serviceUtils.generateUUID()).withTime(LocalDateTime.now());

        BpmnDiagram currentDiagram = repository.findById(diagramId).orElseThrow(() -> new NoSuchElementException("No diagram found with this ID"));
        List<Comment> commentList = currentDiagram.comments();
        commentList.add(newCommentWithIdAndTime);
        BpmnDiagram diagramWithNewComment = currentDiagram.withComments(commentList);

        repository.save(diagramWithNewComment);

        return newCommentWithIdAndTime;
    }

    public void deleteComment(String diagramId, String commentId) {
        BpmnDiagram currentDiagram = repository.findById(diagramId).orElseThrow(() -> new NoSuchElementException("No diagram found with this ID"));
        List<Comment> commentList = currentDiagram.comments();
        for (Comment comment : commentList) {
            if (comment.id().equals(commentId)) {
                commentList.remove(comment);
                BpmnDiagram diagramWithoutCommentToDelete = currentDiagram.withComments(commentList);
                repository.save(diagramWithoutCommentToDelete);
                return;
            }
        }
        throw new NoSuchElementException("No comment found with this Id to this diagramId");
    }
}
