package com.okestro.symphony.dashboard.batch.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "QRTZ_CRON_TRIGGERS", schema="framework")
@IdClass(QrtzTriggersId.class)
@NoArgsConstructor
public class QrtzCronTriggersInfo {
    @Id
    private String schedName;

    @Id
    private String triggerName;

    @Id
    private String triggerGroup;

    @Column(name = "cron_expression")
    private String cronExpression;
}
