package neuefische.capstone.bpmndiagram;

import lombok.With;
import neuefische.capstone.comment.Comment;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@With
public record BpmnDiagram(
        String id,
        @NotEmpty
        String name,
        @NotEmpty
        String businessKey,
        @NotEmpty
        String filename,
        @NotNull
        int version,
        List<Comment> comments,
        boolean customDiagram
) {
}
