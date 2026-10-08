package com.klanvision.healthcare;

import com.klanvision.healthcare.dto.AppointmentBookingRequest;
import com.klanvision.healthcare.model.Appointment;
import com.klanvision.healthcare.model.Doctor;
import com.klanvision.healthcare.model.Patient;
import com.klanvision.healthcare.service.HealthcareService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class HealthcareServiceTest {

    @Autowired
    private HealthcareService healthcareService;

    @Test
    public void testPatientRegistrationAndDoctorSearch() {
        Patient p = new Patient(null, "Test Patient", "test@patient.com", "1234567890", 25, "Female");
        Patient registered = healthcareService.registerPatient(p);
        assertNotNull(registered.getId());

        Doctor d = new Doctor(null, "Dr. Test Doctor", "Neurology", 10, "9876543210", "doc@test.com");
        healthcareService.addDoctor(d);

        var searchResults = healthcareService.searchDoctors("Neurology");
        assertFalse(searchResults.isEmpty());
    }

    @Test
    public void testAppointmentBookingAndDuplicatePrevention() {
        Patient p = healthcareService.registerPatient(new Patient(null, "John Doe", "john@doe.com", "5551234", 30, "Male"));
        Doctor d = healthcareService.addDoctor(new Doctor(null, "Dr. Smith", "Orthopedics", 5, "5559876", "smith@ortho.com"));

        LocalDateTime slot = LocalDateTime.now().plusDays(5).withHour(11).withMinute(0).withSecond(0).withNano(0);

        AppointmentBookingRequest req1 = new AppointmentBookingRequest();
        req1.setPatientId(p.getId());
        req1.setDoctorId(d.getId());
        req1.setAppointmentDateTime(slot);
        req1.setNotes("Knee checkup");

        Appointment booked = healthcareService.bookAppointment(req1);
        assertNotNull(booked.getId());

        // Attempt duplicate booking for same doctor & time slot
        AppointmentBookingRequest req2 = new AppointmentBookingRequest();
        req2.setPatientId(p.getId());
        req2.setDoctorId(d.getId());
        req2.setAppointmentDateTime(slot);

        assertThrows(IllegalStateException.class, () -> healthcareService.bookAppointment(req2));
    }
}
