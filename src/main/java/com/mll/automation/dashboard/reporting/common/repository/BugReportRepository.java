package com.mll.automation.dashboard.reporting.common.repository;

import com.mll.automation.dashboard.reporting.common.model.Bug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface BugReportRepository extends JpaRepository<Bug,Long> {
    Page<Bug> findByFromDateGreaterThanEqualAndToDateLessThanEqual(Date fromDate, Date toDate, Pageable pageable);
    List<Bug> findByFromDateGreaterThanEqualAndToDateLessThanEqual(Date fromDate, Date toDate);
}
