package io.github.juliog516.Helpers;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MockElement {
    DataTypes dataType() default DataTypes.FirstName;
    int blankPercentage() default 0;
    String formula() default "";

}
