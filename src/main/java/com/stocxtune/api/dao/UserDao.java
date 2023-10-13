package com.stocxtune.api.dao;

import com.stocxtune.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserDao extends JpaRepository<User,String> {


    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByMobileNumber(String mobileNumber);

    Boolean existsByAccountNumber(String accountNumber);

}
