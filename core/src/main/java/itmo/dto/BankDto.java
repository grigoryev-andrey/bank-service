package itmo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BankDto {
    private String name;
    private int bankCommission;
}
