package com.tej.JooQDemo;

import com.tej.JooQDemo.jooq.sample.model.Tables;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Book;
import com.tej.JooQDemo.jooq.sample.model.tables.records.BookRecord;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.ResultQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookService {

    @Autowired
    DSLContext context;

    private final TransactionalRunner transactionRunner;

    public List<Book> getBooks(){
        final List<Book> books = new ArrayList<>();
        ResultQuery<BookRecord> resQuery = context.selectFrom(Tables.BOOK);
        transactionRunner.readOnlyTransaction(() -> {
            try(final Cursor<BookRecord> bookRecords = resQuery.fetchSize(2).fetchLazy()) {
                while (bookRecords.hasNext()) {
                    List<Book> into = bookRecords.fetchNext(2).into(Book.class);
                    System.out.println("Num Records: " + into.size());
                    books.addAll(into);
                }
            }
        });
//        for (int i=10; i<100; i++) {
//            final Book b = new Book(i, "title" + i, "author" + i);
//            insertBook(b);
//        }
        return books;
    }

    public void insertBook(Book book){
        System.out.println(book.getTitle());
        context
            .insertInto(Tables.BOOK, Tables.BOOK.ID, Tables.BOOK.TITLE, Tables.BOOK.AUTHOR)
            .values(book.getId(), book.getTitle(), book.getAuthor())
            .execute();
    }
}
