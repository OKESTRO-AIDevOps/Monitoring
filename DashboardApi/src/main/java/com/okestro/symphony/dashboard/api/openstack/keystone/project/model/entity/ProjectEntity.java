package com.okestro.symphony.dashboard.api.openstack.keystone.project.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "PROJECT")
@IdClass(ProjectPK.class)
@NoArgsConstructor
public class ProjectEntity implements Serializable {

    @Id
    @Column(name = "PROJECT_ID", length = 36)
    private String projectId;

    @Id
    @Column(name = "PROVIDER_ID", length = 36)
    private String providerId;

    @Id
    @Column(name = "COLLECT_DT", length = 50)
    private Date collectDt;

//    @Column(name = "PROVIDER_NAME", length = 255)
//    private String providerName;

    @Column(name = "PROJECT_NAME", length = 255)
    private String projectName;

    @Column(name = "STATE", length = 16)
    private String state;


}
