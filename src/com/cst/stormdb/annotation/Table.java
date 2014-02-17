package com.cst.stormdb.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * table
 * 
 * @author Storm
 * 
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {

	/**
	 * name of the table, when used, StormDB will create a table with name of this value
	 * 
	 * @return
	 */
	public String name();
}
