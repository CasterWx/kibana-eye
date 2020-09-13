package com.antzuhl.kibana.dao;

import com.antzuhl.kibana.domain.IndexDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexDetailRepository extends JpaRepository<IndexDetail, Long> {
    IndexDetail findIndexDetailByDay(Integer day);
    IndexDetail findIndexDetailByName(String name);
}
