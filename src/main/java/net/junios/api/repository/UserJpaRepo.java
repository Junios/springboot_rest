package net.junios.api.repository;

import net.junios.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepo extends JpaRepository<User,Long> {
    Optional<User> findByUid(String email);

    Optional<User> findByUidAndProvider(String uid, String provider);
}
