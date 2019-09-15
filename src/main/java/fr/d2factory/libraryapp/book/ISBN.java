package fr.d2factory.libraryapp.book;

import java.util.Objects;

public class ISBN {
    private long isbnCode;

    public ISBN() {
        super();
    }

    public ISBN(long isbnCode) {
        this.isbnCode = isbnCode;
    }

    public long getIsbnCode() {
        return isbnCode;
    }

    public void setIsbnCode(long isbnCode) {
        this.isbnCode = isbnCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbnCode);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof ISBN))
            return false;
        if (o == this) return true;
        ISBN other = (ISBN) o;
        return isbnCode == other.isbnCode;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
