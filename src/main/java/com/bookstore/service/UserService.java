package com.bookstore.service;

import com.bookstore.dto.UserDto;
import com.bookstore.dto.UserRegistrationDto;
import com.bookstore.entity.User;
import java.util.List;

public interface UserService {
    void saveUser(UserRegistrationDto registrationDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();

    void deleteUser(Long id);

    void updateUser(User user);

    void updateUserAddress(Long userId, String address);

    void resetUserPassword(Long userId, String rawPassword);
}
