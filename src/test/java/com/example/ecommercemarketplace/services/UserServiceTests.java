package com.example.ecommercemarketplace.services;


import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.exceptions.UserAlreadyExistsException;
import com.example.ecommercemarketplace.exceptions.UserNotFoundException;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.EmailConfirmationToken;
import com.example.ecommercemarketplace.models.UserEntity;
import com.example.ecommercemarketplace.repositories.UserRepository;
import com.example.ecommercemarketplace.services.impls.EmailConfirmationTokenServiceImpl;
import com.example.ecommercemarketplace.services.impls.UserServiceImpl;
import com.example.ecommercemarketplace.utils.PublicIdGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Mapper<UserEntity, UserDto> userMapper;

    @Mock
    private PublicIdGenerator publicIdGenerator;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private EmailConfirmationTokenServiceImpl tokenService;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity dummyUser1;
    private UserEntity dummyUser2;
    private UserDto expected;

    @BeforeEach
    public void prepare() {
        dummyUser1 = UserEntity.builder()
                .publicId("somePublicId")
                .firstName("Vitaliy")
                .lastName("Smith")
                .email("test@gmail.com")
                .phoneNumber("+123456789")
                .password("password")
                .build();

        dummyUser2 = UserEntity.builder()
                .publicId("somePublicId2")
                .firstName("Nikolay")
                .lastName("John")
                .email("example@gmail.com")
                .phoneNumber("+123456788")
                .password("1234")
                .build();

        expected = UserDto.builder()
                .publicId("somePublicId")
                .firstName("Vitaliy")
                .lastName("Smith")
                .email("test@gmail.com")
                .phoneNumber("+123456789")
                .password("password")
                .build();

    }

    @AfterEach
    public void cleanUp() {
        dummyUser1 = dummyUser2 = null;
        expected = null;
    }

    @Test
    public void UserService_CreateUser_ShouldReturnSavedUser() {
        when(userMapper.mapTo(dummyUser1)).thenReturn(expected);
        when(userMapper.mapFrom(expected)).thenReturn(dummyUser1);
        when(passwordEncoder.encode(anyString())).thenReturn("password");
        when(publicIdGenerator.generate()).thenReturn("somePublicId");
        when(userRepository.save(dummyUser1)).thenReturn(dummyUser1);

        UserDto actual = userService.createUser(expected);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    public void UserService_CreateUser_ShouldThrowExceptionWhenUserWithEmailAlreadyExists() {
        String email = "test@gmail.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> userService.createUser(expected));
    }

    @Test
    public void UserService_FindByEmail_ShouldReturnCorrectUser() {
        String email = expected.getEmail();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(dummyUser1));
        when(userMapper.mapTo(dummyUser1)).thenReturn(expected);

        UserDto actual = userService.findByEmail(email);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    public void UserService_FindByEmail_ShouldThrowExceptionIfUserNotFound() {
        String email = "no@gmail.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(email));
    }

    @Test
    public void UserService_ExistsByEmail_ShouldReturnTrueOnUserExistence() {
        String email = dummyUser1.getEmail();

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        boolean actual = userService.existsByEmail(email);

        assertThat(actual).isTrue();
    }

    @Test
    public void UserService_ExistsByEmail_ShouldReturnFalseIfUserNotExist() {
        String email = dummyUser1.getEmail();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        boolean actual = userService.existsByEmail(email);

        assertThat(actual).isFalse();
    }

    @Test
    public void UserService_FindByEmailConfirmationToken_ShouldReturnCorrectUser() {
        EmailConfirmationToken token = EmailConfirmationToken.builder()
                .token("test")
                .build();

        when(tokenService.findByToken(token.getToken())).thenReturn(token);
        when(userRepository.findByEmailConfirmationToken(token)).thenReturn(Optional.of(dummyUser1));
        when(userMapper.mapTo(dummyUser1)).thenReturn(expected);

        UserDto actual = userService.findByEmailConfirmationToken(token.getToken());

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    public void UserService_FindByEmailConfirmationToken_ShouldThrowExceptionIfWithThatTokenNotExist() {
        EmailConfirmationToken token = EmailConfirmationToken.builder()
                .token("test")
                .build();

        when(tokenService.findByToken(token.getToken())).thenReturn(token);
        when(userRepository.findByEmailConfirmationToken(token)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.findByEmailConfirmationToken(token.getToken()));
    }

    @Test
    public void UserService_UpdateUser_ShouldReturnCorrectUpdatedUser() {
        when(userMapper.mapFrom(expected)).thenReturn(dummyUser1);
        when(userMapper.mapTo(dummyUser1)).thenReturn(expected);
        when(userRepository.save(dummyUser1)).thenReturn(dummyUser1);
        when(userRepository.existsByPublicId(anyString())).thenReturn(true);

        UserDto actual = userService.updateUser(expected);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    public void UserService_FindUserByPublicId_ShouldReturnCorrectUser() {
        String publicId = expected.getPublicId();

        when(userMapper.mapTo(dummyUser1)).thenReturn(expected);
        when(userRepository.findByPublicId(publicId)).thenReturn(Optional.of(dummyUser1));

        UserDto actual = userService.findUserByPublicId(publicId);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    public void UserService_FindUserByPublicId_ShouldThrowExceptionIfUserNotExist() {
        String publicId = expected.getPublicId();

        when(userRepository.findByPublicId(publicId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.findUserByPublicId(publicId));
    }

    @Test
    public void UserService_FindAllUsers_ShouldReturnPageOfUsers() {
        List<UserEntity> listOfUsers = Collections.singletonList(dummyUser1);
        Page<UserEntity> pageOfUsers = new PageImpl<>(listOfUsers);

        when(userRepository.findAll(any(Pageable.class))).thenReturn(pageOfUsers);
        when(userMapper.mapTo(dummyUser1)).thenReturn(expected);

        List<UserDto> expectedUsers = Collections.singletonList(expected);
        Page<UserDto> page = userService.findAllUsers(mock(Pageable.class));

        assertThat(page).containsExactly(expected);
    }

    @Test
    public void UserService_RemoveUserByPublicId_ShouldRemoveUser() {
        when(userRepository.existsByPublicId(anyString())).thenReturn(true);
        userService.removeUserByPublicId(expected.getPublicId());

        verify(userRepository, times(1)).deleteByPublicId(expected.getPublicId());
    }

    @Test
    public void UserService_RemoveUserByPublicId_ShouldThrowExceptionIfUserNotExist() {
        assertThrows(UserNotFoundException.class,
                () -> userService.removeUserByPublicId(expected.getPublicId()));
    }

    @Test
    public void UserService_UpdateUserFully_ShouldReturnUpdatedUser() {
        String publicId = expected.getPublicId();

        when(userMapper.mapTo(dummyUser1)).thenReturn(expected);
        when(userRepository.save(dummyUser1)).thenReturn(dummyUser1);
        when(userRepository.findByPublicId(anyString())).thenReturn(Optional.of(dummyUser1));

        UserDto actual = userService.updateUserFully(publicId, expected);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void UserService_UpdateUserFully_ShouldThrowExceptionIfUserNotExist() {
        String publicId = expected.getPublicId();

        when(userRepository.findByPublicId(publicId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.updateUserFully(publicId, expected));
    }

    @Test
    public void UserService_UpdateUserPatch_ShouldReturnUpdatedUser() {
        String publicId = expected.getPublicId();

        when(userMapper.mapTo(dummyUser1)).thenReturn(expected);
        when(userRepository.existsByPublicId(anyString())).thenReturn(true);
        when(userRepository.save(dummyUser1)).thenReturn(dummyUser1);
        when(userRepository.findByPublicId(anyString())).thenReturn(Optional.of(dummyUser1));

        UserDto actual = userService.updateUserPatch(publicId, expected);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void UserService_UpdateUserPatch_ShouldThrowExceptionIfUserNotExist() {
        String publicId = expected.getPublicId();

        assertThrows(UserNotFoundException.class,
                () -> userService.updateUserFully(publicId, expected));
    }
}
