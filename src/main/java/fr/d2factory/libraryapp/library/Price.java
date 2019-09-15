package fr.d2factory.libraryapp.library;

public enum Price {
    STUDENT_NORMAL_PRICE(0.1f),
    STUDENT_OVER_DATE_PRICE(0.15f),
    RESIDENT_NORMAL_PRICE(0.1f),
    RESIDENT_OVER_DATE_PRICE(0.2f);

    private final float price;

    private Price(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

}
