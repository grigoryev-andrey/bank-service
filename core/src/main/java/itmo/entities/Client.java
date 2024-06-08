package itmo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Enter name of client")
    private String name;

    @NotNull(message = "Enter birthday of client")
    private Date birthday;

    @OneToMany(mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<DepositAccount> depositAccounts;

    @OneToMany(mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<CreditAccount> creditAccounts;

    @OneToMany(mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<DebitAccount> debitAccounts;

    @OneToMany(mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Message> messages;

    private String address;

    @Embedded
    private Passport passport;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @Setter
    @Column(name = "is_subscribed_to_notification")
    private boolean isSubscribedToNotification;

    public Client(String name, Date birthday, String address, Passport passport, Bank bank) {
        this.name = name;
        this.birthday = birthday;
        this.address = address;
        this.passport = passport;
        this.bank = bank;
    }

    public boolean isVerified() {
        return address != null
                && !address.isEmpty()
                && passport.isVerified();
    }
}
