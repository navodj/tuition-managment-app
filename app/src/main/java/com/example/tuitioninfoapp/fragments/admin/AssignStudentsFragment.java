package com.example.tuitioninfoapp.fragments.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.adapters.AssignmentAdapter;
import com.example.tuitioninfoapp.adapters.MultiSelectStudentAdapter;
import com.example.tuitioninfoapp.adapters.StudentListAdapter;
import com.example.tuitioninfoapp.models.Assignment;
import com.example.tuitioninfoapp.models.Course;
import com.example.tuitioninfoapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.example.tuitioninfoapp.services.NotificationService;

public class AssignStudentsFragment extends Fragment {

    private NotificationService notificationService;
    private Spinner spinnerCourses, spinnerTeachers;
    private RecyclerView rvStudents;
    private Button btnSave;
    private RecyclerView rvAssignments;

    private AssignmentAdapter assignmentAdapter;
    private List<Assignment> assignmentList = new ArrayList<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final List<Course> courseList = new ArrayList<>();
    private final List<User> teacherList = new ArrayList<>();
    private final List<User> studentList = new ArrayList<>();

    private MultiSelectStudentAdapter studentAdapter;
    private String selectedCourseId = "";
    private String selectedTeacherId = "";
    private List<String> preSelectedStudentIds = new ArrayList<>();
    private Course currentCourse = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assign_students, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notificationService = new NotificationService();

        // Initialize views
        spinnerCourses = view.findViewById(R.id.spinner_courses);
        spinnerTeachers = view.findViewById(R.id.spinner_teachers);
        rvStudents = view.findViewById(R.id.rv_students);
        btnSave = view.findViewById(R.id.btn_save_assignment);
        rvAssignments = view.findViewById(R.id.rv_assignments);

        // Setup RecyclerView for assignments
        rvAssignments.setLayoutManager(new LinearLayoutManager(getContext()));
        assignmentAdapter = new AssignmentAdapter();
        assignmentAdapter.setAllTeachers(teacherList);
        assignmentAdapter.setAllStudents(studentList);
        rvAssignments.setAdapter(assignmentAdapter);

        // Setup RecyclerView for student selection
        rvStudents.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load data
        loadAssignments();
        loadCourses();
        loadTeachers();
        loadStudents();

        notificationService = new NotificationService();

        // Course selection handler
        spinnerCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCourse = courseList.get(position);
                selectedCourseId = currentCourse.getId();
                preSelectedStudentIds = currentCourse.getStudentIds() != null ?
                        currentCourse.getStudentIds() : new ArrayList<>();

                // Update teacher spinner selection
                if (currentCourse.getTeacherId() != null) {
                    for (int i = 0; i < teacherList.size(); i++) {
                        if (teacherList.get(i).getId().equals(currentCourse.getTeacherId())) {
                            spinnerTeachers.setSelection(i);
                            selectedTeacherId = teacherList.get(i).getId();
                            break;
                        }
                    }
                }

