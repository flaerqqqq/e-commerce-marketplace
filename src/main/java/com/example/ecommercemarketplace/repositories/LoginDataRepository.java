package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.LoginData;
import com.example.ecommercemarketplace.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoginDataRepository extends JpaRepository<LoginData, Long> {

    LoginData findByUser(UserEntity userEntity);

    List<LoginData> findAllByLoginDisabled(boolean loginDisabled);

}
