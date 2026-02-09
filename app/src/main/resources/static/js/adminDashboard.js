import { getDoctors, filterDoctors, saveDoctor } from './services/doctorServices.js';
import { openModal } from './components/modals.js';
import { createDoctorCard } from './components/doctorCard.js';
import { showBookingOverlay } from './loggedPatient.js';

document.addEventListener("DOMContentLoaded", () => {
  loadDoctorCards();
  document.getElementById('addDocBtn').addEventListener('click', () => {
    openModal('addDoctor');
  });



  attachFilterListeners();
});
/**
document.addEventListener('DOMContentLoaded', () => {
    // 1. Initial Load
    loadDoctorCards();

    // 2. Event Listeners for Filtering
    const searchInput = document.getElementById('search-bar');
    const filters = document.querySelectorAll('.filter-dropdown');

    [searchInput, ...filters].forEach(el => {
        el.addEventListener('input', filterDoctorsOnChange);
    });

    // 3. Add Doctor Modal Trigger
    const addBtn = document.getElementById('add-doctor-btn');
    addBtn.addEventListener('click', () => openModal('addDoctor'));
});
*/

/**
 * Fetch and display all doctors
 */
async function loadDoctorCards() {
    try {
        const doctors = await getDoctors(); // From service layer
        renderDoctorCards(doctors);
    } catch (error) {
        console.error("Failed to load doctors:", error);
    }
}

/**
 * Filter logic triggered by UI changes
 */
async function filterDoctorsOnChange() {
    const name = document.getElementById('search-bar').value || null;
    const time = document.getElementById('time-filter').value || null;
    const specialty = document.getElementById('specialty-filter').value || null;

    try {
        const filtered = await filterDoctors(name, time, specialty);

        if (filtered && filtered.length > 0) {
            renderDoctorCards(filtered);
        } else {
            document.getElementById('doctor-container').innerHTML =
                `<p class="no-results">No doctors found with the given filters.</p>`;
        }
    } catch (error) {
        alert("Error filtering doctors: " + error.message);
    }
}

/**
 * Helper to clear and rebuild the doctor list
 */
function renderDoctorCards(doctors) {
    const container = document.getElementById('doctor-container');
    container.innerHTML = ''; // Clear current content

    doctors.forEach(doctor => {
        const card = createDoctorCard(doctor); // Function to return a DOM element
        container.appendChild(card);
    });
}

/**
 * Collect form data and save a new doctor
 */
async function adminAddDoctor() {
    const token = localStorage.getItem('authToken');
    if (!token) {
        alert("Session expired. Please log in again.");
        return;
    }

    const doctorData = {
        name: document.getElementById('new-doc-name').value,
        email: document.getElementById('new-doc-email').value,
        phone: document.getElementById('new-doc-phone').value,
        password: document.getElementById('new-doc-pass').value,
        specialty: document.getElementById('new-doc-specialty').value,
        availableTimes: document.getElementById('new-doc-times').value
    };

    try {
        const success = await saveDoctor(doctorData, token);
        if (success) {
            alert("Doctor added successfully!");
            closeModal('addDoctor');
            window.location.reload();
        }
    } catch (error) {
        alert("Failed to add doctor: " + error.message);
    }
}

/*
  This script handles the admin dashboard functionality for managing doctors:
  - Loads all doctor cards
  - Filters doctors by name, time, or specialty
  - Adds a new doctor via modal form


  Attach a click listener to the "Add Doctor" button
  When clicked, it opens a modal form using openModal('addDoctor')


  When the DOM is fully loaded:
    - Call loadDoctorCards() to fetch and display all doctors


  Function: loadDoctorCards
  Purpose: Fetch all doctors and display them as cards

    Call getDoctors() from the service layer
    Clear the current content area
    For each doctor returned:
    - Create a doctor card using createDoctorCard()
    - Append it to the content div

    Handle any fetch errors by logging them


  Attach 'input' and 'change' event listeners to the search bar and filter dropdowns
  On any input change, call filterDoctorsOnChange()


  Function: filterDoctorsOnChange
  Purpose: Filter doctors based on name, available time, and specialty

    Read values from the search bar and filters
    Normalize empty values to null
    Call filterDoctors(name, time, specialty) from the service

    If doctors are found:
    - Render them using createDoctorCard()
    If no doctors match the filter:
    - Show a message: "No doctors found with the given filters."

    Catch and display any errors with an alert


  Function: renderDoctorCards
  Purpose: A helper function to render a list of doctors passed to it

    Clear the content area
    Loop through the doctors and append each card to the content area


  Function: adminAddDoctor
  Purpose: Collect form data and add a new doctor to the system

    Collect input values from the modal form
    - Includes name, email, phone, password, specialty, and available times

    Retrieve the authentication token from localStorage
    - If no token is found, show an alert and stop execution

    Build a doctor object with the form values

    Call saveDoctor(doctor, token) from the service

    If save is successful:
    - Show a success message
    - Close the modal and reload the page

    If saving fails, show an error message
*/
