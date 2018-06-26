package com.y3tu.tool.core.validator;

import com.y3tu.tool.core.exception.RException;
import com.y3tu.tool.core.exception.ValidatorException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 校验工具类
 *
 * @author y3tu
 * @date 2018/6/26
 */
public class ValidatorUtils {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws ValidatorException 校验不通过，则报CheckedException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws ValidatorException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for (ConstraintViolation<Object> constraint : constraintViolations) {
                msg.append(constraint.getRootBeanClass() + ":" + constraint.getPropertyPath() + ":" + constraint.getMessage()).append("<br/>");
            }
            throw new RException(msg.toString());
        }
    }

}
