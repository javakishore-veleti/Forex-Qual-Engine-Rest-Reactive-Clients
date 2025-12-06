package com.jk.labs.fx.qual_engine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@MappedSuperclass
@EqualsAndHashCode
public class BaseEntity {

    @Column(name = "id")
    @Id
    private String id;

    @Column(name = "created_dt")
    private Date createdAt;

    @Column(name = "updated_dt")
    private Date updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

}
