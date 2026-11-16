package repository;

import models.Book;
import java.util.List;

public interface BookRepository {
    void save(Book book);
    List<Book> findAll();
    Book findByIsbn(String isbn);
    List<Book> findByTitle(String text);
    void delete(Book book);
}