package itmo.entities;

import itmo.enums.OperationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Date datetime;

    @Embedded
    private Money amount;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccountBase bankAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type")
    private OperationType operationType;

    public Transaction(Date datetime, Money amount, BankAccountBase bankAccount, OperationType operationType) {
        this.datetime = datetime;
        this.amount = amount;
        this.bankAccount = bankAccount;
        this.operationType = operationType;
    }
}
