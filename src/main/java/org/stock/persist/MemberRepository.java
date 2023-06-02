package org.stock.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stock.persist.entity.MemberEntity;

import java.lang.reflect.Member;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}
