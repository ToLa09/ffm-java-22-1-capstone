package neuefische.capstone.bpmndiagram;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BpmnDiagramCalled(
        @JsonProperty("id") String calledDiagramId,
        List<String> calledFromActivityIds
) {
}
