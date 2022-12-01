package neuefische.capstone.bpmndiagram;

import lombok.With;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@With
public record Comment(
        String id,
        @NotEmpty
        String content,
        @NotEmpty
        String author,
        LocalDateTime time
) {
}
