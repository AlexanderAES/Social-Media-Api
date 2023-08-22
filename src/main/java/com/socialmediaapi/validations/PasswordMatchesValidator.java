package com.socialmediaapi.validations;

import com.socialmediaapi.annotations.PasswordMatches;
import com.socialmediaapi.payload.request.SignupRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        SignupRequest request = (SignupRequest) obj;
        return request.getPassword().equals(request.getConfirmPassword());
    }
}
