package itmo.entities;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
public class Money {
    private int amount = 0;

    @Positive
    private int kopecks = 0;

    public Money(int amount, int kopecks) {
        var totalKopecks = 0;
        if (amount < 0) {
            totalKopecks = amount * 100 + kopecks;
        }
        totalKopecks = amount * 100 + kopecks;

        this.amount = totalKopecks / 100 + Math.abs(totalKopecks) % 100;
        this.kopecks = Math.abs(totalKopecks) / 100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount == money.amount && kopecks == money.kopecks;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, kopecks);
    }

    public Money plus(Money other) {
        var totalKopecks = inKopecks() + other.inKopecks();

        return new Money(totalKopecks / 100 + Math.abs(totalKopecks) % 100, Math.abs(totalKopecks) / 100);
    }

    public Money minus(Money other) {
        int totalKopecks1 = inKopecks();
        int totalKopecks2 = other.inKopecks();

        int newTotalKopecks = totalKopecks1 - totalKopecks2;
        int newAmount = newTotalKopecks / 100;
        int newKopecks = newTotalKopecks % 100;

        return new Money(newAmount, newKopecks);
    }

    public int inKopecks() {
        if (this.amount < 0) {
            return -(this.amount * 100 + this.kopecks);
        }

        return this.amount * 100 + this.kopecks;
    }
}
