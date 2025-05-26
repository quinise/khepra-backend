package com.khepraptah.khepra_site_backend.util;

import com.khepraptah.khepra_site_backend.model.Schedulable;

import java.time.Duration;
import java.util.Locale;
import java.util.Optional;

public class BufferUtils {
    public static Duration getBufferDuration(Schedulable item) {
        if (item.isVirtual()) {
            return Duration.ofMinutes(15);
        }

        String location = Optional.ofNullable(item.getCity())
                .orElse("virtual")
                .toLowerCase(Locale.US);
        if (location.contains("bremerton")) {
            return Duration.ofMinutes(150); // 2.5 hours
        } else if (location.contains("seattle")) {
            return Duration.ofMinutes(45);
        } else if (location.contains("virtual")) {
            return Duration.ofMinutes(15);
        }

        // Default buffer
        return Duration.ofMinutes(15);
    }
}
