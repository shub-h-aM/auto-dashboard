package com.mll.automation.dashboard.reporting.common.model.helper;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Getter
@Slf4j
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public abstract class AbstractEntity {
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false,name = "created_at")
    protected DateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "last_updated_at")
    protected DateTime lastUpdatedAt;

    @PrePersist
    protected void onCreate() {
        this.lastUpdatedAt = this.createdAt = DateTime.now();
        log.info("On Create ..");
    }
}
