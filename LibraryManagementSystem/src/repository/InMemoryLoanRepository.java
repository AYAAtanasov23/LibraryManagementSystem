package repository;

import models.Loan;
import java.util.ArrayList;
import java.util.List;

public class InMemoryLoanRepository implements LoanRepository {
    private final List<Loan> loans = new ArrayList<>();

    @Override
    public void save(Loan loan) {
        loans.add(loan);
    }

    @Override
    public List<Loan> findAll() {
        return new ArrayList<>(loans);
    }

    @Override
    public Loan findActiveByIsbn(String isbn) {
        for (Loan loan : loans) {
            if (!loan.isReturned() && loan.getBook().getIsbn().equals(isbn)) {
                return loan;
            }
        }
        return null;
    }

    @Override
    public int countActiveByStudent(int studentId) {
        int count = 0;
        for (Loan loan : loans) {
            if (!loan.isReturned() && loan.getStudent().getId() == studentId) {
                count++;
            }
        }
        return count;
    }
}