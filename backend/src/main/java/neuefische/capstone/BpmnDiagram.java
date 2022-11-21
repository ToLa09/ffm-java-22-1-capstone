package neuefische.capstone;

import lombok.With;

import javax.validation.constraints.NotEmpty;

@With
public record BpmnDiagram(
        String id,
        @NotEmpty
        String name,
        @NotEmpty
        String businessKey,
        @NotEmpty
        String xmlFile,
        String comment
//        Instant timestamp,
//        String author,
//        int revision,
//        String status,
//        parents,
//        childs,
) {
}
