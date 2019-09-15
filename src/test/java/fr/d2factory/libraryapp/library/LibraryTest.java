package fr.d2factory.libraryapp.library;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class LibraryTest {
    private Library library;
    private BookRepository bookRepository;

    @Before
    public void setup() {
        bookRepository = new BookRepository();
        try {
            String booksFile = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("books.json"),
                    "UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            List<Book> books = objectMapper.readValue(booksFile, new TypeReference<List<Book>>() {
            });
            bookRepository.addBooks(books);
            library = new LibraryImpl(bookRepository);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void memberCanBorrowBookIfBookIsAvailable() throws HasLateBooksException {
        Student student = new Student(1, 15f);
        Book book = library.borrowBook(46578964513L, student, LocalDate.now());
        assertEquals("J.K. Rowling", book.getAuthor());
        assertEquals(46578964513L, book.getIsbn().getIsbnCode());
        assertEquals("Harry Potter", book.getTitle());
    }

    @Test
    public void borrowedBookIsNoLongerAvailable() throws HasLateBooksException {
        Student student = new Student(2, 15f);
        Resident resident = new Resident(200f);
        library.borrowBook(3326456467846L, student, LocalDate.now());
        Book book = library.borrowBook(3326456467846L, resident, LocalDate.now());
        assertNull(book);
    }

    @Test
    public void ResidentsAreTaxed10CentsForEachDayTheyKeepABook() throws HasLateBooksException {
        Resident resident = new Resident(200f);
        Book book = library.borrowBook(46578964513L, resident, LocalDate.now().minusDays(10));
        library.returnBook(book, resident);
        assertEquals(199f, resident.getWallet(), 0.0001);
    }

    @Test
    public void studentsPay10CentsTheFirst30Days() throws HasLateBooksException {
        Student student = new Student(3, 20f);
        Book book = library.borrowBook(46578964513L, student, LocalDate.now().minusDays(30));
        library.returnBook(book, student);
        assertEquals(17f, student.getWallet(), 0.0001);
    }

    @Test
    public void studentsIn1stYearAreNotTaxedForTheFirst15Days() throws HasLateBooksException {
        Student student = new Student(1, 20f);
        Book book = library.borrowBook(46578964513L, student, LocalDate.now().minusDays(15));
        library.returnBook(book, student);
        assertEquals(20f, student.getWallet(), 0.0001);
    }

    @Test
    public void studentsPay15CentsForEachDayTheyKeeABookAfterTheInitial30Days() throws HasLateBooksException {
        Student student = new Student(3, 20f);
        Book book = library.borrowBook(46578964513L, student, LocalDate.now().minusDays(45));
        library.returnBook(book, student);
        assertEquals(14.75f, student.getWallet(), 0.0001);
    }

    @Test
    public void residentsPay20CentsForEachDayTheyKeepABokAfterTheInitial60Days() throws HasLateBooksException {
        Resident resident = new Resident(100f);
        Book book = library.borrowBook(46578964513L, resident, LocalDate.now().minusDays(70));
        library.returnBook(book, resident);
        assertEquals(92f, resident.getWallet(), 0.0001);
    }
    @Test
    public void membersCannotBorrowBookIfTheyHaveLateBooks() throws HasLateBooksException {
        Resident resident = new Resident(100f);
        library.borrowBook(46578964513L, resident, LocalDate.now().minusDays(70));
        try {
            library.borrowBook(3326456467846L, resident, LocalDate.now());
            fail("HasLateException Should be thrown ");
        } catch (HasLateBooksException e) {
            assertEquals(e.getMessage(), "The member has late books !");
        }
    }
}
