# 📚 Tuition Management App

An Android-based Tuition Management System that simplifies attendance tracking, assignment handling, course material sharing, and student-teacher communication using **Firebase** as the backend.

---

## 🚀 Features

### 👤 Admin
- Login using email/password (Firebase Auth)
- Full CRUD operations for:
  - Students
  - Teachers
- Assign:
  - Courses to teachers
  - Teachers to students
- Send in-app notifications to students upon assignment

### 👨‍🏫 Teacher
- Take **QR-based student attendance**
- Upload **assignments** and **results**
- Upload **course materials (PDF)**
- Manage profile (name, subject, experience, address, Google Maps location)

### 👨‍🎓 Student
- View:
  - Attendance records
  - Assignments and results
  - Course materials (PDF)
- Submit assignments
- Receive in-app notifications from admin/teachers

---

## 🛠️ Tech Stack

### Frontend
- **Java (Android SDK)**
- **XML layouts**
- **Fragments, Activities, RecyclerViews**
- **Intents & Navigation**

### Backend
- **Firebase Authentication** (Email/Password login)
- **Firebase Firestore** (CRUD operations, data storage)
- **Firebase Storage** (PDF file uploads)
- **In-App Notification System** (Firestore-based)

---

## 🧪 Firebase Structure

### 🔐 Authentication
- Email/Password login for Admin, Teacher, and Student roles

### 🔥 Firestore Collections
- AssignmentCourses 
- attendance 
- course_assignments
- courses
- materials
- notifications
- students
- teachers
- users


---

## 📷 Screenshots

![teacher_viewAttendance](https://github.com/user-attachments/assets/88175b3e-b59c-48aa-89a1-3e502e343bdc)
![login](https://github.com/user-attachments/assets/ef82bd34-b044-433b-ac35-381812c27883)
![admin_addUser](https://github.com/user-attachments/assets/15567deb-fbe9-4698-8d28-a2a1a288afe1)
![admin_assign](https://github.com/user-attachments/assets/765ee85b-9afc-4eed-89fb-f9e35bcb55d9)
![student_attendanceMark](https://github.com/user-attachments/assets/89a7a572-aba8-46de-9982-24f9e57dc035)
![teacher_uploadMaterials](https://github.com/user-attachments/assets/f23f4df0-32b6-48ad-b362-9b8aee954bbe)
![student_notification](https://github.com/user-attachments/assets/9ab49df9-5a0d-4b54-9215-14e6be439de5)
![student_profile](https://github.com/user-attachments/assets/36f3b29d-ac8f-4e58-a73d-d3dd207cf7e3)
![student_lectureMaterial](https://github.com/user-attachments/assets/2b7737f4-61e9-497b-b085-09dcc04ce5f0)
![teacher_takingAttendance](https://github.com/user-attachments/assets/ebbfba3e-364d-408f-b70d-f146a404ae5b)


---
## 📌 Future Improvements
-Push notifications (via Firebase Cloud Messaging)
- Admin analytics dashboard
- Material file types other than PDF
- Calendar-based attendance overview

---
## 🧰 How to Run

1. Clone the repo:
   ```bash
   git clone https://github.com/your-username/tuition-management-app.git

2. Open in Android Studio

3. Connect to Firebase:
- Add your google-services.json to the app/ directory
- Enable Auth, Firestore, and Storage in the Firebase console

4. Sync Gradle and Run on Emulator/Device



