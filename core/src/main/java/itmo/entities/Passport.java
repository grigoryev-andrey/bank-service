package itmo.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Passport {
    private String series;
    private String number;

    public boolean isVerified() {
        return series != null
                && !series.isEmpty()
                && number != null
                && !number.isEmpty();
    }
}
