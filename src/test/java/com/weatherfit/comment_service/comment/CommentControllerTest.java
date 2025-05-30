package com.weatherfit.comment_service.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.weatherfit.comment_service.comment.controller.CommentController;
import com.weatherfit.comment_service.comment.dto.CommentRequestDTO;
import com.weatherfit.comment_service.comment.dto.CommentResponseDTO;
import com.weatherfit.comment_service.comment.mapper.CommentMapper;
import com.weatherfit.comment_service.comment.service.CommentService;
import com.weatherfit.comment_service.common.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebFluxTest(controllers = CommentController.class)
@Import(SecurityConfig.class)
public class CommentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CommentService commentService;

        //Spring MVC 전용 Mockmvc, objMapper
//    @Autowired
//    private MockMvc mockMvc;

//    @Autowired
//    private ObjectMapper objectMapper;

    @Test
    void createComment() {
        //given
        CommentRequestDTO req = new CommentRequestDTO();
        req.setBoardId(42L);
        req.setNickname("tester");
        req.setContent("내용");

        CommentResponseDTO resp = new CommentResponseDTO();
        resp.setId(100l);
        resp.setBoardId(42l);
        resp.setNickname("tester");
        resp.setContent("내용");
        resp.setStatus(1);

        // BDD
        given(commentService.writeComment(any(Mono.class)))
                .willReturn(Mono.just(resp));

            // TDD
//        when(commentService.writeComment(any(Mono.class)))
//                .thenReturn(Mono.just(resp));

        webTestClient.post()
                .uri("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CommentResponseDTO.class)
                .value(body -> {
                    assert body.getId().equals(resp.getId());
                    assert body.getBoardId().equals(resp.getBoardId());
                    assert body.getNickname().equals(resp.getNickname());
                    assert body.getContent().equals(resp.getContent());
                    assert body.getStatus() == resp.getStatus();
                });

            // Spring mvc 전용 test 코드
//        mockMvc.perform(post("/comments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(resp.getId()))
//                .andExpect(jsonPath("$.boardId").value(resp.getBoardId()))
//                .andExpect(jsonPath("$.nickname").value(resp.getNickname()))
//                .andExpect(jsonPath("$.content").value(resp.getContent()))
//                .andExpect(jsonPath("$.status").value(resp.getStatus()));
    }
}
