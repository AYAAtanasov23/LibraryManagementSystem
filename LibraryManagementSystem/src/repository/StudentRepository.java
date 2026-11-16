package repository;

import models.Student;
import java.util.List;

public interface StudentRepository {
    void save(Student student);
    List<Student> findAll();
    Student findById(int id);
}