package com.cires.usersgenerator.common.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.multipart.MultipartFile;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

import static com.cires.usersgenerator.exception.constant.ResponseMessageConstant.MISSING_JSON_FILE_DESCRIPTION;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
@Constraint(validatedBy = { FileValidator.Validator.class })
public @interface FileValidator {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @RequiredArgsConstructor
    class Validator implements ConstraintValidator<FileValidator, MultipartFile> {

        private final MessageSource messageSource;

        @Override
        public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
            if (multipartFile.isEmpty()) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                         messageSource.getMessage(MISSING_JSON_FILE_DESCRIPTION, null, Locale.ENGLISH))
                        .addConstraintViolation();
                return false;
            }
            return true;
        }
    }
}
