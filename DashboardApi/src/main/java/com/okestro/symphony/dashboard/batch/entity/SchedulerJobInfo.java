package com.okestro.symphony.dashboard.batch.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@Entity
//@Table(name = "tb_schedule_job", schema="framework")
@Table(name = "tb_schedule_job")
public class SchedulerJobInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "sch_id")
    private Long schId;

    @Column(name = "data_id")
    private String dataId;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "cron_job")
    private Boolean cronJob;

    @Column(name = "sched_class")
    private String schedClass;

    @Column(name = "sched_group")
    private String schedGroup;

    @Column(name = "sched_name")
    private String schedName;

    @Column(name = "is_external")
    private char isExternal;

    @Column(name = "repeat_time")
    private Long repeatTime;

    @Column(name = "sched_desc")
    private String schedDesc;

//    @Column(name = "cret_dt")
//    private Date cret_dt;
//    @Column(name = "cret_user_id")
//    private String cret_user_id;
//    @Column(name = "updt_dt")
//    private Date updtDt;
//    @Column(name = "updt_user_id")
//    private String updtUser_id;

}
