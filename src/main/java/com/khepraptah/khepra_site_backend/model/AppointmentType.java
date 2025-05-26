package com.khepraptah.khepra_site_backend.model;

public enum AppointmentType {
    READING(30),
    CLEANSING(45),
    INITIATION(60),
    WORKSHOP(90);

    private final int durationMinutes;

    AppointmentType(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }
}
