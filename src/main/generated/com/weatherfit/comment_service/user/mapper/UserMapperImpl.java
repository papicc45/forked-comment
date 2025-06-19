package com.weatherfit.comment_service.user.mapper;

import com.weatherfit.comment_service.common.util.DateTimeFormatterUtil;
import com.weatherfit.comment_service.user.dto.UserRequestDTO;
import com.weatherfit.comment_service.user.dto.UserResponseDTO;
import com.weatherfit.comment_service.user.dto.UserResponseDTO.UserResponseDTOBuilder;
import com.weatherfit.comment_service.user.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-19T17:49:33+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private DateTimeFormatterUtil dateTimeFormatterUtil;

    @Override
    public UserResponseDTO userToDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDTOBuilder userResponseDTO = UserResponseDTO.builder();

        userResponseDTO.createdDate( dateTimeFormatterUtil.formatDate( user.getCreatedDate() ) );
        userResponseDTO.modifiedDate( dateTimeFormatterUtil.formatDate( user.getModifiedDate() ) );
        userResponseDTO.id( user.getId() );
        userResponseDTO.userId( user.getUserId() );
        userResponseDTO.nickname( user.getNickname() );

        return userResponseDTO.build();
    }

    @Override
    public User DTOToUser(UserRequestDTO userRequestDTO) {
        if ( userRequestDTO == null ) {
            return null;
        }

        User user = new User();

        user.setUserId( userRequestDTO.getUserId() );
        user.setNickname( userRequestDTO.getNickname() );
        user.setEmail( userRequestDTO.getEmail() );
        user.setPhone( userRequestDTO.getPhone() );

        return user;
    }
}
