package com.okestro.symphony.dashboard.batch.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "QRTZ_TRIGGERS", schema="framework")
@IdClass(QrtzTriggersId.class)
public class QrtzTriggersInfo {
    @Id
    private String schedName;

    @Id
    private String triggerName;

    @Id
    private String triggerGroup;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_group")
    private String jobGroup;

    @Column(name = "next_fire_time")
    private Long nextFireTime;

    @Column(name = "prev_fire_time")
    private Long prevFireTime;

    @Column(name = "trigger_state")
    private String triggerState;

    @Column(name = "trigger_type")
    private String triggerType;

    @Column(name = "start_time")
    private Long startTime;

    @OneToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private QrtzCronTriggersInfo qrtzCronTriggersInfo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trigger_name", referencedColumnName = "sched_name", insertable = false, updatable = false)
    private SchedulerJobInfo schedulerJobInfo;

    @Transient
    private boolean toggled;

    /**
     * 옵션 메뉴 토글 용도
     */
    public QrtzTriggersInfo() {
        toggled = false;
    }
}
