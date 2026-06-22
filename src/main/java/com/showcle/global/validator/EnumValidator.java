package com.showcle.global.validator;

import com.showcle.global.annotation.ValidEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Stream;

public class EnumValidator implements ConstraintValidator<ValidEnum, CharSequence> {
    private List<String> acceptedValues;
    private boolean ignoreCase;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        acceptedValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .toList();
        ignoreCase = constraintAnnotation.ignoreCase();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        // 빈 스트링도 true, 빈 값은 NotEmpty 로 체크
        if (StringUtils.isEmpty(value)) return true;

        try {
            for(String str: acceptedValues) {
                if(ignoreCase && str.equalsIgnoreCase(value.toString())) return true;
                if(!ignoreCase && str.equals(value.toString())) return true;
            }
            return false;
        } catch(Exception e) {
            return false;
        }
    }
}