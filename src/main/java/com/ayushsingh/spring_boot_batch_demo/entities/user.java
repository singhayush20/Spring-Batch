package com.ayushsingh.spring_boot_batch_demo.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class user {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rollNo")
    private String rollNo;
    @Column(name="name",nullable = false)
    private String name;
    @Column(name="personalEmail",nullable = false)
    private String personalEmail;
    @Column(name="officialEmail",nullable = false)
    private String officialEmail;
    @Column(name="mobileNo",nullable = false)
    private Long mobileNo;
}
