package com.project.back_end.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }


// 1. **Add @Service Annotation**:
//    - This class should be annotated with `@Service` to indicate that it is a service layer class.
//    - The `@Service` annotation marks this class as a Spring-managed bean for business logic.
//    - Instruction: Add `@Service` above the class declaration.

// 2. **Constructor Injection for Dependencies**:
//    - The `DoctorService` class depends on `DoctorRepository`, `AppointmentRepository`, and `TokenService`.
//    - These dependencies should be injected via the constructor for proper dependency management.
//    - Instruction: Ensure constructor injection is used for injecting dependencies into the service.

// 3. **Add @Transactional Annotation for Methods that Modify or Fetch Database Data**:
//    - Methods like `getDoctorAvailability`, `getDoctors`, `findDoctorByName`, `filterDoctorsBy*` should be annotated with `@Transactional`.
//    - The `@Transactional` annotation ensures that database operations are consistent and wrapped in a single transaction.
//    - Instruction: Add the `@Transactional` annotation above the methods that perform database operations or queries.

// 4. **getDoctorAvailability Method**:
//    - Retrieves the available time slots for a specific doctor on a particular date and filters out already booked slots.
//    - The method fetches all appointments for the doctor on the given date and calculates the availability by comparing against booked slots.
//    - Instruction: Ensure that the time slots are properly formatted and the available slots are correctly filtered.

    @Transactional
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        try {
            Optional<Doctor> doctor = doctorRepository.findById(doctorId);
            if (doctor.isEmpty()) {
                return new ArrayList<>();
            }

            List<String> availableTimes = doctor.get().getAvailableTimes();
            if (availableTimes == null) {
                return new ArrayList<>();
            }

            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            List<Appointment> appointments = appointmentRepository
                    .findByDoctorIdAndAppointmentTimeBetween(doctorId, startOfDay, endOfDay);

            List<String> bookedTimes = new ArrayList<>();
            for (Appointment apt : appointments) {
                LocalTime aptTime = apt.getAppointmentTime().toLocalTime();
                bookedTimes.add(aptTime.toString());
            }

            List<String> finalAvailable = new ArrayList<>();
            for (String time : availableTimes) {
                if (!bookedTimes.contains(time)) {
                    finalAvailable.add(time);
                }
            }
            return finalAvailable;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

// 5. **saveDoctor Method**:
//    - Used to save a new doctor record in the database after checking if a doctor with the same email already exists.
//    - If a doctor with the same email is found, it returns `-1` to indicate conflict; `1` for success, and `0` for internal errors.
//    - Instruction: Ensure that the method correctly handles conflicts and exceptions when saving a doctor.

    @Transactional
    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
                return -1;
            }
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

