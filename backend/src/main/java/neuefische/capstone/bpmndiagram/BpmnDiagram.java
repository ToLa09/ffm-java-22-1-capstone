package neuefische.capstone.bpmndiagram;

import lombok.With;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
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
        @NotEmpty
        int version,
        List<BpmnDiagram> calledProcesses,
        String commentText,
        LocalDateTime commentTime,
        String commentAuthor
) {
}
