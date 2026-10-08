package com.klanvision.healthcare.config;

import com.klanvision.healthcare.dto.AppointmentBookingRequest;
import com.klanvision.healthcare.model.Doctor;
import com.klanvision.healthcare.model.Patient;
import com.klanvision.healthcare.service.HealthcareService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initHealthcareData(HealthcareService healthcareService) {
        return args -> {
            // Pre-load Patients
            Patient p1 = healthcareService.registerPatient(new Patient(null, "Pavani Yellaturu", "pavani@example.com", "+91 9876543210", 24, "Female"));
            Patient p2 = healthcareService.registerPatient(new Patient(null, "Rahul Sharma", "rahul@example.com", "+91 9123456789", 30, "Male"));

            // Pre-load Doctors
            Doctor d1 = healthcareService.addDoctor(new Doctor(null, "Dr. Sarah Jenkins", "Cardiology", 12, "+91 9888877771", "sarah.jenkins@hospital.com"));
            Doctor d2 = healthcareService.addDoctor(new Doctor(null, "Dr. Amit Patel", "Neurology", 15, "+91 9888877772", "amit.patel@hospital.com"));
            Doctor d3 = healthcareService.addDoctor(new Doctor(null, "Dr. Elena Rostova", "Pediatrics", 8, "+91 9888877773", "elena.rostova@hospital.com"));

            // Pre-load Appointments
            AppointmentBookingRequest req1 = new AppointmentBookingRequest();
            req1.setPatientId(p1.getId());
            req1.setDoctorId(d1.getId());
            req1.setAppointmentDateTime(LocalDateTime.now().plusDays(2).withHour(10).withMinute(0).withSecond(0).withNano(0));
            req1.setNotes("Routine heart checkup");
            healthcareService.bookAppointment(req1);

            AppointmentBookingRequest req2 = new AppointmentBookingRequest();
            req2.setPatientId(p2.getId());
            req2.setDoctorId(d2.getId());
            req2.setAppointmentDateTime(LocalDateTime.now().plusDays(3).withHour(14).withMinute(30).withSecond(0).withNano(0));
            req2.setNotes("Migraine consultation");
            healthcareService.bookAppointment(req2);
        };
    }
}
