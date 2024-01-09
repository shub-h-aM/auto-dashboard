package com.mll.automation.dashboard.reporting.common.repository;

import com.mll.automation.dashboard.reporting.common.model.ParentTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
@Repository
public interface ParentTaskReportRepository extends JpaRepository<ParentTask,Long>{
    Page<ParentTask> findByFromDateGreaterThanEqualAndToDateLessThanEqual(Date fromDate, Date toDate, Pageable pageable);
    @Query("SELECT p.automationTicket, p.noOfBlocker, p.noOfRejection, p.noOfEnhancement, p.qaSuggestion, p.status, p.automationTicket " +
            "FROM ParentTask p " +
            "WHERE p.fromDate >= :fromDate AND p.toDate <= :toDate")
    List<Object[]> getFieldsByDateRange(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
    List<ParentTask> findByFromDateGreaterThanEqualAndToDateLessThanEqual(Date fromDate, Date toDate);
}
