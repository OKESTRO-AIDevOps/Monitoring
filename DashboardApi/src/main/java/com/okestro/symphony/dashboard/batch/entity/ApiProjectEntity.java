package com.okestro.symphony.dashboard.batch.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "PROJECT")
@IdClass(ApiProjectPK.class)
@NoArgsConstructor
public class ApiProjectEntity implements Serializable {

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
