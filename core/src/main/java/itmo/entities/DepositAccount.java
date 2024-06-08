package itmo.entities;

import itmo.enums.BankAccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Table(name = "deposit")
@NoArgsConstructor
public class DepositAccount extends BankAccountBase {
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "start_money_amount")),
            @AttributeOverride(name = "kopecks", column = @Column(name = "start_money_kopecks"))
    })
    private Money startMoney;

    @Column(name = "date_of_closing")
    @NotNull(message = "Enter date of closing")
    private Date dateOfClosing;

    public DepositAccount(Money money,
                          Client client,
                          Bank bank,
                          Date openingDate,
                          Date dateOfClosing) {
        super(money, BankAccountType.DEBIT, client, bank, openingDate);

        this.startMoney = money;
        this.dateOfClosing = dateOfClosing;
    }
}
