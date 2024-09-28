package itmo.entities;

import itmo.enums.BankAccountType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Entity
@Table(name = "bank_account")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BankAccountBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Embedded
    @Setter
    protected Money money;

    @Enumerated(EnumType.STRING)
    @Column(name = "bank_account_type")
    protected BankAccountType bankAccountType;

    @ManyToOne
    @JoinColumn(name = "client_id")
    protected Client client;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    protected Bank bank;

    @Column(name = "opening_date")
    protected Date openingDate;

    public BankAccountBase() {}

    protected BankAccountBase(Money money,
                           BankAccountType bankAccountType,
                           Client client,
                           Bank bank,
                           Date openingDate) {
        this.money = money;
        this.bankAccountType = bankAccountType;
        this.client = client;
        this.bank = bank;
        this.openingDate = openingDate;
    }
}
