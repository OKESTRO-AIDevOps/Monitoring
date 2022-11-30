package com.okestro.symphony.dashboard.api.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "T_PROVIDER_MENU")
@TypeDef(
        name = "json",
        typeClass = JsonType.class
)
public class ProviderMenu {
    @Id
    @Column(name = "created_at", columnDefinition = "bigint(20) unsigned")
    private Long createdAt;

    @Column(name = "prvd_id", length = 100)
    private String updatedId;

    @Type(type = "json")
    @Column(name = "json_data", columnDefinition = "json")
    @NotNull
    private String jsonData;
}