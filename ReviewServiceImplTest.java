package mk.finki.ukim.wp.lab;

import mk.finki.ukim.wp.lab.model.Book;
import mk.finki.ukim.wp.lab.model.Review;
import mk.finki.ukim.wp.lab.repository.jpa.BookRepository;
import mk.finki.ukim.wp.lab.repository.jpa.ReviewRepository;
import mk.finki.ukim.wp.lab.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
public class ReviewServiceImplTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    public void testSave() {
        Integer score = 5;
        String description = "excellent book";
        LocalDateTime timestamp = LocalDateTime.now();

        Book book = new Book();
        book.setTitle("Title Book");
        book.setIsbn("1234567890");
        book = bookRepository.save(book);

        reviewService.save(score, description, book.getId(), timestamp);

        List<Review> reviews = reviewRepository.findAll();
        assertEquals(1, reviews.size());
        Review review = reviews.get(0);
        assertEquals(score, review.getScore());
        assertEquals(description, review.getDescription());
        assertEquals(book, review.getBook());
        assertEquals(timestamp, review.getTimestamp());
    }

    @Test
    public void testFindReviewsByBook() {
        Book book = new Book();
        book.setTitle("Sample Book");
        book.setIsbn("1234567890");
        book = bookRepository.save(book);

        Review review1 = new Review(5, "Best book", book, LocalDateTime.now());
        Review review2 = new Review(4, "Nice book", book, LocalDateTime.now());
        reviewRepository.save(review1);
        reviewRepository.save(review2);

        List<Review> reviews = reviewService.findReviewsByBook(book);

        assertEquals(2, reviews.size());
        assertTrue(reviews.contains(review1));
        assertTrue(reviews.contains(review2));
    }

    @Test
    public void testFindReviewsInTimeInterval() {
        Book book = new Book();
        book.setTitle("Title Book");
        book.setIsbn("1234567890");
        book = bookRepository.save(book);

        LocalDateTime now = LocalDateTime.now();
        Review review1 = new Review(5, "Best book", book, now.minusDays(1));
        Review review2 = new Review(4, "Nice book", book, now);
        Review review3 = new Review(3, "Average book", book, now.plusDays(1));
        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);

        List<Review> reviews = reviewService.findReviewsInTimeInterval(now.minusHours(2), now.plusHours(2));

        assertEquals(1, reviews.size());
        assertTrue(reviews.contains(review2));
    }
}
