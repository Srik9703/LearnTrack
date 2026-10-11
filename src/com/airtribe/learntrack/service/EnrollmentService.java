package com.airtribe.learntrack.service;

import java.util.ArrayList;
import java.util.List;
import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.util.IdGenerator;

public class EnrollmentService {
    private final ArrayList<Enrollment> enrollments = new ArrayList<Enrollment>();
    private final StudentService studentService;
    private final CourseService courseService;

    public EnrollmentService(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    public Enrollment enrollStudent(int studentId, int courseId) throws EntityNotFoundException {
        Student student = studentService.findStudentById(studentId);
        Course course = courseService.findCourseById(courseId);
        if (!student.isActive()) throw new IllegalArgumentException("Cannot enroll an inactive student.");
        if (!course.isActive()) throw new IllegalArgumentException("Cannot enroll in an inactive course.");
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudentId() == studentId && enrollment.getCourseId() == courseId
                    && "ACTIVE".equals(enrollment.getStatus())) {
                throw new IllegalArgumentException("This student is already actively enrolled in this course.");
            }
        }
        Enrollment enrollment = new Enrollment(IdGenerator.getNextEnrollmentId(), studentId, courseId);
        enrollments.add(enrollment);
        return enrollment;
    }

    public List<Enrollment> listEnrollments() { return new ArrayList<Enrollment>(enrollments); }

    public List<Enrollment> getEnrollmentsForStudent(int studentId) throws EntityNotFoundException {
        studentService.findStudentById(studentId);
        ArrayList<Enrollment> result = new ArrayList<Enrollment>();
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudentId() == studentId) result.add(enrollment);
        }
        return result;
    }

    public void updateEnrollmentStatus(int enrollmentId, String status) throws EntityNotFoundException {
        if (!"ACTIVE".equals(status) && !"COMPLETED".equals(status) && !"CANCELLED".equals(status)) {
            throw new IllegalArgumentException("Status must be ACTIVE, COMPLETED, or CANCELLED.");
        }
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getId() == enrollmentId) {
                enrollment.setStatus(status);
                return;
            }
        }
        throw new EntityNotFoundException("Enrollment with ID " + enrollmentId + " was not found.");
    }
}
