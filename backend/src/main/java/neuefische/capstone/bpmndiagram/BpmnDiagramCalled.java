package neuefische.capstone.bpmndiagram;

import java.util.List;

public record BpmnDiagramCalled(
        String calledDiagramId,
        List<String> calledFromActivities
) {
}
