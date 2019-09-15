package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
    private Map<ISBN, Book> availableBooks = new HashMap<>();
    private Map<Book, LocalDate> borrowedBooks = new HashMap<>();

    public void addBooks(List<Book> books) {
        for (Book book : books) {
            availableBooks.put(book.getIsbn(), book);
        }
    }

    public Book findBook(long isbnCode) {
        ISBN isbn = new ISBN(isbnCode);
        return availableBooks.get(isbn);
    }

    public void saveBookBorrow(Book book, LocalDate borrowedAt) {
        availableBooks.remove(book.getIsbn());
        borrowedBooks.put(book, borrowedAt);
    }

    public LocalDate findBorrowedBookDate(Book book) {
        return borrowedBooks.get(book);
    }

    public void returnBook(Book book) {
        availableBooks.put(book.getIsbn(), book);
        borrowedBooks.remove(book);
    }
}
