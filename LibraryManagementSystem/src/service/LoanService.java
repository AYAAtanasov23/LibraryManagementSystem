package service;

import models.Book;
import models.Loan;
import models.Student;
import repository.BookRepository;
import repository.LoanRepository;
import repository.StudentRepository;

public class LoanService {
    private static final int MAX_ACTIVE_LOANS = 3;

    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final LoanRepository loanRepository;

    public LoanService(BookRepository bookRepository, StudentRepository studentRepository,
                       LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
        this.loanRepository = loanRepository;
    }

    public void borrowBook(String isbn, int studentId) {
        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found");
        }
        if (!book.isAvailable()) {
            throw new IllegalArgumentException("Book is already borrowed");
        }
        if (loanRepository.countActiveByStudent(studentId) >= MAX_ACTIVE_LOANS) {
            throw new IllegalArgumentException("Student already has 3 unreturned books");
        }

        loanRepository.save(new Loan(book, student));
        book.setAvailable(false);
    }

    public void returnBook(String isbn) {
        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        Loan loan = loanRepository.findActiveByIsbn(isbn);
        if (loan == null) {
            throw new IllegalArgumentException("This book is not borrowed");
        }

        loan.setReturned(true);
        book.setAvailable(true);
    }
}