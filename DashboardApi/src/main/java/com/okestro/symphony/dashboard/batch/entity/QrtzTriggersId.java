package com.okestro.symphony.dashboard.batch.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class QrtzTriggersId implements Serializable {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "sched_name")
    private String schedName;

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "trigger_name")
    private String triggerName;

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "trigger_group")
    private String triggerGroup;
}
