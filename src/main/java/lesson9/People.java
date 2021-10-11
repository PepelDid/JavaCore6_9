package lesson9;

import java.util.List;

public class People implements Student {
    private String name;
    private List<Course> courses;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Course> getAllCourses() {
        return courses;
    }

    enum Course {MATH, BIO, FYZ, LITER, LANG, HIST, SOC, POLIT, CHEM};
    public People(String name, List<Course> courses) {
        this.name = name;
        this.courses = courses;
    }




}
