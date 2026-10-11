package com.airtribe.learntrack;

import java.util.List;
import java.util.Scanner;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.service.StudentService;
import com.airtribe.learntrack.service.CourseService;
import com.airtribe.learntrack.service.EnrollmentService;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final StudentService STUDENT_SERVICE = new StudentService();
    private static final CourseService COURSE_SERVICE = new CourseService();
    private static final EnrollmentService ENROLLMENT_SERVICE =
            new EnrollmentService(STUDENT_SERVICE, COURSE_SERVICE);

    public static void main(String[] args) {
        boolean running = true;
        System.out.println("=== Welcome to LearnTrack ===");
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1: addStudent(); break;
                    case 2: listStudents(); break;
                    case 3: searchStudent(); break;
                    case 4: changeStudentStatus(false); break;
                    case 5: changeStudentStatus(true); break;
                    case 6: addCourse(); break;
                    case 7: listCourses(); break;
                    case 8: changeCourseStatus(); break;
                    case 9: enrollStudent(); break;
                    case 10: viewStudentEnrollments(); break;
                    case 11: changeEnrollmentStatus(); break;
                    case 12: listEnrollments(); break;
                    case 0: running = false; System.out.println("Goodbye!"); break;
                    default: System.out.println("Invalid menu option. Please choose a listed option.");
                }
            } catch (EntityNotFoundException | IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
            System.out.println();
        }
        SCANNER.close();
    }

    private static void printMenu() {
        System.out.println("----- Main Menu -----");
        System.out.println("1. Add student");
        System.out.println("2. View all students");
        System.out.println("3. Search student by ID");
        System.out.println("4. Deactivate student");
        System.out.println("5. Activate student");
        System.out.println("6. Add course");
        System.out.println("7. View all courses");
        System.out.println("8. Activate/deactivate course");
        System.out.println("9. Enroll student in course");
        System.out.println("10. View enrollments for a student");
        System.out.println("11. Update enrollment status");
        System.out.println("12. View all enrollments");
        System.out.println("0. Exit");
    }

    private static void addStudent() {
        String first = readRequired("First name: ");
        String last = readRequired("Last name: ");
        String email = readLine("Email (optional): ");
        String batch = readRequired("Batch: ");
        Student student = STUDENT_SERVICE.addStudent(first, last, email, batch);
        System.out.println("Student created: " + student);
    }

    private static void listStudents() {
        List<Student> students = STUDENT_SERVICE.listStudents();
        if (students.isEmpty()) { System.out.println("No students found."); return; }
        for (Student student : students) System.out.println(student);
    }

    private static void searchStudent() throws EntityNotFoundException {
        System.out.println(STUDENT_SERVICE.findStudentById(readInt("Student ID: ")));
    }

    private static void changeStudentStatus(boolean active) throws EntityNotFoundException {
        int id = readInt("Student ID: ");
        if (active) STUDENT_SERVICE.activateStudent(id);
        else STUDENT_SERVICE.deactivateStudent(id);
        System.out.println("Student status updated.");
    }

    private static void addCourse() {
        String name = readRequired("Course name: ");
        String description = readRequired("Description: ");
        int duration = readInt("Duration in weeks: ");
        if (duration <= 0) throw new IllegalArgumentException("Duration must be greater than zero.");
        System.out.println("Course created: " + COURSE_SERVICE.addCourse(name, description, duration));
    }

    private static void listCourses() {
        List<Course> courses = COURSE_SERVICE.listCourses();
        if (courses.isEmpty()) { System.out.println("No courses found."); return; }
        for (Course course : courses) System.out.println(course);
    }

    private static void changeCourseStatus() throws EntityNotFoundException {
        int id = readInt("Course ID: ");
        String answer = readRequired("Activate this course? (yes/no): ");
        if (!"yes".equalsIgnoreCase(answer) && !"no".equalsIgnoreCase(answer)) {
            throw new IllegalArgumentException("Please enter yes or no.");
        }
        COURSE_SERVICE.setCourseActive(id, "yes".equalsIgnoreCase(answer));
        System.out.println("Course status updated.");
    }

    private static void enrollStudent() throws EntityNotFoundException {
        int studentId = readInt("Student ID: ");
        int courseId = readInt("Course ID: ");
        System.out.println("Enrollment created: " + ENROLLMENT_SERVICE.enrollStudent(studentId, courseId));
    }

    private static void viewStudentEnrollments() throws EntityNotFoundException {
        int studentId = readInt("Student ID: ");
        List<Enrollment> enrollments = ENROLLMENT_SERVICE.getEnrollmentsForStudent(studentId);
        if (enrollments.isEmpty()) { System.out.println("No enrollments found for this student."); return; }
        for (Enrollment enrollment : enrollments) System.out.println(enrollment);
    }

    private static void changeEnrollmentStatus() throws EntityNotFoundException {
        int id = readInt("Enrollment ID: ");
        String status = readRequired("New status (ACTIVE/COMPLETED/CANCELLED): ").toUpperCase();
        ENROLLMENT_SERVICE.updateEnrollmentStatus(id, status);
        System.out.println("Enrollment status updated.");
    }

    private static void listEnrollments() {
        List<Enrollment> enrollments = ENROLLMENT_SERVICE.listEnrollments();
        if (enrollments.isEmpty()) { System.out.println("No enrollments found."); return; }
        for (Enrollment enrollment : enrollments) System.out.println(enrollment);
    }

    private static int readInt(String prompt) {
        while (true) {
            String value = readLine(prompt);
            try { return Integer.parseInt(value.trim()); }
            catch (NumberFormatException ex) { System.out.println("Please enter a valid whole number."); }
        }
    }

    private static String readRequired(String prompt) {
        while (true) {
            String value = readLine(prompt).trim();
            if (!value.isEmpty()) return value;
            System.out.println("This field cannot be empty.");
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine();
    }
}
