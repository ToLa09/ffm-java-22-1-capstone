package neuefische.capstone.comment;

import neuefische.capstone.ServiceUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommentServiceTest {
    private final CommentRepository repository = mock(CommentRepository.class);
    private final ServiceUtils serviceUtils = mock(ServiceUtils.class);
    private final CommentService service = new CommentService(repository, serviceUtils);

    @Test
    void getCommentsByDiagramId() {
        //given
        String diagramId = "1";
        when(repository.findAllByDiagramId(diagramId)).thenReturn(List.of());
        //when
        List<Comment> actual = service.getCommentsByDiagramId(diagramId);
        //then
        assertEquals(List.of(), actual);
        verify(repository).findAllByDiagramId(diagramId);
    }

    @Test
    void addComment() {
        //given
        Comment testComment = new Comment(
                null,
                "test",
                "author",
                null,
                "123"
        );
        Comment testCommentWithIdAndDate = testComment.withId("1").withTime(LocalDateTime.of(2022, 11, 30, 1, 1));
        when(repository.insert(testCommentWithIdAndDate)).thenReturn(testCommentWithIdAndDate);
        when(serviceUtils.generateUUID()).thenReturn("1");
        when(serviceUtils.generateCurrentTime()).thenReturn(LocalDateTime.of(2022, 11, 30, 1, 1));
        //when
        Comment actual = service.addComment(testComment);
        //then
        assertEquals(testCommentWithIdAndDate, actual);
        verify(repository).insert(testCommentWithIdAndDate);
    }

    @Test
    void deleteCommentById() {
        //given
        String id = "1";
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(id);
        //when
        service.deleteCommentById(id);
        //then
        verify(repository).deleteById(id);
        verify(repository).existsById(id);
    }

    @Test
    void deleteCommentsByDiagramId() {
        //given
        String id = "1";
        when(repository.findAllByDiagramId(id)).thenReturn(List.of());
        doNothing().when(repository).deleteAll(List.of());
        //when
        service.deleteCommentsByDiagramId(id);
        //then
        verify(repository).deleteAll(List.of());
        verify(repository).findAllByDiagramId(id);
    }
}