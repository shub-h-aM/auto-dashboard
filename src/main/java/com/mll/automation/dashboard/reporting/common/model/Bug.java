package com.mll.automation.dashboard.reporting.common.model;

import lombok.*;
import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "bug")
public class Bug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="ticket")
    private String ticket;

    @Column(name="title")
    private String title;

    @Column(name="qa_owner")
    private String qaOwner;

    @Column(name="parent_ticket")
    private String parentTicket;

    @Column(name = "from_date")
    @Temporal(TemporalType.DATE)
    private Date fromDate;

    @Column(name = "to_date")
    @Temporal(TemporalType.DATE)
    private  Date toDate;
}
