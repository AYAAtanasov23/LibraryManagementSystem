package service;

import service.FakeRepos.FakeBookRepository;
import models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {
    private FakeBookRepository bookRepo;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepo = new FakeBookRepository();
        bookService = new BookService(bookRepo);
    }

    @Test
    void addBook_validBook_isSaved() {
        // Arrange
        Book book = new Book("Dune", "Frank Herbert", "111", 1965);

        // Act
        bookService.addBook(book);

        // Assert
        assertEquals(1, bookRepo.findAll().size());
        assertSame(book, bookRepo.findByIsbn("111"));
    }

    @Test
    void addBook_emptyTitle_isRejected() {
        // Arrange
        Book book = new Book("   ", "Someone", "222", 2000);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(book));
        assertTrue(bookRepo.findAll().isEmpty());
    }

    @Test
    void addBook_duplicateIsbn_isRejected() {
        // Arrange
        bookService.addBook(new Book("Dune", "Frank Herbert", "111", 1965));
        Book duplicate = new Book("Other title", "Other author", "111", 1999);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(duplicate));
        assertEquals(1, bookRepo.findAll().size());
    }

    @Test
    void deleteBook_borrowedBook_isRejected() {
        // Arrange
        Book book = new Book("Dune", "Frank Herbert", "111", 1965);
        bookService.addBook(book);
        book.setAvailable(false);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> bookService.deleteBook("111"));
        assertEquals(1, bookRepo.findAll().size());
    }

    @Test
    void deleteBook_availableBook_isRemoved() {
        // Arrange
        bookService.addBook(new Book("Dune", "Frank Herbert", "111", 1965));

        // Act
        bookService.deleteBook("111");

        // Assert
        assertTrue(bookRepo.findAll().isEmpty());
    }
}