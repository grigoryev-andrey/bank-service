package itmo.entities;

import itmo.dto.BankDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Enter name of bank")
    private String name;

    @OneToMany(mappedBy = "bank")
    private Set<BankPercent> bankPercent;

    @Setter
    @Column(name = "bank_commission")
    private int bankCommission;

    @OneToMany(mappedBy = "bank",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<CreditAccount> creditAccounts;

    @OneToMany(mappedBy = "bank",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<DebitAccount> client;

    @OneToMany(mappedBy = "bank",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<DebitAccount> debitAccounts;

    @OneToMany(mappedBy = "bank",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<DepositAccount> depositAccount;

    public Bank(BankDto bankDto) {
        this.name = bankDto.getName();
        this.bankCommission = bankDto.getBankCommission();
    }
}
