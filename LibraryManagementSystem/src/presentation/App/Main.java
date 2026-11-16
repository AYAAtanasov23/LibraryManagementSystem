package presentation.App;

import models.Student;
import presentation.controller.BookController;
import presentation.controller.LoanController;
import repository.BookRepository;
import repository.InMemoryBookRepository;
import repository.InMemoryLoanRepository;
import repository.InMemoryStudentRepository;
import repository.LoanRepository;
import repository.StudentRepository;
import service.BookService;
import service.LoanService;

public class Main {
    public static void main(String[] args) {
        BookRepository bookRepo = new InMemoryBookRepository();
        StudentRepository studentRepo = new InMemoryStudentRepository();
        LoanRepository loanRepo = new InMemoryLoanRepository();

        studentRepo.save(new Student(1, "Maria"));
        studentRepo.save(new Student(2, "Ivan"));
        studentRepo.save(new Student(3, "Alex"));

        BookService bookService = new BookService(bookRepo);
        LoanService loanService = new LoanService(bookRepo, studentRepo, loanRepo);

        BookController bookController = new BookController(bookService);
        LoanController loanController = new LoanController(loanService);

        LibraryMenu menu = new LibraryMenu(bookController, loanController);
        menu.start();
    }
}