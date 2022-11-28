package neuefische.capstone.comment;

import lombok.With;

import java.time.LocalDateTime;

@With
public record Comment(
        String id,
        String content,
        String author,
        LocalDateTime time,
        String diagramId
) {
}
