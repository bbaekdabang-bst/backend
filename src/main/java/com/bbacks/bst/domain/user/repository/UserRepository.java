package com.bbacks.bst.domain.user.repository;

import com.bbacks.bst.domain.user.domain.PlatformType;
import com.bbacks.bst.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserToken(String userToken);

    Optional<User> findByUserSocialId(String socialId);

    Optional<User> findByUserPlatformAndUserSocialId(PlatformType platformType, String userSocialId);

}