// 6. **updateDoctor Method**:
//    - Updates an existing doctor's details in the database. If the doctor doesn't exist, it returns `-1`.
//    - Instruction: Make sure that the doctor exists before attempting to save the updated record and handle any errors properly.

    @Transactional
    public int updateDoctor(Doctor doctor) {
        try {
            if (!doctorRepository.existsById(doctor.getId())) {
                return -1;
            }
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

// 7. **getDoctors Method**:
//    - Fetches all doctors from the database. It is marked with `@Transactional` to ensure that the collection is properly loaded.
//    - Instruction: Ensure that the collection is eagerly loaded, especially if dealing with lazy-loaded relationships (e.g., available times). 

    @Transactional(readOnly = true)
    public List<Doctor> getDoctors() {
        try {
            return doctorRepository.findAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

// 8. **deleteDoctor Method**:
//    - Deletes a doctor from the system along with all appointments associated with that doctor.
//    - It first checks if the doctor exists. If not, it returns `-1`; otherwise, it deletes the doctor and their appointments.
//    - Instruction: Ensure the doctor and their appointments are deleted properly, with error handling for internal issues.

    @Transactional
    public int deleteDoctor(Long doctorId) {
        try {
            if (!doctorRepository.existsById(doctorId)) {
                return -1;
            }
            appointmentRepository.deleteAllByDoctorId(null);
            // appointmentRepository.deleteAll(appointments);
            doctorRepository.deleteById(doctorId);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

// 9. **validateDoctor Method**:
//    - Validates a doctor's login by checking if the email and password match an existing doctor record.
//    - It generates a token for the doctor if the login is successful, otherwise returns an error message.
//    - Instruction: Make sure to handle invalid login attempts and password mismatches properly with error responses.

    @Transactional
    public String validateDoctor(String email, String password) {
        try {
            Doctor doctor = doctorRepository.findByEmail(email);
            if (doctor == null) {
                return "Invalid email or password";
            }

            if (!doctor.getPassword().equals(password)) {
                return "Invalid email or password";
            }

            return tokenService.generateToken(email);
        } catch (Exception e) {
            return "Error validating doctor";
        }
    }

// 10. **findDoctorByName Method**:
//    - Finds doctors based on partial name matching and returns the list of doctors with their available times.
//    - This method is annotated with `@Transactional` to ensure that the database query and data retrieval are properly managed within a trafindByNameLikensaction.
//    - Instruction: Ensure that available times are eagerly loaded for the doctors.

    @Transactional
    public List<Doctor> findDoctorByName(String name) {
        try {
            return doctorRepository.findByNameLike(name);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

// 11. **filterDoctorsByNameSpecilityandTime Method**:
//    - Filters doctors based on their name, specialty, and availability during a specific time (AM/PM).
//    - The method fetches doctors matching the name and specialty criteria, then filters them based on their availability during the specified time period.
//    - Instruction: Ensure proper filtering based on both the name and specialty as well as the specified time period.

    @Transactional
    public List<Doctor> filterDoctorsByNameSpecialtyAndTime(String name, String specialty, String time) {
    try {
        List<Doctor> doctors=new ArrayList<>();

            if(name == null || name.equals("null")) name = null;
            if (specialty == null || specialty.equals("null")) specialty = null;
            if (time == null || time.equals("null")) time = null;

            if (name==null && specialty==null) {
                doctors = getDoctors();
            }else if (name!=null && specialty==null){
                doctors = findDoctorByName(name);
            }else if (name!=null && specialty!=null ){
                doctors = filterDoctorByNameAndSpecialty(name,specialty);
            }else if (name!=null && specialty!=null) {
                doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
            }
            if (time != null){
                doctors = filterDoctorByTime(doctors,time);
            }
            return doctors;


        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

// 12. **filterDoctorByTime Method**:
//    - Filters a list of doctors based on whether their available times match the specified time period (AM/PM).
//    - This method processes a list of doctors and their available times to return those that fit the time criteria.
//    - Instruction: Ensure that the time filtering logic correctly handles both AM and PM time slots and edge cases.

    private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String time) {
        List<Doctor> result = new ArrayList<>();
        for (Doctor doctor : doctors) {
            if (hasTimeSlot(doctor.getAvailableTimes(), time)) {
                result.add(doctor);
            }
        }
        return result;
    }

    private boolean hasTimeSlot(List<String> availableTimes, String time) {
        if (availableTimes == null) {
            return false;
        }

        boolean isAM = "AM".equalsIgnoreCase(time);
        for (String slot : availableTimes) {
            LocalTime slotTime = LocalTime.parse(slot);
            if (isAM && slotTime.isBefore(LocalTime.NOON)) {
                return true;
            } else if (!isAM && slotTime.isAfter(LocalTime.NOON)) {
                return true;
            }
        }
        return false;
    }

// 13. **filterDoctorByNameAndTime Method**:
//    - Filters doctors based on their name and the specified time period (AM/PM).
//    - Fetches doctors based on partial name matching and filters the results to include only those available during the specified time period.
//    - Instruction: Ensure that the method correctly filters doctors based on the given name and time of day (AM/PM).

    @Transactional
    public List<Doctor> filterDoctorByNameAndTime(String name, String time) {
        try {
            List<Doctor> doctors = findDoctorByName(name);
            return filterDoctorByTime(doctors, time);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

// 14. **filterDoctorByNameAndSpecility Method**:
//    - Filters doctors by name and specialty.
//    - It ensures that the resulting list of doctors matches both the name (case-insensitive) and the specified specialty.
//    - Instruction: Ensure that both name and specialty are considered when filtering doctors.

    @Transactional
    public List<Doctor> filterDoctorByNameAndSpecialty(String name, String specialty) {
        try {
            return doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

// 15. **filterDoctorByTimeAndSpecility Method**:
//    - Filters doctors based on their specialty and availability during a specific time period (AM/PM).
//    - Fetches doctors based on the specified specialty and filters them based on their available time slots for AM/PM.
//    - Instruction: Ensure the time filtering is accurately applied based on the given specialty and time period (AM/PM).

    @Transactional
    public List<Doctor> filterDoctorBySpecialtyAndTime(String specialty, String time) {
        try {
            List<Doctor> doctors = filterDoctorBySpecialty(specialty);
            return filterDoctorByTime(doctors, time);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

// 16. **filterDoctorBySpecility Method**:
//    - Filters doctors based on their specialty.
//    - This method fetches all doctors matching the specified specialty and returns them.
//    - Instruction: Make sure the filtering logic works for case-insensitive specialty matching.

    @Transactional
    public List<Doctor> filterDoctorBySpecialty(String specialty) {
        try {
            return doctorRepository.findBySpecialtyIgnoreCase(specialty);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

// 17. **filterDoctorsByTime Method**:
//    - Filters all doctors based on their availability during a specific time period (AM/PM).
//    - The method checks all doctors' available times and returns those available during the specified time period.
//    - Instruction: Ensure proper filtering logic to handle AM/PM time periods.

    @Transactional
    public List<Doctor> filterDoctorsByTime(String time){
        try {
            List<Doctor> allDoctors = getDoctors();
            return filterDoctorByTime(allDoctors, time);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    public Optional<Doctor> getDoctorById(Long doctorId) {
        // TODO Auto-generated method stub;
        return doctorRepository.findById(doctorId);
    }


}
