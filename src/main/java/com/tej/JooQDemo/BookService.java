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
        ResultQuery<BookRecord> resQuery = context.selectFrom(Tables.BOOK).fetchSize(2);
        transactionRunner.readOnlyTransaction(() -> {
            try(final Cursor<BookRecord> bookRecords = resQuery.fetchLazy()) {
                while (bookRecords.hasNext()) {
                    List<Book> into = bookRecords.fetch().into(Book.class);
                    System.out.println("Num Records: " + into.size());
                    books.addAll(into);
                }
            }
        });
        return books;
    }

    public void insertBook(Book book){
        System.out.println(book.getTitle());
        context
            .insertInto(Tables.BOOK, Tables.BOOK.TITLE, Tables.BOOK.AUTHOR)
            .values(book.getTitle(), book.getAuthor())
            .execute();
    }
}
