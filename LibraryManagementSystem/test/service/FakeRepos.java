package service;

import models.Book;
import models.Loan;
import models.Student;
import repository.BookRepository;
import repository.LoanRepository;
import repository.StudentRepository;
import java.util.ArrayList;
import java.util.List;

public class FakeRepos {

    public static class FakeBookRepository implements BookRepository {
        private final List<Book> books = new ArrayList<>();

        @Override public void save(Book b) { books.add(b); }
        @Override public List<Book> findAll() { return new ArrayList<>(books); }
        @Override public void delete(Book b) { books.remove(b); }

        @Override
        public Book findByIsbn(String isbn) {
            for (Book b : books) {
                if (b.getIsbn().equals(isbn)) return b;
            }
            return null;
        }

        @Override
        public List<Book> findByTitle(String text) {
            List<Book> result = new ArrayList<>();
            for (Book b : books) {
                if (b.getTitle().toLowerCase().contains(text.toLowerCase())) result.add(b);
            }
            return result;
        }
    }

    public static class FakeStudentRepository implements StudentRepository {
        private final List<Student> students = new ArrayList<>();

        @Override public void save(Student s) { students.add(s); }
        @Override public List<Student> findAll() { return new ArrayList<>(students); }

        @Override
        public Student findById(int id) {
            for (Student s : students) {
                if (s.getId() == id) return s;
            }
            return null;
        }
    }

    public static class FakeLoanRepository implements LoanRepository {
        private final List<Loan> loans = new ArrayList<>();

        @Override public void save(Loan l) { loans.add(l); }
        @Override public List<Loan> findAll() { return new ArrayList<>(loans); }

        @Override
        public Loan findActiveByIsbn(String isbn) {
            for (Loan l : loans) {
                if (!l.isReturned() && l.getBook().getIsbn().equals(isbn)) return l;
            }
            return null;
        }

        @Override
        public int countActiveByStudent(int studentId) {
            int count = 0;
            for (Loan l : loans) {
                if (!l.isReturned() && l.getStudent().getId() == studentId) count++;
            }
            return count;
        }
    }
}