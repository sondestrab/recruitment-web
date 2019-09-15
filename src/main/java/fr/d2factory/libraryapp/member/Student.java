package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.library.Price;

import java.time.LocalDate;
import java.util.HashSet;

public class Student extends Member {
    private static final int FREE_TRIAL_PERIOD = 15;
    private static final int STUDENT_LIMIT_DATE = 30;
    private int year;

    public Student(int year, float wallet) {
        this.year = year;
        setWallet(wallet);
        setBorrowedBooks(new HashSet<>());
    }

    @Override
    public void payBook(int numberOfDays) {
        if (year == 1) {
            if (numberOfDays > FREE_TRIAL_PERIOD
                    && numberOfDays <= STUDENT_LIMIT_DATE) {
                updateWallet(numberOfDays - FREE_TRIAL_PERIOD,
                        Price.STUDENT_NORMAL_PRICE.getPrice());
            } else if (numberOfDays > STUDENT_LIMIT_DATE) {
                updateWallet(STUDENT_LIMIT_DATE - FREE_TRIAL_PERIOD
                        , Price.STUDENT_NORMAL_PRICE.getPrice());
                updateWallet(numberOfDays - STUDENT_LIMIT_DATE,
                        Price.STUDENT_OVER_DATE_PRICE.getPrice());
            }
        } else {
            updateWallet(numberOfDays, Price.STUDENT_NORMAL_PRICE.getPrice());
            if (numberOfDays > STUDENT_LIMIT_DATE) {
                updateWallet(numberOfDays - STUDENT_LIMIT_DATE,
                        Price.STUDENT_OVER_DATE_PRICE.getPrice() - Price.STUDENT_NORMAL_PRICE.getPrice());
            }
        }
    }

    @Override
    public boolean isLate(BookRepository bookRepository) {
        LocalDate today = LocalDate.now();
        for (Book book : getBorrowedBooks()) {
            LocalDate borrowedAt = bookRepository.findBorrowedBookDate(book);
            LocalDate mustBeReturnedAt = borrowedAt.plusDays(STUDENT_LIMIT_DATE);
            if (today.compareTo(mustBeReturnedAt) > 0) {
                return true;
            }
        }
        return false;
    }
}
