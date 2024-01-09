package com.mll.automation.dashboard.reporting.common.model;

import lombok.*;
import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "parent_task")
public class ParentTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "ticket_id")
    private String ticketId;

    @Column(name = "title")
    private String title;

    @Column(name = "qa_owner")
    private String qaOwner;

    @Column(name = "status")
    private String status;

    @Column(name = "automation_ticket")
    private String automationTicket;

    @Column(name = "no_of_blocker")
    private String noOfBlocker;

    @Column(name = "no_of_rejection")
    private String noOfRejection;

    @Column(name = "no_of_enhancement")
    private String noOfEnhancement;

    @Column(name = "qa_suggestion")
    private String qaSuggestion;

    @Column(name="automation_sanity_suite_used")
    private String automationSanitySuiteUsed;

    @Column(name = "from_date")
    @Temporal(TemporalType.DATE)
    private Date fromDate;

    @Column(name = "to_date")
    @Temporal(TemporalType.DATE)
    private Date toDate;
}
