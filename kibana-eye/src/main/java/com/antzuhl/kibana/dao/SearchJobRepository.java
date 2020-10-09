package com.antzuhl.kibana.dao;

import com.antzuhl.kibana.domain.SearchJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchJobRepository extends JpaRepository<SearchJob, Long> {
}
