**Title:**  _As a [user role], I want [feature/goal], so that [reason]._
**Acceptance Criteria:**
1.  [Criteria 1]
2.  [Criteria 2]
3.  [Criteria 3]

    **Priority:**  [High/Medium/Low]  

    **Story Points:**  [Estimated Effort in Points]  

    **Notes:**
-   [Additional information or edge cases]
## Admin User Stories
### 1. Admin Login
**Title:** _As an **Admin**, I want to **log into the portal with my username and password**, so that I can **manage the platform securely**._
**Acceptance Criteria:**
1.  Secure login fields for username and password must be present.
2.  Access is granted only with valid administrative credentials.
3.  User is redirected to the admin dashboard upon successful authentication.

    **Priority:** High

    **Story Points:** 3
----------
### 2. Admin Logout
**Title:** _As an **Admin**, I want to **log out of the portal**, so that I can **protect system access**._
**Acceptance Criteria:**
1.  A "Logout" option is clearly visible on the dashboard.
2.  The user session is terminated immediately upon clicking logout.
3.  The system redirects the user back to the public login page.

    **Priority:** High

    **Story Points:** 1
----------
### 3. Add Doctor
**Title:** _As an **Admin**, I want to **add doctors to the portal**, so that they can manage patient consultations._
**Acceptance Criteria:**
1.  Admin can enter essential doctor details (Name, ID, Specialization).
2.  The system checks for duplicate entries before saving.
3.  A success message confirms the new doctor has been added.

    **Priority:** High

    **Story Points:** 5
----------
### 4. Delete Doctor Profile
**Title:** _As an **Admin**, I want to **delete a doctor's profile from the portal**, so that I can maintain an accurate staff list._
**Acceptance Criteria:**
1.  Admin can select and remove an existing doctor profile.
2.  A confirmation dialog appears to prevent accidental deletion.
3.  The profile is permanently removed from the active system database.

    **Priority:** Medium

    **Story Points:** 3
----------
### 5. Track Usage Statistics
**Title:** _As an **Admin**, I want to **run a stored procedure in MySQL CLI**, so that I can **get the number of appointments per month and track usage statistics**._
**Acceptance Criteria:**
1.  The admin can access the MySQL CLI interface within the environment.
2.  The specific stored procedure for monthly appointments executes correctly.
   3.  Results are displayed clearly to show monthly usage trends.
   
   **Priority:** Medium
   
   **Story Points:** 5
------------------
## Patient User Stories
### 1. Patient Registration
**Title:** _As a **Patient**, I want to **register for an account**, so that I can **access the portal services**._
**Acceptance Criteria:**
1.  User provides name, email, and password.
2.  System validates that the email is unique and valid.
3.  User receives a confirmation message upon successful registration.

    **Priority:** High

    **Story Points:** 5
----------
### 2. Book Appointment
**Title:** _As a **Patient**, I want to **book an appointment with a doctor**, so that I can **receive medical consultation**._
**Acceptance Criteria:**
1.  Patient can select a doctor and an available time slot.
2.  The system prevents double-booking for the same time slot.
3.  A confirmation email/notification is sent to the patient.

    **Priority:** High

    **Story Points:** 8
----------
### 3. View Medical Records
**Title:** _As a **Patient**, I want to **view my medical history**, so that I can **track my health progress**._
**Acceptance Criteria:**
1.  Patient can access a "Medical History" section.
2.  Records display past diagnoses, prescriptions, and lab results.
3.  Data is presented in a clear, chronological order.

    **Priority:** Medium

    **Story Points:** 5
----------
### 4. Cancel Appointment
**Title:** _As a **Patient**, I want to **cancel a scheduled appointment**, so that I can **free up the slot for others if I cannot attend**._
**Acceptance Criteria:**
1.  Patient can view a list of upcoming appointments and select "Cancel."
2.  System prompts for a confirmation before canceling.
3.  The time slot is immediately updated to "Available" for other users.

    **Priority:** Medium

    **Story Points:** 3
----------
### 5. Update Profile
**Title:** _As a **Patient**, I want to **update my contact information**, so that the **hospital has my current details**._
**Acceptance Criteria:**
1.  Patient can edit phone number, address, and emergency contact.
2.  Changes are saved and reflected immediately in the system.

    **Priority:** Low

    **Story Points:** 2
----------
## Doctor User Stories
### 1. View Appointment Schedule
**Title:** _As a **Doctor**, I want to **see my daily schedule**, so that I can **prepare for upcoming consultations**._
**Acceptance Criteria:**
1.  Doctor sees a list of all patients scheduled for the day.
2.  Schedule includes the patient's name and the appointment time.

    **Priority:** High

    **Story Points:** 3
----------
### 2. Access Patient History
**Title:** _As a **Doctor**, I want to **review a patient’s medical history during a visit**, so that I can **provide informed care**._
**Acceptance Criteria:**
1.  Doctor can click on a patient's name to see past visits and notes.
2.  Records load quickly and accurately.

    **Priority:** High

    **Story Points:** 5
----------
### 3. Write Prescriptions
**Title:** _As a **Doctor**, I want to **add a digital prescription to a patient's record**, so that they can **collect their medication**._
**Acceptance Criteria:**
1.  Doctor can enter medication name, dosage, and instructions.
2.  Prescription is saved to the patient's profile instantly.

    **Priority:** High

    **Story Points:** 5
----------
### 4. Set Availability
**Title:** _As a **Doctor**, I want to **mark my working hours and breaks**, so that **patients can only book me when I am available**._
**Acceptance Criteria:**
1.  Doctor can select specific days and times to block out.
2.  The portal prevents patient booking during these blocked times.

    **Priority:** Medium

    **Story Points:** 5
----------
### 5. Record Consultation Notes
**Title:** _As a **Doctor**, I want to **record notes during an appointment**, so that there is a **permanent record of the diagnosis**._
**Acceptance Criteria:**
1.  A text field is available for notes during the active appointment.
2.  Notes are encrypted and securely stored.

    **Priority:** Medium

    **Story Points:** 3
    
