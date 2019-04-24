package org.nkcoder.user;

import org.nkcoder.user.domain.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByUuid(String uuid);

    Optional<User> findByEmail(String email);
}
