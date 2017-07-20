package com.library.dao.interfaces;

import com.library.entities.Author;
import com.library.entities.Book;
import com.library.entities.Genre;

import java.util.List;

public interface BookDao {
    List<Book> getBooks();
    List<Book> getBooks(Author author);
    List<Book> getBooks(String bookName);
    List<Book> getBooks(Genre genre);
    List<Book> getBooks(Character letter);
    Object getFieldValue(Long id, String fieldName);
}
