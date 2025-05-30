package com.weatherfit.comment_service.reply;

import com.weatherfit.comment_service.common.config.SecurityConfig;
import com.weatherfit.comment_service.reply.controller.ReplyController;
import com.weatherfit.comment_service.reply.dto.ReplyRequestDTO;
import com.weatherfit.comment_service.reply.dto.ReplyResponseDTO;
import com.weatherfit.comment_service.reply.service.ReplyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebFluxTest(controllers = ReplyController.class)
@Import(SecurityConfig.class)
public class ReplyControllerTest {

    @MockBean
    private ReplyService replyService;

    @Autowired
    private WebTestClient webClient;

    @Test
    void writeReplyTest() {

        ReplyRequestDTO req = new ReplyRequestDTO();
        req.setCommentId(2L);
        req.setNickname("이동준");
        req.setContent("대댓글");

        ReplyResponseDTO resp = new ReplyResponseDTO();
        resp.setId(1L);
        resp.setNickname("이동준");
        resp.setCommentId(2L);
        resp.setContent("대댓글");
        resp.setStatus(1);

        given(replyService.writeReply(any(ReplyRequestDTO.class)))
                .willReturn(Mono.just(resp));

        webClient.post()
                .uri("/reply")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ReplyResponseDTO.class)
                .value(body -> {
                    assert body.getCommentId().equals(req.getCommentId());
                    assert body.getContent().equals(req.getContent());
                    assert body.getNickname().equals(req.getNickname());
                });
    }
}
