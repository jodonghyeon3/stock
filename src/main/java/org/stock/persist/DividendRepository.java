package org.stock.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.stock.persist.entity.DividendEntity;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface DividendRepository extends JpaRepository<DividendEntity,Long> {
    List<DividendEntity> findAllByCompanyId(Long id);

    @Transactional
    void deleteAllByCompanyId(Long id);
    boolean existsByCompanyIdAndDate(Long CompanyId , LocalDateTime date);
}
