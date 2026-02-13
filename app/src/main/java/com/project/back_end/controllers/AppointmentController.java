package com.project.back_end.controllers;


import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.CMService;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.PatientService;

import jakarta.websocket.server.PathParam;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import javax.print.Doc;
import javax.swing.text.html.Option;

import static org.springframework.util.ClassUtils.isPresent;


@RestController
@RequestMapping("${api.path}/appointments")
public class AppointmentController {

    Logger logger = Logger.getLogger(AppointmentService.class.getName());
    private AppointmentService appointmentService;
    private CMService service;
    private DoctorService doctorService;
    private PatientService patientService;

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller.
//    - Use `@RequestMapping("/appointments")` to set a base path for all appointment-related endpoints.
//    - This centralizes all routes that deal with booking, updating, retrieving, and canceling appointments.

    public AppointmentController(AppointmentService appointmentService, CMService service, DoctorService doctorService, PatientService patientService) {
        this.appointmentService = appointmentService;
        this.service = service;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

// 2. Autowire Dependencies:
//    - Inject `AppointmentService` for handling the business logic specific to appointments.
//    - Inject the general `Service` class, which provides shared functionality like token validation and appointment checks.


    // 3. Define the `getAppointments` Method:
//    - Handles HTTP GET requests to fetch appointments based on date and patient name.
//    - Takes the appointment date, patient name, and token as path variables.
//    - First validates the token for role `"doctor"` using the `Service`.
//    - If the token is valid, returns appointments for the given patient on the specified date.
//    - If the token is invalid or expired, responds with the appropriate message and status code.
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<Map<String, Object>> getAppointments(@RequestParam LocalDate date, @RequestParam String patientName, @RequestParam String token) {
        Map<String, Object> response = new HashMap<>();
        if (service.validateToken(token, "doctor")) {
            List<Appointment> appointments = appointmentService.getAppointmentsByDoctorsTokenAndDate(token, date, patientName);
            response.put("appointments", appointments);
        } else {
            response.put("error", "Invalid or expired token. Please log in again.");
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);

    }


    // 4. Define the `bookAppointment` Method:
//    - Handles HTTP POST requests to create a new appointment.
//    - Accepts a validated `Appointment` object in the request body and a token as a path variable.
//    - Validates the token for the `"patient"` role.
//    - Uses service logic to validate the appointment data (e.g., check for doctor availability and time conflicts).
//    - Returns success if booked, or appropriate error messages if the doctor ID is invalid or the slot is already taken.
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, Object>> bookAppointment(@RequestBody AppointmentDTO appointmentDTO, @PathVariable String token) {
        //TODO: process POST request
        Map<String, Object> response = new HashMap<>();
        if (service.validateToken(token, "patient")) {
            Appointment appointment = new Appointment();
            Optional<Doctor> doctorOption = doctorService.getDoctorById(appointmentDTO.getDoctorId());
            if (!doctorOption.isPresent()) {
                response.put("error", "Invalid doctor Id.");
                return ResponseEntity.badRequest().body(response);
            }
            appointment.setDoctor(doctorOption.get());
            appointment.setPatient(patientService.getPatientById(appointmentDTO.getPatientId()));
            appointment.setAppointmentTime(appointmentDTO.getAppointmentTime());
            int result = appointmentService.bookAppointment(appointment);
            if (result == 1) {
                response.put("message", "Appointment booked successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Failed to book appointment. Please try again.");
                return ResponseEntity.status(500).body(response);
            }
        } else {
            response.put("error", "Invalid or expired token. Please log in again.");
            return ResponseEntity.badRequest().body(response);
        }
    }


    // 5. Define the `updateAppointment` Method:
//    - Handles HTTP PUT requests to modify an existing appointment.
//    - Accepts a validated `Appointment` object and a token as input.
//    - Validates the token for `"patient"` role.
//    - Delegates the update logic to the `AppointmentService`.
//    - Returns an appropriate success or failure response based on the update result.
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, Object>> updateAppointment(@RequestBody AppointmentDTO appointmentDTO, @PathVariable String token) {
        Map<String, Object> response = new HashMap<>();
        if (service.validateToken(token, "patient")) {
            Appointment updatedAppointment = new Appointment();
            Optional<Doctor> doctorOption = doctorService.getDoctorById(appointmentDTO.getDoctorId());
            if (!doctorOption.isPresent()) {
                response.put("error", "Invalid doctor Id.");
                return ResponseEntity.badRequest().body(response);
            }
            updatedAppointment.setDoctor(doctorOption.get());
            updatedAppointment.setPatient(patientService.getPatientById(appointmentDTO.getPatientId()));
            updatedAppointment.setAppointmentTime(appointmentDTO.getAppointmentTime());
            updatedAppointment.setStatus(appointmentDTO.getStatus());
            String result = appointmentService.updateAppointment(appointmentDTO.getId(), updatedAppointment);
            if (result.equals("success")) {
                response.put("message", "Appointment updated successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Failed to update appointment. Please try again.");
                return ResponseEntity.status(500).body(response);
            }
        } else {
            response.put("error", "Invalid or expired token. Please log in again.");
            return ResponseEntity.badRequest().body(response);
        }
    }


    // 6. Define the `cancelAppointment` Method:
//    - Handles HTTP DELETE requests to cancel a specific appointment.
//    - Accepts the appointment ID and a token as path variables.
//    - Validates the token for `"patient"` role to ensure the user is authorized to cancel the appointment.
//    - Calls `AppointmentService` to handle the cancellation process and returns the result.
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, Object>> cancelAppointment(@PathVariable Long id, @PathVariable String token) {
        Map<String, Object> response = new HashMap<>();
        if (service.validateToken(token, "patient")) {
            Patient patient = service.getPatientByToken(token);

            String result = appointmentService.cancelAppointment(id, patient.getId());
            if (result.equals("success")) {
                response.put("message", "Appointment canceled successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Failed to cancel appointment. Please try again.");
                return ResponseEntity.status(500).body(response);
            }
        } else {
            response.put("error", "Invalid or expired token. Please log in again.");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
