package repository;

import models.Loan;
import java.util.List;

public interface LoanRepository {
    void save(Loan loan);
    List<Loan> findAll();
    Loan findActiveByIsbn(String isbn);
    int countActiveByStudent(int studentId);
}