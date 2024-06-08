package itmo.converters;

import itmo.entities.Money;
import picocli.CommandLine;

public class MoneyConverter implements CommandLine.ITypeConverter<Money> {
    @Override
    public Money convert(String value) {
        String[] parts = value.split("\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid money format. Use amount,kopecks (e.g., 100.50)");
        }
        int amount = Integer.parseInt(parts[0].trim());
        int kopecks = Integer.parseInt(parts[1].trim());
        return new Money(amount, kopecks);
    }
}