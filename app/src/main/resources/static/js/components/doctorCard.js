import { showBookingOverlay } from './loggedPatient.js';
import { deleteDoctor } from './docotrServices.js';
import { fetchPatientDetails } from './patientServices.js';

/**
 * Creates and returns a DOM element for a single doctor card
 * @param {Object} doctor - The doctor data object
 * @returns {HTMLElement}
 */
export function createDoctorCard(doctor) {
  // 1. Create the main container for the doctor card
  const card = document.createElement('div');
  card.className = 'doctor-card';

  // 2. Retrieve the current user role and token from localStorage
  const userRole = localStorage.getItem('role'); // e.g., 'admin', 'patient', or null
  const token = localStorage.getItem('token');

  // 3. Create a div to hold doctor information
  const doctorInfo = document.createElement('div');
  doctorInfo.className = 'doctor-info';

  // 4. Create and set doctor details
  const name = document.createElement('h3');
  name.textContent = `Dr. ${doctor.name}`;

  const spec = document.createElement('p');
  spec.className = 'specialization';
  spec.textContent = doctor.specialization;

  const email = document.createElement('p');
  email.className = 'email';
  email.textContent = doctor.email;

  // 5. Create and list available appointment times
  const timesList = document.createElement('ul');
  timesList.className = 'appointment-times';
  doctor.availableTimes?.forEach(time => {
    const li = document.createElement('li');
    li.textContent = time;
    timesList.appendChild(li);
  });

  // 6. Append info to container
  doctorInfo.append(name, spec, email, timesList);

  // 7. Create container for card action buttons
  const actionsContainer = document.createElement('div');
  actionsContainer.className = 'card-actions';

  // === ADMIN ROLE ACTIONS ===
  if (userRole === 'admin') {
    const deleteBtn = document.createElement('button');
    deleteBtn.className = 'btn-delete';
    deleteBtn.textContent = 'Delete Doctor';

    deleteBtn.addEventListener('click', async () => {
      if (confirm(`Are you sure you want to delete Dr. ${doctor.name}?`)) {
        try {
          const result = await deleteDoctor(doctor.id, token);
          if (result.success) {
            alert('Doctor removed successfully');
            card.remove(); // Remove card from UI
          }
        } catch (err) {
          console.error('Delete failed:', err);
        }
      }
    });
    actionsContainer.appendChild(deleteBtn);
  }

  // === PATIENT (NOT LOGGED-IN) ROLE ACTIONS ===
  else if (!token) {
    const bookBtn = document.createElement('button');
    bookBtn.textContent = 'Book Now';
    bookBtn.addEventListener('click', () => {
      alert('Please log in to book an appointment.');
      window.location.href = '/login.html';
    });
    actionsContainer.appendChild(bookBtn);
  }

  // === LOGGED-IN PATIENT ROLE ACTIONS === 
  else if (userRole === 'patient') {
    const bookBtn = document.createElement('button');
    bookBtn.textContent = 'Book Now';

    bookBtn.addEventListener('click', async () => {
      try {
        const patientData = await fetchPatientDetails(token);
        // Show booking overlay UI with doctor and patient info
        showBookingOverlay(doctor, patientData);
      } catch (err) {
        console.error('Failed to initiate booking:', err);
        alert('Session expired. Please log in again.');
      }
    });
    actionsContainer.appendChild(bookBtn);
  }

  // 8. Final Assembly
  card.append(doctorInfo, actionsContainer);
  
  return card;
}

/*
Import the overlay function for booking appointments from loggedPatient.js

  Import the deleteDoctor API function to remove doctors (admin role) from docotrServices.js

  Import function to fetch patient details (used during booking) from patientServices.js

  Function to create and return a DOM element for a single doctor card
    Create the main container for the doctor card
    Retrieve the current user role from localStorage
    Create a div to hold doctor information
    Create and set the doctor’s name
    Create and set the doctor's specialization
    Create and set the doctor's email
    Create and list available appointment times
    Append all info elements to the doctor info container
    Create a container for card action buttons
    === ADMIN ROLE ACTIONS ===
      Create a delete button
      Add click handler for delete button
     Get the admin token from localStorage
        Call API to delete the doctor
        Show result and remove card if successful
      Add delete button to actions container
   
    === PATIENT (NOT LOGGED-IN) ROLE ACTIONS ===
      Create a book now button
      Alert patient to log in before booking
      Add button to actions container
  
    === LOGGED-IN PATIENT ROLE ACTIONS === 
      Create a book now button
      Handle booking logic for logged-in patient   
        Redirect if token not available
        Fetch patient data with token
        Show booking overlay UI with doctor and patient info
      Add button to actions container
   
  Append doctor info and action buttons to the car
  Return the complete doctor card element
*/
