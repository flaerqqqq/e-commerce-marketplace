package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.exceptions.UserAlreadyExistsException;
import com.example.ecommercemarketplace.exceptions.UserNotFoundException;
import com.example.ecommercemarketplace.mappers.UserMapper;
import com.example.ecommercemarketplace.models.EmailConfirmationToken;
import com.example.ecommercemarketplace.models.LoginData;
import com.example.ecommercemarketplace.models.UserEntity;
import com.example.ecommercemarketplace.repositories.UserRepository;
import com.example.ecommercemarketplace.services.EmailConfirmationTokenService;
import com.example.ecommercemarketplace.services.UserService;
import com.example.ecommercemarketplace.utils.PublicIdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PublicIdGenerator publicIdGenerator;
    private final EmailConfirmationTokenService emailConfirmationTokenService;

    @Override
    public UserDto findByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User with email=%s is not found".formatted(email)));
        return userMapper.mapTo(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByEmailConfirmationToken(String token) {
        return userRepository.existsByToken(token);
    }

    @Override
    public boolean isUserEntity(String email) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        return userOptional.isPresent() && userOptional.get() instanceof UserEntity;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException("User with email=%s already exists".formatted(userDto.getEmail()));
        }

        String publicId = publicIdGenerator.generate();
        String hashedPassword = passwordEncoder.encode(userDto.getPassword());

        userDto.setPublicId(publicId);
        userDto.setPassword(hashedPassword);
        userDto.setLoginData(new LoginData());

        UserEntity savedUser = userRepository.save(userMapper.mapFrom(userDto));

        return userMapper.mapTo(savedUser);
    }

    @Override
    public UserDto findByEmailConfirmationToken(String token) {
        EmailConfirmationToken emailConfirmationToken = emailConfirmationTokenService.findByToken(token);
        UserEntity user = userRepository.findByEmailConfirmationToken(emailConfirmationToken).orElseThrow(() ->
                new UserNotFoundException("User with confirmationToken=%s is not found".formatted(token)));

        return userMapper.mapTo(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        throwIfUserNotFoundByPublicId(userDto.getPublicId());

        UserEntity updatedUser = userRepository.save(userMapper.mapFrom(userDto));

        return userMapper.mapTo(updatedUser);
    }

    @Override
    public UserDto findUserByPublicId(String publicId) {
        UserEntity user = userRepository.findByPublicId(publicId).orElseThrow(() ->
                new UserNotFoundException("User with publicId=%s is not found".formatted(publicId)));
        return userMapper.mapTo(user);
    }

    @Override
    public Page<UserDto> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::mapTo);
    }

    @Override
    public UserDto updateUserFully(String publicId, UserDto userDto) {
        UserEntity user = userRepository.findByPublicId(publicId).orElseThrow(() ->
                new UserNotFoundException("User with publicId=%s is not found".formatted(publicId)));
        user.setFirstName(userDto.getFirstName());
        user.setLastName((userDto.getLastName()));
        user.setPhoneNumber(userDto.getPhoneNumber());

        UserEntity savedUser = userRepository.save(user);

        return userMapper.mapTo(savedUser);

    }

    @Override
    public UserDto updateUserPatch(String publicId, UserDto userDto) {
        throwIfUserNotFoundByPublicId(publicId);

        UserEntity updatedUser = userRepository.findByPublicId(publicId).map(user -> {
            Optional.ofNullable(userDto.getFirstName()).ifPresent(user::setFirstName);
            Optional.ofNullable(userDto.getLastName()).ifPresent(user::setLastName);
            Optional.ofNullable(userDto.getPhoneNumber()).ifPresent(user::setPhoneNumber);
            return user;
        }).get();

        UserEntity savedUser = userRepository.save(updatedUser);

        return userMapper.mapTo(savedUser);
    }

    @Override
    public void removeUserByPublicId(String publicId) {
        throwIfUserNotFoundByPublicId(publicId);
        userRepository.deleteByPublicId(publicId);
    }

    private void throwIfUserNotFoundByPublicId(String publicId){
        if (!userRepository.existsByPublicId(publicId)) {
            throw new UserNotFoundException("User with publicId=%s is not found".formatted(publicId));
        }
    }


}
