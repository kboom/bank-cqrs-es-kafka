package com.plgrnds.bank.customers.command;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotNull
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = Email.EmailValidator.class)
public @interface Email {

    String message() default "not a well-formed email address";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    class EmailValidator implements ConstraintValidator<Email, String> {

        private com.plgrnds.bank.customers.domain.model.Email.EmailSpecification emailSpecification = new com.plgrnds.bank.customers.domain.model.Email.EmailSpecification();

        @Override
        public void initialize(Email constraintAnnotation) {}

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return emailSpecification.isSatisfiedBy(value);
        }
    }
}
