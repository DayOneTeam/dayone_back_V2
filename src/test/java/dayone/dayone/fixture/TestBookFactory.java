package dayone.dayone.fixture;

import dayone.dayone.book.entity.Book;
import dayone.dayone.book.entity.repository.BookRepository;
import dayone.dayone.book.entity.value.Author;
import dayone.dayone.book.entity.value.Publisher;
import dayone.dayone.book.entity.value.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TestBookFactory {

    @Autowired
    private BookRepository bookRepository;

    public Book createBook(String title, String author, String publisher) {
        final Book book = new Book(null, new Title(title), new Author(author), new Publisher(publisher), "이미지", "1abdcs3csd");
        return bookRepository.save(book);
    }

    public List<Book> createNBook(final int cnt, final String title, final String author, final String publisher) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            books.add(createBook(title, author, publisher));
        }
        bookRepository.saveAll(books);
        return books;
    }
}
