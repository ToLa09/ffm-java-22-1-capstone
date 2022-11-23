package neuefische.capstone.camunda;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/camundaprocesses")
@RequiredArgsConstructor
public class CamundaController {

    private final CamundaService service;

    @GetMapping()
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void writeCamundaProcessesToDB(){
        service.writeCamundaProcessesToDB();
    }
}
