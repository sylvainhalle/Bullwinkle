/*
  Copyright 2014-2017 Sylvain Hallé
  Laboratoire d'informatique formelle
  Université du Québec à Chicoutimi, Canada

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

/**
 * 
 */
package ca.uqac.lif.bullwinkle;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)

/**
 * Indicates that a method is attached to a non-terminal symbol in a grammar
 * @author Sylvain Hallé
 */
@Target({ElementType.METHOD})
@Documented
public @interface Builds 
{
	/**
	 * The name of the non-terminal symbol the method is attached to
	 * @return The name
	 */
	String rule();
	
	/**
	 * Whether to expand the rule
	 * @return {@code true} if the rule is to be expanded, {@code false}
	 * otherwise
	 */
	boolean pop() default false;
	
	/**
	 * Whether to prune the arguments from any terminal symbol
	 * @return {@code true} if the arguments should be cleaned, {@code false}
	 * otherwise
	 */
	boolean clean() default false;
}
