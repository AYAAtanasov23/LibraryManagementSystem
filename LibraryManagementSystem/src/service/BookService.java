package service;

import models.Book;
import repository.BookRepository;
import java.util.ArrayList;
import java.util.List;

public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }
        if (bookRepository.findByIsbn(book.getIsbn()) != null) {
            throw new IllegalArgumentException("Book already exists");
        }
        bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchByTitle(String text) {
        return bookRepository.findByTitle(text);
    }

    public List<Book> getAvailableBooks() {
        List<Book> result = new ArrayList<>();
        for (Book b : bookRepository.findAll()) {
            if (b.isAvailable()) {
                result.add(b);
            }
        }
        return result;
    }

    public void editBook(String isbn, String title, String author, int year) {
        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        book.setTitle(title);
        book.setAuthor(author);
        book.setYear(year);
    }

    public void deleteBook(String isbn) {
        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        if (!book.isAvailable()) {
            throw new IllegalArgumentException("Cannot delete a borrowed book");
        }
        bookRepository.delete(book);
    }
}