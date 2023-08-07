package com.bbacks.bst.user.repository;

import com.bbacks.bst.user.domain.PlatformType;
import com.bbacks.bst.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserNickname(String userToken);

    Optional<User> findByUserPlatformAndUserSocialId(PlatformType platformType, String userSocialId);

}
