package com.crib.server.services.helpers;

import com.crib.server.common.enums.CtrlResponseStatus;
import com.crib.server.common.patterns.CtrlRequest;
import com.crib.server.common.patterns.CtrlResponse;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidationHelper {

    private static ValidatorFactory factory;
    private static Validator validator;

    static {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Returns true if errors exist, false if no errors
    public static <T1 extends CtrlRequest, T2 extends CtrlResponse> boolean addValidationErrorsToResponse(T1 request, T2 response) {
        Set<ConstraintViolation<T1>> violations = validator.validate(request);

        for (ConstraintViolation<T1> violation : violations) {
            response.addMessage("Validation Error: " + violation.getPropertyPath() + " - " + violation.getMessage());
        }
        if (!violations.isEmpty()) {
            response.setStatus(CtrlResponseStatus.VALIDATION_ERROR);
        }
        return !violations.isEmpty();
    }
}