                // Update student list
                updateStudentList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCourseId = "";
                currentCourse = null;
            }
        });

        // Teacher selection handler
        spinnerTeachers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTeacherId = teacherList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTeacherId = "";
            }
        });

        // Save button handler
        btnSave.setOnClickListener(v -> saveAssignments());
    }

    private void loadCourses() {
        db.collection("courses").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                courseList.clear();
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    Course course = doc.toObject(Course.class);
                    if (course != null) {
                        course.setId(doc.getId());
                        courseList.add(course);
                    }
                }

                // Populate course spinner
                ArrayAdapter<Course> courseAdapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        courseList
                );
                courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCourses.setAdapter(courseAdapter);

                // Select first course by default if available
                if (!courseList.isEmpty()) {
                    spinnerCourses.setSelection(0);
                }

            }

        });
    }



    private void loadTeachers() {
        db.collection("users")
                .whereEqualTo("role", "teacher")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        teacherList.clear();
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            User teacher = doc.toObject(User.class);
                            if (teacher != null) {
                                teacher.setId(doc.getId());
                                teacherList.add(teacher);
                            }
                        }

                        // Populate teacher spinner
                        ArrayAdapter<User> teacherAdapter = new ArrayAdapter<>(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                teacherList
                        );
                        teacherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerTeachers.setAdapter(teacherAdapter);

                        // Update assignment adapter
                        assignmentAdapter.setAllTeachers(teacherList);
                    }
                });
    }

    private void loadStudents() {
        db.collection("users")
                .whereEqualTo("role", "student")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        studentList.clear();
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            User student = doc.toObject(User.class);
                            if (student != null) {
                                student.setId(doc.getId());
                                studentList.add(student);
                            }
                        }
                        updateStudentList();

                        // Update assignment adapter
                        assignmentAdapter.setAllStudents(studentList);
                    }
                });
    }

    private void updateStudentList() {
        if (!studentList.isEmpty()) {
            studentAdapter = new MultiSelectStudentAdapter(studentList, preSelectedStudentIds);
            rvStudents.setAdapter(studentAdapter);
        }
    }

    private void saveAssignments() {
        if (selectedCourseId.isEmpty() || selectedTeacherId.isEmpty()) {
            Toast.makeText(getContext(), "Please select course and teacher", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show saving indicator
        Toast.makeText(getContext(), "Saving assignment...", Toast.LENGTH_SHORT).show();

        // First fetch teacher data asynchronously
        db.collection("users").document(selectedTeacherId).get()
                .addOnCompleteListener(teacherTask -> {
                    if (teacherTask.isSuccessful() && teacherTask.getResult() != null) {
                        DocumentSnapshot teacherDoc = teacherTask.getResult();
                        processBatchOperations(teacherDoc);
                    } else {
                        Toast.makeText(getContext(), "Failed to load teacher data", Toast.LENGTH_SHORT).show();
                        Log.e("AssignStudents", "Error fetching teacher", teacherTask.getException());
                    }
                });
    }

    private void processBatchOperations(DocumentSnapshot teacherDoc) {
        WriteBatch batch = db.batch();

        // 1. Create assignment document
        DocumentReference assignmentRef = db.collection("assignments").document();
        Assignment newAssignment = new Assignment();
        newAssignment.setId(assignmentRef.getId());
        newAssignment.setCourseId(selectedCourseId);
        newAssignment.setTeacherId(selectedTeacherId);
        newAssignment.setStudentIds(new ArrayList<>(studentAdapter.getSelectedStudents()));
        batch.set(assignmentRef, newAssignment);

        // 2. Update course
        DocumentReference courseRef = db.collection("courses").document(selectedCourseId);
        Map<String, Object> courseUpdates = new HashMap<>();
        courseUpdates.put("teacherId", selectedTeacherId);
        courseUpdates.put("studentIds", newAssignment.getStudentIds());
        batch.update(courseRef, courseUpdates);

        // 3. Update students' enrolled courses
        List<String> assignedStudentIds = newAssignment.getStudentIds();
        for (User student : studentList) {
            String studentId = student.getId();
            DocumentReference studentRef = db.collection("users").document(studentId);
            List<String> studentCourses = student.getCourses() != null ? new ArrayList<>(student.getCourses()) : new ArrayList<>();

            if (assignedStudentIds.contains(studentId)) {
                if (!studentCourses.contains(selectedCourseId)) {
                    studentCourses.add(selectedCourseId);
                    batch.update(studentRef, "courses", studentCourses);
                }
            } else {
                if (studentCourses.contains(selectedCourseId)) {
                    studentCourses.remove(selectedCourseId);
                    batch.update(studentRef, "courses", studentCourses);
                }
            }
        }

        // 4. Update teacher's course list
        if (teacherDoc.exists()) {
            User teacher = teacherDoc.toObject(User.class);
            if (teacher != null) {
                List<String> teacherCourses = teacher.getCourses() != null ? new ArrayList<>(teacher.getCourses()) : new ArrayList<>();
                if (!teacherCourses.contains(selectedCourseId)) {
                    teacherCourses.add(selectedCourseId);
                    batch.update(db.collection("users").document(selectedTeacherId), "courses", teacherCourses);
                }
            }
        }

        // 🔔 Commit and send notification
        batch.commit().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Assignment saved successfully", Toast.LENGTH_SHORT).show();
                loadAssignments();

                // Send notifications to assigned students
                for (String studentId : newAssignment.getStudentIds()) {
                    String msg = "You've been assigned to the course: " + currentCourse.getName();
                    notificationService.sendNotification(studentId, msg);
                }

            } else {
                Toast.makeText(getContext(), "Failed to save assignment", Toast.LENGTH_SHORT).show();
                Log.e("AssignStudents", "Batch commit failed", task.getException());
            }
        });
    }


    private void loadAssignments() {
        db.collection("assignments").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                assignmentList.clear();
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    Assignment assignment = doc.toObject(Assignment.class);
                    if (assignment != null) {
                        assignment.setId(doc.getId());
                        assignmentList.add(assignment);
                    }
                }
                assignmentAdapter.setAssignments(assignmentList);
            } else {
                Toast.makeText(getContext(), "Failed to load assignments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotificationsToStudents(String courseName, List<String> studentIds) {
        String title = "New Course Assigned";
        String message = "You have been assigned to course: " + courseName;
        long timestamp = System.currentTimeMillis();

        for (String studentId : studentIds) {
            // Save in Firestore (for NotificationsFragment)
            Map<String, Object> notification = new HashMap<>();
            notification.put("title", title);
            notification.put("message", message);
            notification.put("timestamp", timestamp);

            db.collection("users").document(studentId)
                    .collection("notifications").add(notification);

            // Send push notification (Firebase token logic should be handled in NotificationService)
            notificationService.sendNotificationToUser(studentId, title, message);
        }
    }


}