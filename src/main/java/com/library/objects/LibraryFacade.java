package com.library.objects;

import com.library.dao.interfaces.BookDao;
import com.library.entities.Author;
import com.library.entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LibraryFacade {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private SearchCriteria searchCriteria;

    private List<Book> books;

    public List<Book> getBooks() {
        if(books==null){
            books=bookDao.getBooks();
        }
        return books;
    }
    public void searchBooksByLetter(){
        books=bookDao.getBooks(searchCriteria.getLetter());
    }
    public void searchBooksByGenre(){
        books=bookDao.getBooks(searchCriteria.getGenre());
    }
    public void searchBooksByText(){
        switch (searchCriteria.getSearchType()){
            case TITLE:
                books=bookDao.getBooks(searchCriteria.getText());
                break;
            case AUTHOR:
                books=bookDao.getBooks(new Author(searchCriteria.getText()));
                break;
        }
    }
}
