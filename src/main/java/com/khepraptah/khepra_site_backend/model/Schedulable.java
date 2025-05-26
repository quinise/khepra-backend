package com.khepraptah.khepra_site_backend.model;

import java.time.LocalDateTime;

public interface Schedulable {
    LocalDateTime getStartTime();
    LocalDateTime getEndTime();
    boolean isVirtual();
    String getCity();
}