package com.weatherfit.comment_service.user.mapper;


import ch.qos.logback.core.model.ComponentModel;
import com.weatherfit.comment_service.common.util.DateTimeFormatterUtil;
import com.weatherfit.comment_service.user.dto.UserRequestDTO;
import com.weatherfit.comment_service.user.dto.UserResponseDTO;
import com.weatherfit.comment_service.user.entity.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = DateTimeFormatterUtil.class)
public interface UserMapper {
    @Mapping(source = "createdDate", target = "createdDate", qualifiedByName = "dateTimeFormatting")
    @Mapping(source = "modifiedDate", target = "modifiedDate", qualifiedByName = "dateTimeFormatting")
    UserResponseDTO userToDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    User DTOToUser(UserRequestDTO userRequestDTO);
}
