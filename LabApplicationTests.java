package mk.finki.ukim.wp.lab;

import mk.finki.ukim.wp.lab.model.Author;
import mk.finki.ukim.wp.lab.model.AuthorFullname;
import mk.finki.ukim.wp.lab.model.Book;
import mk.finki.ukim.wp.lab.model.exceptions.BookNotFoundException;
import mk.finki.ukim.wp.lab.repository.jpa.AuthorRepository;
import mk.finki.ukim.wp.lab.repository.jpa.BookRepository;
import mk.finki.ukim.wp.lab.repository.jpa.BookStoreRepository;
import mk.finki.ukim.wp.lab.service.AuthorService;
import mk.finki.ukim.wp.lab.service.BookService;
import mk.finki.ukim.wp.lab.service.impl.AuthorServiceImpl;
import mk.finki.ukim.wp.lab.service.impl.BookServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class LabApplicationTests {

	@Test
	void contextLoads() {
	}


	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookStoreRepository bookStoreRepository;


	private BookServiceImpl bookService;

	private AuthorRepository authorRepository;
	private AuthorServiceImpl authorService;

	@BeforeEach
	void setUp() {
		authorRepository = mock(AuthorRepository.class);
		authorService = new AuthorServiceImpl(authorRepository);
		bookService = new BookServiceImpl(bookRepository, bookStoreRepository, authorRepository);
	}

	@Test
	void testListAuthors() {
		Author author1 = new Author(new AuthorFullname("Eroll", "Sakipi"), "Biography 1", LocalDate.of(2002, 1, 1));
		Author author2 = new Author(new AuthorFullname("Erman", "Ramiqi"), "Biography 2", LocalDate.of(2002, 5, 15));
		List<Author> authors = Arrays.asList(author1, author2);

		when(authorRepository.findAll()).thenReturn(authors);

		List<Author> result = authorService.listAuthors();

		assertEquals(2, result.size());
		assertEquals("Eroll", result.get(0).getAuthorFullname().getName());
		assertEquals("Ramiqi", result.get(1).getAuthorFullname().getSurname());
	}

	@Test
	void testFindById() {
		Author author = new Author(new AuthorFullname("Eroll", "Sakipi"), "Biography", LocalDate.of(2002, 1, 1));
		when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

		Author result = authorService.findById(1L);

		assertEquals("Eroll", result.getAuthorFullname().getName());
		assertEquals("Sakipi", result.getAuthorFullname().getSurname());
	}

}
