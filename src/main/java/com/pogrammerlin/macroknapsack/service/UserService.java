package com.pogrammerlin.macroknapsack.service;

import com.pogrammerlin.macroknapsack.dto.response.UserDetailResponse;
import com.pogrammerlin.macroknapsack.dto.response.UsersDetailResponse;
import com.pogrammerlin.macroknapsack.mapper.MacroKnapSackDataMapper;
import com.pogrammerlin.macroknapsack.model.User;
import com.pogrammerlin.macroknapsack.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private MacroKnapSackDataMapper mapper;

    public UsersDetailResponse getAllUsers() {
        return mapper.mapUsersDetailResponse(userRepository.findAll());
    }

    public User getUserByUserId(long userId) {
        return userRepository.findUserById(userId);
    }

    public UserDetailResponse createUser(User user) {
        return mapper.mapUserDetailResponse(userRepository.save(user));
    }
}
