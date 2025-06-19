package com.weatherfit.comment_service.reply.mapper;

import com.weatherfit.comment_service.common.util.DateTimeFormatterUtil;
import com.weatherfit.comment_service.reply.dto.ReplyRequestDTO;
import com.weatherfit.comment_service.reply.dto.ReplyResponseDTO;
import com.weatherfit.comment_service.reply.dto.ReplyResponseDTO.ReplyResponseDTOBuilder;
import com.weatherfit.comment_service.reply.entity.Reply;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-19T17:49:33+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class ReplyMapperImpl implements ReplyMapper {

    @Autowired
    private DateTimeFormatterUtil dateTimeFormatterUtil;

    @Override
    public ReplyResponseDTO replyToDTO(Reply reply) {
        if ( reply == null ) {
            return null;
        }

        ReplyResponseDTOBuilder replyResponseDTO = ReplyResponseDTO.builder();

        replyResponseDTO.createdDate( dateTimeFormatterUtil.formatDate( reply.getCreatedDate() ) );
        replyResponseDTO.modifiedDate( dateTimeFormatterUtil.formatDate( reply.getModifiedDate() ) );
        replyResponseDTO.id( reply.getId() );
        replyResponseDTO.nickname( reply.getNickname() );
        replyResponseDTO.content( reply.getContent() );
        replyResponseDTO.status( reply.getStatus() );
        if ( reply.getCommentId() != null ) {
            replyResponseDTO.commentId( reply.getCommentId().longValue() );
        }

        return replyResponseDTO.build();
    }

    @Override
    public List<ReplyResponseDTO> replyListToDTOs(List<Reply> replies) {
        if ( replies == null ) {
            return null;
        }

        List<ReplyResponseDTO> list = new ArrayList<ReplyResponseDTO>( replies.size() );
        for ( Reply reply : replies ) {
            list.add( replyToDTO( reply ) );
        }

        return list;
    }

    @Override
    public Reply DTOToReply(ReplyRequestDTO replyRequestDTO) {
        if ( replyRequestDTO == null ) {
            return null;
        }

        Reply reply = new Reply();

        reply.setNickname( replyRequestDTO.getNickname() );
        reply.setContent( replyRequestDTO.getContent() );
        if ( replyRequestDTO.getCommentId() != null ) {
            reply.setCommentId( replyRequestDTO.getCommentId().intValue() );
        }

        return reply;
    }
}
