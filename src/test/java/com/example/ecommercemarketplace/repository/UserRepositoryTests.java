package com.example.ecommercemarketplace.repository;


import com.example.ecommercemarketplace.models.UserEntity;
import com.example.ecommercemarketplace.repositories.UserRepository;
import com.example.ecommercemarketplace.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private UserEntity dummyUser1;
    private UserEntity dummyUser2;

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

    }

    @AfterEach
    public void cleanUp() {
        dummyUser1 = dummyUser2 = null;
    }
/*
    @Test
    public void UserRepository_Save_ReturnsSavedUser() {
        UserEntity savedUser = userRepository.save(dummyUser1);

        assertThat(savedUser).isEqualTo(dummyUser1);
    }

    @Test
    public void UserRepository_FindAll_ReturnsListOfAllSavedUsers() {
        userRepository.save(dummyUser1);
        userRepository.save(dummyUser2);

        List<UserEntity> savedUsers = userRepository.findAll();

        assertThat(savedUsers).isNotNull();
        assertThat(savedUsers).hasSize(2);
    }

    @Test
    public void UserRepository_FindByPublicId_ShouldReturnCorrectUser() {
        String publicId = dummyUser1.getPublicId();
        userRepository.save(dummyUser1);

        Optional<UserEntity> user = userRepository.findByPublicId(publicId);

        assertThat(user).isPresent();
        assertThat(user.get().getPublicId()).isEqualTo(publicId);
    }

    @Test
    public void UserRepository_FindByEmail_ShouldReturnCorrectUser() {
        String email = dummyUser1.getEmail();
        userRepository.save(dummyUser1);

        Optional<UserEntity> user = userRepository.findByEmail(email);

        assertThat(user).isPresent();
        assertThat(user.get().getEmail()).isEqualTo(email);
    }

    @Test
    public void UserRepository_ExistsByEmail_ShouldReturnTrueOnUserExistence() {
        String email = dummyUser1.getEmail();
        userRepository.save(dummyUser1);

        boolean userExistenceByEmail = userRepository.existsByEmail(email);

        assertThat(userExistenceByEmail).isTrue();
    }

    @Test
    public void UserRepository_ExistsByPublicId_ShouldReturnTrueOnUserExistence() {
        String publicId = dummyUser1.getPublicId();
        userRepository.save(dummyUser1);

        boolean userExistenceByPublicId = userRepository.existsByPublicId(publicId);

        assertThat(userExistenceByPublicId).isTrue();
    }

*/
}
