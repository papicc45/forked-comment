package com.weatherfit.comment_service.reply.mapper;

import com.weatherfit.comment_service.reply.dto.ReplyRequestDTO;
import com.weatherfit.comment_service.reply.dto.ReplyResponseDTO;
import com.weatherfit.comment_service.reply.entity.Reply;
import org.mapstruct.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ReplyMapper {
    @Mapping(target = "createdTime", ignore = true)
    ReplyResponseDTO replyToDTO(Reply reply);

    @AfterMapping
    default void splitDateTime(Reply reply, @MappingTarget ReplyResponseDTO replyResponseDTO) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String createdDate = reply.getCreatedDate().format(dateFormatter);
        String createdTime = reply.getCreatedDate().format(timeFormatter);

        replyResponseDTO.setCreatedDate(createdDate);
        replyResponseDTO.setCreatedTime(createdTime);
    }
//    List<ReplyResponseDTO> repliesToDTOList(List<Reply> replies);
//
List<ReplyResponseDTO> replyListToDTOs(List<Reply> replies);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    Reply DTOToReply(ReplyRequestDTO replyRequestDTO);
}
