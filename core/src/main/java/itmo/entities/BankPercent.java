package itmo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "bank_percent")
@NoArgsConstructor
public class BankPercent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;
    @Setter
    @Embedded
    private Money money;
    @Setter
    private int percent;

    public BankPercent(Bank bank, Money money, int percent) {
        this.bank = bank;
        this.money = money;
        this.percent = percent;
    }
}
