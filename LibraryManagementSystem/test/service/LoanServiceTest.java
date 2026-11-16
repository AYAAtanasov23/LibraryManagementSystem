package service;

import models.Book;
import models.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FakeRepos.FakeBookRepository;
import service.FakeRepos.FakeLoanRepository;
import service.FakeRepos.FakeStudentRepository;

import static org.junit.jupiter.api.Assertions.*;

public class LoanServiceTest {
    private FakeBookRepository bookRepo;
    private FakeLoanRepository loanRepo;
    private LoanService service;

    @BeforeEach
    void setUp() {
        bookRepo = new FakeBookRepository();
        FakeStudentRepository studentRepo = new FakeStudentRepository();
        loanRepo = new FakeLoanRepository();
        service = new LoanService(bookRepo, studentRepo, loanRepo);

        studentRepo.save(new Student(1, "Maria"));
        studentRepo.save(new Student(2, "Ivan"));
        bookRepo.save(new Book("Book A", "Author", "A", 2001));
        bookRepo.save(new Book("Book B", "Author", "B", 2002));
        bookRepo.save(new Book("Book C", "Author", "C", 2003));
        bookRepo.save(new Book("Book D", "Author", "D", 2004));
    }

    @Test
    void borrowBook_availableBook_becomesBorrowed() {
        service.borrowBook("A", 1);
        assertFalse(bookRepo.findByIsbn("A").isAvailable());
        assertEquals(1, loanRepo.countActiveByStudent(1));
    }

    @Test
    void borrowBook_alreadyBorrowedBook_isRejected() {
        service.borrowBook("A", 1);
        assertThrows(IllegalArgumentException.class, () -> service.borrowBook("A", 2));
        assertEquals(0, loanRepo.countActiveByStudent(2));
    }

    @Test
    void borrowBook_fourthBookForStudent_isRejected() {
        service.borrowBook("A", 1);
        service.borrowBook("B", 1);
        service.borrowBook("C", 1);
        assertThrows(IllegalArgumentException.class, () -> service.borrowBook("D", 1));
        assertTrue(bookRepo.findByIsbn("D").isAvailable());
    }

    @Test
    void returnBook_borrowedBook_becomesAvailableAgain() {
        service.borrowBook("A", 1);
        service.returnBook("A");
        assertTrue(bookRepo.findByIsbn("A").isAvailable());
        assertEquals(0, loanRepo.countActiveByStudent(1));
    }

    @Test
    void returnBook_notBorrowedBook_isRejected() {
        assertThrows(IllegalArgumentException.class, () -> service.returnBook("A"));
    }
}