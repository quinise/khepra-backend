package com.khepraptah.khepra_site_backend.model;

public enum EventType {
    WORKSHOP(90),
    BEMBE(120),
    LECTURE(90),
    EGUNGUN(60),
    TRAINING(120);

    private final int durationMinutes;

    EventType(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }
}
