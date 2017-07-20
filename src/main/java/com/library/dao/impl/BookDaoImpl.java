package com.library.dao.impl;

import com.library.dao.interfaces.BookDao;
import com.library.entities.Author;
import com.library.entities.Book;
import com.library.entities.Genre;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class BookDaoImpl implements BookDao {

    @Autowired
    private SessionFactory sessionFactory;

    private ProjectionList bookProjection;

    public BookDaoImpl() {
        bookProjection = Projections.projectionList();
        bookProjection.add(Projections.property("id"), "id");
        bookProjection.add(Projections.property("name"), "name");
        bookProjection.add(Projections.property("image"), "image");
        bookProjection.add(Projections.property("genre"), "genre");
        bookProjection.add(Projections.property("pageCount"), "pageCount");
        bookProjection.add(Projections.property("isbn"), "isbn");
        bookProjection.add(Projections.property("publisher"), "publisher");
        bookProjection.add(Projections.property("author"), "author");
        bookProjection.add(Projections.property("publishYear"), "publishYear");
        bookProjection.add(Projections.property("descr"), "descr");
    }

    @Transactional
    @Override
    public List<Book> getBooks() {
        List<Book> books=createBookList(createBookCriteria());
        return books;
    }

    @Transactional
    @Override
    public List<Book> getBooks(Author author) {
        List<Book> books=createBookList(createBookCriteria().add(Restrictions.ilike("author.fio",
                author.getFio(),MatchMode.ANYWHERE)));
        return books;
    }

    @Transactional
    @Override
    public List<Book> getBooks(String bookName) {
        List<Book> books=createBookList(createBookCriteria().add(Restrictions.ilike("b.name",
                bookName,MatchMode.ANYWHERE)));
        return books;
    }

    @Transactional
    @Override
    public List<Book> getBooks(Genre genre) {
        List<Book> books = createBookList(createBookCriteria().
                add(Restrictions.eq("genre.id", genre.getId())));
        return books;
    }

    @Transactional
    @Override
    public List<Book> getBooks(Character letter) {
        List<Book> books = createBookList(createBookCriteria().
                add(Restrictions.ilike("b.name", letter.toString(), MatchMode.START)));
        return books;
    }

    private DetachedCriteria createBookCriteria(){
        DetachedCriteria bookListCriteria=DetachedCriteria.forClass(Book.class,"b");
        createAliases(bookListCriteria);
        return bookListCriteria;
    }

    private void createAliases(DetachedCriteria criteria){
        criteria.createAlias("b.author","author");
        criteria.createAlias("b.genre","genre");
        criteria.createAlias("b.publisher","publisher");
    }

    private List<Book> createBookList(DetachedCriteria bookListCriteria){
        Criteria criteria=bookListCriteria.getExecutableCriteria(sessionFactory.getCurrentSession());
        criteria.addOrder(Order.asc("b.name")).setProjection(bookProjection)
                .setResultTransformer(Transformers.aliasToBean(Book.class));
        return criteria.list();
    }
}
