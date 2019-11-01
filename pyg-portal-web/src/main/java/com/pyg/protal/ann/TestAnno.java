package com.pyg.protal.ann;

import java.lang.annotation.*;

/**
 * @Title TestAnno
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-10-31 21:34
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface TestAnno {
}
