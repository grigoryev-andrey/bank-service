package itmo.validators;

import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.ValidatorFactory;

public abstract class ValidatorBase<TValue> {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final jakarta.validation.Validator validator = factory.getValidator();
    public void validate(TValue value) {
        var violations = validator.validate(value);
        if (!violations.isEmpty()) {
            throw new ValidationException("Validation error: " + violations);
        }
    }
}
