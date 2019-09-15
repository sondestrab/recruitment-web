package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.library.Price;

import java.time.LocalDate;
import java.util.HashSet;

public class Resident extends Member {
    private static final int RESIDENT_LIMIT_DATE = 60;

    public Resident(float wallet) {
        setWallet(wallet);
        setBorrowedBooks(new HashSet<>());
    }

    @Override
    public void payBook(int numberOfDays) {
        updateWallet(numberOfDays, Price.RESIDENT_NORMAL_PRICE.getPrice());
        if (numberOfDays > RESIDENT_LIMIT_DATE) {
            updateWallet(numberOfDays - RESIDENT_LIMIT_DATE,
                    Price.RESIDENT_OVER_DATE_PRICE.getPrice() - Price.RESIDENT_NORMAL_PRICE.getPrice());
        }
    }

    @Override
    public boolean isLate(BookRepository bookRepository) {
        LocalDate today = LocalDate.now();
        for (Book book : getBorrowedBooks()) {
            LocalDate borrowedAt = bookRepository.findBorrowedBookDate(book);
            LocalDate mustBeReturnedAt = borrowedAt.plusDays(RESIDENT_LIMIT_DATE);
            if (today.compareTo(mustBeReturnedAt) > 0) {
                return true;
            }
        }
        return false;
    }
}
