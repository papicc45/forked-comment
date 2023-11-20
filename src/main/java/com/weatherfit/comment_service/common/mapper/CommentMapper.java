package com.weatherfit.comment_service.common.mapper;

import com.weatherfit.comment_service.dto.CommentRepsonseDTO;
import com.weatherfit.comment_service.dto.CommentRequestDTO;
import com.weatherfit.comment_service.dto.ReplyRequestDTO;
import com.weatherfit.comment_service.dto.ReplyResponseDTO;
import com.weatherfit.comment_service.entity.Comment;
import com.weatherfit.comment_service.entity.Reply;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "replyList", target = "replyList")
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "modifiedDate", ignore = true)
    CommentRepsonseDTO commentToDTO(Comment comment);
    List<CommentRepsonseDTO> commentsToDTOList(List<Comment> comments);
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "modifiedDate", ignore = true)
    ReplyResponseDTO replyToDTO(Reply reply);
    List<ReplyResponseDTO> repliesToDTOList(List<Reply> replies);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "replyList", ignore = true)
    Comment DTOToComment(CommentRequestDTO commentRequestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "comment.id", source = "commentId")
    Reply DTOToReply(ReplyRequestDTO replyRequestDTO);

}

