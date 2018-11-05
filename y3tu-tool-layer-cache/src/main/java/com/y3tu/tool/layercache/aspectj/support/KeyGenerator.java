
package com.y3tu.tool.layercache.aspectj.support;

import java.lang.reflect.Method;

/**
 * Cache key generator. Used for creating a key based on the given method
 * (used as context) and its parameters.
 *
 * @author Costin Leau
 * @author Chris Beams
 * @author Phillip Webb
 * @since 3.1
 */
public interface KeyGenerator {

	/**
	 * Generate a key for the given method and its parameters.
	 * @param target the target instance
	 * @param method the method being called
	 * @param params the method parameters (with any var-args expanded)
	 * @return a generated key
	 */
	Object generate(Object target, Method method, Object... params);

}
