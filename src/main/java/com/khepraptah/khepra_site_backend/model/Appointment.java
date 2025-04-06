package com.khepraptah.khepra_site_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String name;
    private String email;
    private double phone_number;
    private Date date;
    private boolean isVirtual;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) {this.email = email;}

    public double getPhone_number() { return phone_number; }
    public void setPhone_number(double phone_number) { this.phone_number = phone_number; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public boolean getIsVirtual() { return isVirtual; }
    public void setIsVirtual(boolean isVirtual) { this.isVirtual = isVirtual; }
}
