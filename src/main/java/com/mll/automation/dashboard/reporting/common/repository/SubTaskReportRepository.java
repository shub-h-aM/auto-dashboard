package com.mll.automation.dashboard.reporting.common.repository;

import com.mll.automation.dashboard.reporting.common.model.SubTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import java.util.List;

public interface SubTaskReportRepository extends JpaRepository<SubTask,Long> {
    Page<SubTask> findByFromDateGreaterThanEqualAndToDateLessThanEqual(Date fromDate, Date toDate, Pageable pageable);
    List<SubTask> findByFromDateGreaterThanEqualAndToDateLessThanEqual(Date fromDate, Date toDate);
}
