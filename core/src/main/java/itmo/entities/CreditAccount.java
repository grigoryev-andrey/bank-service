package itmo.entities;

import itmo.enums.BankAccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Table(name = "credit")
@NoArgsConstructor
public class CreditAccount extends BankAccountBase {
    @Positive
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "limit_amount")),
            @AttributeOverride(name = "kopecks", column = @Column(name = "limit_kopecks"))
    })
    @Setter
    private Money limit;

    @Positive
    private int commission;

    public CreditAccount(Money money,
                        Client client,
                        Bank bank,
                        Date openingDate,
                        Money limit) {
        super(money, BankAccountType.DEBIT, client, bank, openingDate);

        this.limit = limit;
    }
}
