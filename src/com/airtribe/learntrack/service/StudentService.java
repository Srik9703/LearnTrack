package com.airtribe.learntrack.service;

import java.util.ArrayList;
import java.util.List;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.util.IdGenerator;

public class StudentService {
    private final ArrayList<Student> students = new ArrayList<Student>();

    public Student addStudent(String firstName, String lastName, String email, String batch) {
        Student student = new Student(IdGenerator.getNextStudentId(), firstName, lastName, email, batch);
        students.add(student);
        return student;
    }

    // Overloaded method for students without an email address.
    public Student addStudent(String firstName, String lastName, String batch) {
        Student student = new Student(IdGenerator.getNextStudentId(), firstName, lastName, batch);
        students.add(student);
        return student;
    }

    public List<Student> listStudents() { return new ArrayList<Student>(students); }

    public Student findStudentById(int id) throws EntityNotFoundException {
        for (Student student : students) {
            if (student.getId() == id) return student;
        }
        throw new EntityNotFoundException("Student with ID " + id + " was not found.");
    }

    public void deactivateStudent(int id) throws EntityNotFoundException {
        findStudentById(id).setActive(false);
    }

    public void activateStudent(int id) throws EntityNotFoundException {
        findStudentById(id).setActive(true);
    }

    public void updateStudent(int id, String firstName, String lastName, String email, String batch)
            throws EntityNotFoundException {
        Student student = findStudentById(id);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setBatch(batch);
    }

    public void removeStudent(int id) throws EntityNotFoundException {
        students.remove(findStudentById(id));
    }
}
