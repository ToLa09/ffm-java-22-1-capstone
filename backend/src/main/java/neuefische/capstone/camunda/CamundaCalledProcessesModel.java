package neuefische.capstone.camunda;

import java.util.List;

public record CamundaCalledProcessesModel(
        String id,
        String key,
        String name,
        String resource,
        int version,
        List<String> calledFromActivityIds
) {
}
