package com.cst.stormdb.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * column of a table
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

	/**
	 * name of the column
	 * 
	 * @return
	 */
	public String name();

	/**
	 * type of the column
	 * 
	 * @return
	 */
	public Type type();
	

	/**
	 * constant type
	 * 
	 * @author Storm
	 * 
	 */
	public enum Type {
		INTEGER, LONG, TEXT
	};
}
