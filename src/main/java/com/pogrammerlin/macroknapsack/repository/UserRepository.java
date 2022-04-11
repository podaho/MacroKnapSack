package com.pogrammerlin.macroknapsack.repository;

import com.pogrammerlin.macroknapsack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(long userId);
}
