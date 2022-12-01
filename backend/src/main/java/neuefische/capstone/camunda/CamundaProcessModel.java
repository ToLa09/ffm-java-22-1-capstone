package neuefische.capstone.camunda;

public record CamundaProcessModel(
        String id,
        String key,
        String name,
        String resource,
        int version,
        boolean startableInTasklist
) {
}
