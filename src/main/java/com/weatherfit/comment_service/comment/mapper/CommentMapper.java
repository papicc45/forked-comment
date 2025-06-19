package com.weatherfit.comment_service.comment.mapper;

import com.weatherfit.comment_service.comment.dto.CommentRequestDTO;
import com.weatherfit.comment_service.comment.dto.CommentResponseDTO;
//import com.weatherfit.comment_service.entity.Reply;
import com.weatherfit.comment_service.comment.entity.Comment;
import com.weatherfit.comment_service.common.util.DateTimeFormatterUtil;
import com.weatherfit.comment_service.reply.dto.ReplyResponseDTO;
import com.weatherfit.comment_service.reply.entity.Reply;
import org.mapstruct.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring", uses = DateTimeFormatterUtil.class)
//@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {ReplyMapper.class})
public interface CommentMapper {

    @Mapping(source = "createdDate", target = "createdDate", qualifiedByName = "dateTimeFormatting")
    @Mapping(source = "modifiedDate", target = "modifiedDate", qualifiedByName = "dateTimeFormatting")

    CommentResponseDTO commentToDTO(Comment comment);

//    @Mapping(target = "createdTime", ignore = true)
//    ReplyResponseDTO replyToDTO(Reply reply);
//    @AfterMapping
//    default void splitDateTime(Comment comment, @MappingTarget CommentResponseDTO commentRepsonseDTO) {
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//
//        String createdDate = comment.getCreatedDate().format(dateFormatter);
//        String createdTime = comment.getCreatedDate().format(timeFormatter);
//
//        commentRepsonseDTO.setCreatedDate(createdDate);
//        commentRepsonseDTO.setCreatedTime(createdTime);
//    }
    List<CommentResponseDTO> commentsToDTOList(List<Comment> comments);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "id", ignore = true)
    Comment DTOToComment(CommentRequestDTO commentRequestDTO);
}

