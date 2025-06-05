package com.khepraptah.khepra_site_backend.util;

import com.khepraptah.khepra_site_backend.model.Appointment;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class BufferUtilsTest {

    @Test
    void getBufferDuration_returns15Minutes_forVirtualAppointment() {
        Appointment appointment = new Appointment();
        appointment.setIsVirtual(true);
        appointment.setCity(null); // null city

        Duration buffer = BufferUtils.getBufferDuration(appointment);

        assertThat(buffer).isEqualTo(Duration.ofMinutes(15));
    }

    @Test
    void getBufferDuration_returns2_5Hours_forBremerton() {
        Appointment appointment = new Appointment();
        appointment.setIsVirtual(false);
        appointment.setCity("Bremerton");

        Duration buffer = BufferUtils.getBufferDuration(appointment);

        assertThat(buffer).isEqualTo(Duration.ofMinutes(150));
    }

    @Test
    void getBufferDuration_returns45Minutes_forSeattle() {
        Appointment appointment = new Appointment();
        appointment.setIsVirtual(false);
        appointment.setCity("Seattle");

        Duration buffer = BufferUtils.getBufferDuration(appointment);

        assertThat(buffer).isEqualTo(Duration.ofMinutes(45));
    }

    @Test
    void getBufferDuration_returnsDefault_whenCityUnknown() {
        Appointment appointment = new Appointment();
        appointment.setIsVirtual(false);
        appointment.setCity("UnknownTown");

        Duration buffer = BufferUtils.getBufferDuration(appointment);

        assertThat(buffer).isEqualTo(Duration.ofMinutes(15));
    }
}
