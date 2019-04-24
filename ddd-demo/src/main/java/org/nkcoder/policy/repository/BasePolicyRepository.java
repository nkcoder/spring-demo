package org.nkcoder.policy.repository;

import org.nkcoder.policy.domain.model.Policy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;


@NoRepositoryBean
public interface BasePolicyRepository<T extends Policy> extends CrudRepository<T, Long> {
    Optional<T> findByPolicyNumber(String PolicyNumber);
}
