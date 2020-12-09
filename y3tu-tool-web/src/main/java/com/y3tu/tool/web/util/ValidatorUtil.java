package com.y3tu.tool.web.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 对实体对象参数校验
 * 相当于hibernate-validator对Controller请求参数的校验
 *
 * @author y3tu
 */
public class ValidatorUtil {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 校验对象，如果不通过抛出IllegalArgumentException异常
     *
     * @param obj    需要校验的对象
     * @param groups 校验目标
     */
    public static void validate(Object obj, Class<?>... groups) {
        Set<ConstraintViolation<Object>> resultSet = validator.validate(obj, groups);
        if (resultSet.size() > 0) {
            //如果存在错误结果，则将其解析并进行拼凑后异常抛出
            List<String> errorMessageList = resultSet.stream().map(o -> o.getMessage()).collect(Collectors.toList());
            StringBuilder errorMessage = new StringBuilder();
            errorMessageList.stream().forEach(o -> errorMessage.append(o + ";"));
            throw new IllegalArgumentException(errorMessage.toString());
        }
    }

}
