package mk.finki.ukim.wp.lab;

import mk.finki.ukim.wp.lab.model.Author;
import mk.finki.ukim.wp.lab.model.AuthorFullname;
import mk.finki.ukim.wp.lab.model.Book;
import mk.finki.ukim.wp.lab.model.BookStore;
import mk.finki.ukim.wp.lab.model.exceptions.BookNotFoundException;
import mk.finki.ukim.wp.lab.model.exceptions.BookStoreNotFoundException;
import mk.finki.ukim.wp.lab.repository.jpa.AuthorRepository;
import mk.finki.ukim.wp.lab.repository.jpa.BookRepository;
import mk.finki.ukim.wp.lab.repository.jpa.BookStoreRepository;
import mk.finki.ukim.wp.lab.service.impl.BookServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookServiceTest {

    private BookRepository bookRepository;
    private BookStoreRepository bookStoreRepository;
    private AuthorRepository authorRepository;
    private BookServiceImpl bookService;

    private Book testBook;
    private Author testAuthor;


    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        bookStoreRepository = mock(BookStoreRepository.class);
        authorRepository = mock(AuthorRepository.class);
        bookService = new BookServiceImpl(bookRepository, bookStoreRepository, authorRepository);


        testBook = new Book("1234567890", "Test Book", "Fiction", 2024);
        testBook.setId(1L);

        testAuthor = new Author(new AuthorFullname("Eroll", "Sakipi"), "Biography", LocalDate.of(2002, 1, 1));
        testAuthor.setId(1L);


        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(testBook));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
    }


    @Test
    void testAddAuthorToBook_Success() {
        // testing adding author to book
        Author addedAuthor = bookService.addAuthorToBook(1L, "1234567890");

        assertNotNull(addedAuthor);
        assertTrue(testBook.getAuthorsfromBookClass().contains(testAuthor));
    }

    @Test
    void testAddAuthorToBook_AuthorNotFound() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

       // testing when author is not found
        Author addedAuthor = bookService.addAuthorToBook(1L, "1234567890");

        assertNull(addedAuthor);
        assertFalse(testBook.getAuthorsfromBookClass().contains(testAuthor));
    }

    @Test
    void testFindBookByIsbn_Success() {
        Book foundBook = bookService.findBookByIsbn("1234567890");

        assertNotNull(foundBook);
        assertEquals("Test Book", foundBook.getTitle());
    }

    @Test
    void testFindBookByIsbn_BookNotFound() {
        when(bookRepository.findByIsbn("invalid_isbn")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> {
            bookService.findBookByIsbn("invalid_isbn");
        });
    }

}
