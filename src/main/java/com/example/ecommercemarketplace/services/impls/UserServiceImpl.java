package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.exceptions.UserAlreadyExistsException;
import com.example.ecommercemarketplace.exceptions.UserNotFoundException;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.UserEntity;
import com.example.ecommercemarketplace.repositories.UserRepository;
import com.example.ecommercemarketplace.services.UserService;
import com.example.ecommercemarketplace.utils.PublicIdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private Mapper<UserEntity, UserDto> userMapper;
    private PasswordEncoder passwordEncoder;
    private PublicIdGenerator publicIdGenerator;

    @Override
    public UserDto findByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email=" + email +" is not found"));

        return userMapper.mapTo(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail())){
            throw new UserAlreadyExistsException("User with email="+userDto.getEmail()+ " already exists");
        }

        String publicId = publicIdGenerator.generate();
        String hashedPassword = passwordEncoder.encode(userDto.getPassword());

        userDto.setPublicId(publicId);
        userDto.setPassword(hashedPassword);

        UserEntity savedUser = userRepository.save(userMapper.mapFrom(userDto));

        return userMapper.mapTo(savedUser);
    }
}
