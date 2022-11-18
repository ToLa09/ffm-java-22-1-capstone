package neuefische.capstone;

import lombok.With;

@With
public record BpmnDiagram(
        String id,
        String name,
        String businessKey,
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
