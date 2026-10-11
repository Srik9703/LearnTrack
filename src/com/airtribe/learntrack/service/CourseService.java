package com.airtribe.learntrack.service;

import java.util.ArrayList;
import java.util.List;
import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.util.IdGenerator;

public class CourseService {
    private final ArrayList<Course> courses = new ArrayList<Course>();

    public Course addCourse(String name, String description, int durationInWeeks) {
        Course course = new Course(IdGenerator.getNextCourseId(), name, description, durationInWeeks);
        courses.add(course);
        return course;
    }

    public List<Course> listCourses() { return new ArrayList<Course>(courses); }

    public Course findCourseById(int id) throws EntityNotFoundException {
        for (Course course : courses) {
            if (course.getId() == id) return course;
        }
        throw new EntityNotFoundException("Course with ID " + id + " was not found.");
    }

    public void setCourseActive(int id, boolean active) throws EntityNotFoundException {
        findCourseById(id).setActive(active);
    }
}
