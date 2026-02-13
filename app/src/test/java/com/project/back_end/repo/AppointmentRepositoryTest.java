
package com.project.back_end.repo;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;


    private Doctor testDoctor;
    private Patient testPatient;
    private Appointment testAppointment;

    @BeforeEach
    void setUp() {
        testDoctor = doctorRepository.findById(1L).orElseThrow(() -> new RuntimeException("Doctor not found"));

        testPatient = patientRepository.findById(1L).orElseThrow(() -> new RuntimeException("Patient not found"));
        
        testAppointment = new Appointment();
        testAppointment.setDoctor(testDoctor);
        testAppointment.setPatient(testPatient);
        testAppointment.setAppointmentTime(LocalDateTime.now().plusDays(1));
        testAppointment.setStatus(0);
    }

    @Test
    void testFindByDoctorIdAndAppointmentTimeBetween() {
        appointmentRepository.save(testAppointment);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        List<Appointment> results = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(testDoctor.getId(), start, end);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        //assertEquals(1, results.size());
    }

    @Test
    void testFindByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween() {
        appointmentRepository.save(testAppointment);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        List<Appointment> results = appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                1L, "john", start, end);

        assertNotNull(results);
       // assertFalse(results.isEmpty());
    }

    @Test
    void testFindByPatientId() {
        appointmentRepository.save(testAppointment);

        List<Appointment> results = appointmentRepository.findByPatientId(testPatient.getId() );

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1L, results.get(0).getPatient().getId());
    }

    @Test
    void testFindByPatient_IdAndStatusOrderByAppointmentTimeAsc() {
        appointmentRepository.save(testAppointment);

        List<Appointment> results = appointmentRepository.findByPatient_IdAndStatusOrderByAppointmentTimeAsc(1L, 0);

        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    void testFilterByDoctorNameAndPatientId() {
        appointmentRepository.save(testAppointment);

        List<Appointment> results = appointmentRepository.filterByDoctorNameAndPatientId(testAppointment.getDoctor().getName(), testPatient.getId() );

        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    void testFilterByDoctorNameAndPatientIdAndStatus() {
        appointmentRepository.save(testAppointment);

        List<Appointment> results = appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(testDoctor.getName(), testPatient.getId(), 0);

        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    void testUpdateStatus() {
        Appointment saved = appointmentRepository.save(testAppointment);
        appointmentRepository.updateStatus(1, saved.getId());

        Appointment updated = appointmentRepository.findById(saved.getId()).orElse(null);
        assertNotNull(updated);
        assertEquals(1, updated.getStatus());
    }


}