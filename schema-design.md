
## MySQL Database Design
### Table: patient
- id: INT, Primary Key, Auto Increment
- name: varchar, Not Null, size 200
- email: varchar, Not Null
- password: varchar, Not Null, min size 8
- phone: varchar, Not Null
- address: varchar, Not Null
------
### Table: doctor
- id: INT, Primary Key, Auto Increment
- name: varchar, Not Null, size 200
- email: varchar, Not Null
- speciality: varchar, Not Null
- password: varchar, Not Null, min size 8

-----
### Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- patient_id: INT, Foreign Key → patients(id)
- appointment_time: DATETIME, Not Null
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)

-----

### Table: admin
- id: INT, Primary Key, Auto Increment
- name: varchar, Not Null, size 200
- email: varchar, Not Null, valid email format
- password: varchar, Not Null, min size 8

### Table: available_time
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- start_time: DATETIME, Not Null
- end_time: DATETIME, Not Null
- -note: start and end time should not overlap for one doctor

## MongoDB Collection Design
### Collection: prescriptions
```json
{
  "_id": "ObjectId('64abc123456')",
  "patientName": "John Smith",
  "appointmentId": 51,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours.",
  "refillCount": 2,
  "pharmacy": {
    "name": "Walgreens SF",
    "location": "Market Street"
  }
}
```
### Collection: notes
```json
{
  "_id": "ObjectId('64abc123456')",
  "patientName": "John Smith",
  "appointmentId": 51,
  "notes": "The patient was in excrutiating pain"
}
```
