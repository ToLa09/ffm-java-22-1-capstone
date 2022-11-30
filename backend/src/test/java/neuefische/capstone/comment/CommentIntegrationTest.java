package neuefische.capstone.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DirtiesContext
    void POSTCommentAndGETByDiagamId_expect201and200() throws Exception {
        String responseBody = mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "content": "test comment",
                                    "author": "testauthor",
                                    "diagramId": "123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Comment responseObject = objectMapper.readValue(responseBody, Comment.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(responseObject.id()))
                .andExpect(jsonPath("$.[0].content").value("test comment"))
                .andExpect(jsonPath("$.[0].author").value("testauthor"))
                .andExpect(jsonPath("$.[0].diagramId").value("123"));
    }

    @Test
    @DirtiesContext
    void DELETEComment_expect204() throws Exception {
        String responseBody = mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "content": "test comment",
                                    "author": "testauthor",
                                    "diagramId": "123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Comment responseObject = objectMapper.readValue(responseBody, Comment.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/" + responseObject.id()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DirtiesContext
    void DELETEComment_expect404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/123"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("No Element found with this ID"));
    }
}