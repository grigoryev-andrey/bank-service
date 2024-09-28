package itmo.dto;

import itmo.entities.Passport;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class UpdatedClientDto {
    String name;
    Date birthday;
    String address;
    Passport passport;
}
