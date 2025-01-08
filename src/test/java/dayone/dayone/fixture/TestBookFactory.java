package dayone.dayone.fixture;

import dayone.dayone.book.entity.Book;
import dayone.dayone.book.entity.repository.BookRepository;
import dayone.dayone.book.entity.value.Author;
import dayone.dayone.book.entity.value.Publisher;
import dayone.dayone.book.entity.value.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestBookFactory {

    @Autowired
    private BookRepository bookRepository;

    public Book createBook(String title, String author, String publisher) {
        final Book book = new Book(null, new Title(title), new Author(author), new Publisher(publisher), "이미지", "1abdcs3csd");
        return bookRepository.save(book);
    }
}
