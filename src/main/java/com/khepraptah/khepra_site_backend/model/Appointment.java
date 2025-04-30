package com.khepraptah.khepra_site_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;


import java.util.Date;
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "appointment_type")
    private String type;

    @Column(name = "user_id")
    private String userId;
    @Column(name = "client_name")
    private String name;

    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private double phone_number;

    @Column(name = "date")
    private Date date;

    @Column(name = "isVirtual")
    private Boolean isVirtual;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) {this.email = email;}

    public double getPhone_number() { return phone_number; }
    public void setPhone_number(double phone_number) { this.phone_number = phone_number; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public Boolean getIsVirtual() { return isVirtual; }
    public void setIsVirtual(Boolean isVirtual) { this.isVirtual = isVirtual; }
}
