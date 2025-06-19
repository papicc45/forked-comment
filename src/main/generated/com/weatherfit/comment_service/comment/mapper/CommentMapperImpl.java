package com.weatherfit.comment_service.comment.mapper;

import com.weatherfit.comment_service.comment.dto.CommentRequestDTO;
import com.weatherfit.comment_service.comment.dto.CommentResponseDTO;
import com.weatherfit.comment_service.comment.entity.Comment;
import com.weatherfit.comment_service.common.util.DateTimeFormatterUtil;
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
public class CommentMapperImpl implements CommentMapper {

    @Autowired
    private DateTimeFormatterUtil dateTimeFormatterUtil;

    @Override
    public CommentResponseDTO commentToDTO(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();

        commentResponseDTO.setCreatedDate( dateTimeFormatterUtil.formatDate( comment.getCreatedDate() ) );
        commentResponseDTO.setModifiedDate( dateTimeFormatterUtil.formatDate( comment.getModifiedDate() ) );
        commentResponseDTO.setId( comment.getId() );
        if ( comment.getBoardId() != null ) {
            commentResponseDTO.setBoardId( comment.getBoardId().longValue() );
        }
        commentResponseDTO.setNickname( comment.getNickname() );
        commentResponseDTO.setContent( comment.getContent() );
        if ( comment.getStatus() != null ) {
            commentResponseDTO.setStatus( comment.getStatus() );
        }

        return commentResponseDTO;
    }

    @Override
    public List<CommentResponseDTO> commentsToDTOList(List<Comment> comments) {
        if ( comments == null ) {
            return null;
        }

        List<CommentResponseDTO> list = new ArrayList<CommentResponseDTO>( comments.size() );
        for ( Comment comment : comments ) {
            list.add( commentToDTO( comment ) );
        }

        return list;
    }

    @Override
    public Comment DTOToComment(CommentRequestDTO commentRequestDTO) {
        if ( commentRequestDTO == null ) {
            return null;
        }

        Comment comment = new Comment();

        if ( commentRequestDTO.getBoardId() != null ) {
            comment.setBoardId( commentRequestDTO.getBoardId().intValue() );
        }
        comment.setNickname( commentRequestDTO.getNickname() );
        comment.setContent( commentRequestDTO.getContent() );

        return comment;
    }
}
