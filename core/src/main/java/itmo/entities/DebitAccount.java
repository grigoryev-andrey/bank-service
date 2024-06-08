package itmo.entities;

import itmo.enums.BankAccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "debit")
public class DebitAccount extends BankAccountBase {
    public DebitAccount(Money money,
                           Client client,
                           Bank bank,
                           Date openingDate) {
        super(money, BankAccountType.DEBIT, client, bank, openingDate);
    }
}
