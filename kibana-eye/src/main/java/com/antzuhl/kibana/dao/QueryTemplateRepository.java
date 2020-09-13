package com.antzuhl.kibana.dao;

import com.antzuhl.kibana.domain.QueryTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryTemplateRepository extends JpaRepository<QueryTemplate, Long> {
    public QueryTemplate findQueryTemplateByType(String type);
}
