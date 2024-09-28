package itmo.converters;

import itmo.entities.Passport;
import picocli.CommandLine;

public class PassportConverter implements CommandLine.ITypeConverter<Passport> {
    @Override
    public Passport convert(String value) {
        String[] parts = value.split("\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid passport format. Use series.number (e.g., 1000.5012)");
        }
        var series = parts[0].trim();
        var number = parts[1].trim();
        return new Passport(series, number);
    }
}
