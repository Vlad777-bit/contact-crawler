package com.github.vlad777bit.contactcrawler.repository;

import com.github.vlad777bit.contactcrawler.model.ContactInfo;
import com.github.vlad777bit.contactcrawler.model.ContactType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {
    boolean existsByNormalizedValueAndType(String normalizedValue, ContactType type);
    List<ContactInfo> findTop100ByTask_IdOrderByIdAsc(Long taskId);

    Page<ContactInfo> findByTask_Id(Long taskId, Pageable pageable);
    Page<ContactInfo> findByTask_IdAndType(Long taskId, ContactType type, Pageable pageable);
}
