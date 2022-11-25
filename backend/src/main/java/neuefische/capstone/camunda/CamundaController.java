package neuefische.capstone.camunda;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/camundaprocesses")
@RequiredArgsConstructor
public class CamundaController {

    private final CamundaService service;

    @PostMapping()
    public ResponseEntity<String> writeCamundaProcessesToDB() {
        try {
            service.writeCamundaProcessesToDB();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CamundaResponseException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
