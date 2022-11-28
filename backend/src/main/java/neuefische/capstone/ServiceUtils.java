package neuefische.capstone;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ServiceUtils {

    public String generateCamundaId(String key, int version) {
        return key + ":" + version + ":" + UUID.randomUUID();
    }

    public String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
