/*
 *   Copyright 2010 Jalian Systems Pvt. Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
*/

package com.jaliansystems.simpletemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jaliansystems.simpletemplate.internal.templates.TemplateElement;

/**
 * An in-memory representation of a template.
 * 
 * <p>
 * A template, specified as a string, file or a resource, must first be
 * converted into an instance of this class using a
 * {@linkplain com.jaliansystems.simpletemplate.TemplateReader template
 * reader}. The resulting object can then be used to convert the template into a
 * String using the {@link #apply} method using a {@link Scope}.
 * </p>
 * 
 * <h3>Template Syntax</h3>
 * <p>
 * A template is just plain text interspersed with template directives. Any
 * number of model variables can be passed to the template through the
 * {@linkplain Scope scope} object.
 * </p>
 * 
 * <h3>Template Start and End Tokens</h3>
 * <p>
 * By default a template uses <code>$</code> for start and end tokens. You can
 * override the start and end tokens while reading using
 * {@linkplain com.jaliansystems.simpletemplate.TemplateReader template
 * reader}.
 * </p>
 * 
 * <h3>Contexts and Data Types</h3>
 * <p>
 * SimpleTemplate operates in two contexts. When you start reading a file it is
 * in Text context. In Text context all text input is emitted into the output.
 * SimpleTemplate moves into Template context when the stream reaches one of the
 * template directives (i.e one of <code>$identifier</code>, <code>$if</code>,
 * etc.). In the Template context, SimpleTemplate reads text as tokens and can
 * identify integers, booleans (true/false) and strings. Each of these data
 * types, themselves, is a template. You can switch into a block context using
 * <code>${}$</code> block.
 * </p>
 * 
 * <h3>Attribute Access</h3>
 * <p>
 * The simplest directive in a template is referencing to a model attribute. You
 * can refer to an attribute reference such as name and email from scope:
 * <blockquote>
 * 
 * <pre>
 * Name is $name$ and email is $email$
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * The $name$ and $email$ in the template will be replaced with the return
 * values from the toString method of the objects put in the scope as “name” and
 * “email”.
 * </p>
 * <p>
 * You can also access nested attributes.
 * </p>
 * <blockquote>
 * 
 * <pre>
 * Address address = new Address();
 * address.setName(“John Doe”);
 * address.setEmail(“john.doe@example.com”);
 * scope.put(“address”, address);
 * </pre>
 * 
 * </blockquote>
 * <p>
 * With scope setup with a composite object like address:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * Name is $address.name$ and email is $address.email$
 * </pre>
 * 
 * </blockquote>
 * <p>
 * gives a result “Name is John Doe and email is john.doe@example.com”.
 * SimpleTemplate just emits an empty string for the attributes that are null.
 * When an attribute needs to be resolved, SimpleTemplate looks for methods that
 * start with 'get', 'is' and '' it that order.
 * </p>
 * 
 * <p>
 * When an attribute (nested or otherwise) refers to a java.util.Collection,
 * SimpleTemplate handles it differently. SimpleTemplate emits a concatenation
 * of toString values of each item separated by st_list_separator. Also each
 * list item is prefixed with st_list_prefix and suffixed with st_list_suffix.
 * 
 * With scope setup as:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * String[] flowers = new String[] { “Rose”, “Jasmine”, “Lily” } ;
 * scope.put(“list”, flowers);
 * </pre>
 * 
 * </blockquote>
 * <p>
 * The following script:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * $set st_list_separator to “--”
 * $set st_list_prefix to “&lt;flower:”
 * $set st_list_suffix to “/&gt;”
 * $list$
 * </pre>
 * 
 * </blockquote>
 * <p>
 * gives a result
 * “&lt;flower:Rose/&gt;--&lt;flower:Jasmine/&gt;--&lt;flower:Lily/&gt;”.
 * </p>
 * <p>
 * The st_list_prefix and st_list_suffix defaults to empty strings and
 * st_list_separator defaults to “,”.
 * </p>
 * <h3>Indexed Access</h3>
 * <p>
 * You can access items from a Collection, Array or a Map using indexed access.
 * Setting up scope as follows:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * String[] flowers = new String[] { “Rose”, “Jasmine”, “Lily” } ;
 * scope.put(“flowers”, flowers);
 * </pre>
 * 
 * </blockquote>
 * <p>
 * the following template
 * </p>
 * <blockquote>
 * 
 * <pre>
 * $flowers[0]$
 * </pre>
 * 
 * </blockquote>
 * <p>
 * gives a result "Rose".
 * </p>
 * <p>
 * Similarly, with a map set into scope:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * Map&lt;String, String&gt; capitals = new HashMap&lt;String, String&gt;();
 * capitals.put("India", "New Delhi");
 * capitals.put("United States", "Washington");
 * capitals.put("Canada", "Ottawa");
 * scope.put(“capitals”, capitals);
 * </pre>
 * 
 * </blockquote>
 * <p>
 * the following template
 * </p>
 * <blockquote>
 * 
 * <pre>
 * $capitals["United States"]$
 * </pre>
 * 
 * </blockquote>
 * <p>
 * gives a result "Washington".
 * </p>
 * <p>
 * You can even use an expression as an index. For accessing items from an
 * Array, the expression should be resolved into an integer. For example, with
 * scope setup as
 * </p>
 * <blockquote>
 * 
 * <pre>
 * Map&lt;String, String&gt; capitals = new HashMap&lt;String, String&gt;();
 * capitals.put("India", "New Delhi");
 * capitals.put("United States", "Washington");
 * capitals.put("Canada", "Ottawa");
 * scope.put(“capitals”, capitals);
 * scope.put("mycountry", "India");
 * </pre>
 * 
 * </blockquote>
 * <p>
 * the following template
 * </p>
 * <blockquote>
 * 
 * <pre>
 * $capitals[mycountry]$
 * </pre>
 * 
 * </blockquote>
 * <p>
 * gives a result "New Delhi". You get the same result even you use a template
 * instruction as an index. So,
 * </p>
 * <blockquote>
 * 
 * <pre>
 * $capitals[$mycountry$]$
 * </pre>
 * 
 * </blockquote>
 * <p>
 * also gives a result "New Delhi".
 * </p>
 * <h3>Looping</h3>
 * <p>
 * You can loop through a Map or a Collection with SimpleTemplate looping
 * directive.
 * </p>
 * <blockquote>
 * 
 * <pre>
 * List of flowers available:
 * $flowers {
 *     $index1$: $it$
 * }$
 * 
 * $capitals {
 *     Capital of $key$ is $value$
 * }$
 * </pre>
 * 
 * </blockquote>
 * <p>
 * Within the scope of the looping over a Collection or Array, SimpleTemplate
 * sets index0 to the current item index (starting with 0), index1 to the
 * current item index (starting with 1) and it to the item itself. For Maps, key
 * is set to key of the current entry and value is set to the value.
 * </p>
 * <p>
 * You can also loop through a Map using the values or keySet (though it is
 * nothing to do with SimpleTemplate).
 * </p>
 * <blockquote>
 * 
 * <pre>
 * Known Capitals: $capitals.keySet { $it$ }
 * </pre>
 * 
 * </blockquote>
 * <h3>Introducing Aliases</h3>
 * <p>
 * Sometimes, you need to access attributes that are deeply nested in the model.
 * You can introduce aliases into the current scope and reduce the amount of
 * typing you need to do as well as make the template code look good.
 * SimpleTemplate adds the aliases it, index0, index1, key and value in the
 * looping context. You can introduce your own aliases using set and with
 * directives to SimpleTemplate.
 * </p>
 * 
 * <p>
 * Suppose we have a AddressBook object which has a list of AddressBookEntrys.
 * Each AddressBookEntry further has a list of PhoneNumbers each with a type and
 * a number. For iterating through all the phone numbers in an AddressBook we
 * might write:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * $addressBook.addresses {
 *     Name: $it.firstName$ $it.lastName$
 *     $it.phoneNumbers {
 *         $it.type$ phone: $it.number$
 *     }$
 * }$
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * The same thing can also be written (using with):
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * $addressBook.addresses {
 *     $with it {
 *         Name: $firstName$ $lastName$
 *         $phoneNumbers {
 *             $with it {
 *                 $type$ phone: $number$
 *             }$
 *         }$
 *     }$
 * }$
 * </pre>
 * 
 * </blockquote>
 * <p>
 * In this academic example the amount of the template text for the example with
 * with is more. However, in real world where you need to work with deeply
 * nested java objects, the with directive saves time and makes the template
 * code read cleaner.
 * </p>
 * 
 * <p>
 * We can also use set directive to create an alias. The same code above written
 * using set :
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * $addressBook.addresses {
 *     $set address to it
 *     Name: $address.firstName$ $address.lastName$
 *     $address.phoneNumbers {
 *         $with it {
 *             $type$ phone: $number$
 *         }$
 *     }$
 * }$
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>Conditionals</h3>
 * <p>
 * There will be times in when you want to conditionally emit text. For those
 * cases SimpleTemplate supports if-else and if constructs. Most expressions can
 * be used as conditions to test in a if statement. An object that evaluates to
 * null is false. A Collection, Map, Array and String returns false if it is
 * empty. All other cases the expression evaluates to be true.
 * </p>
 * 
 * <p>
 * Using conditionals you can check whether a Collection is empty before you
 * process it:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * This address book belongs to: $addressBook.belongsTo$
 * $ifelse $addressBook.addresses {
 *     $addressBook.addresses {
 *        $set address to it
 *        Name: $address.firstName$ $address.lastName$
 *        $address.phoneNumbers {
 *            $with it {
 *                $type$ phone: $number$
 *            }$
 *        }$
 *     }$
 * }
 * {
 *     "No address book does not have any entries"
 * }$
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>Block Statements</h3>
 * <p>
 * SimpleTemplate supports two types block statements. You have already seen one
 * of them - the looping constructs and conditional examples use {}$ blocks. The
 * other type of block statement starts with ${ and ends with }$. The difference
 * between these two kinds of block statements is the way they are evaluated.
 * The {}$ block is evaluated as text and it escapes into the template mode when
 * it sees any one of the template directives. The ${ block is evaluated in
 * template syntax. When SimpleTemplate reads a template it starts evaluating in
 * text mode.
 * </p>
 * <p>
 * For example,
 * </p>
 * <blockquote>
 * 
 * <pre>
 * ${ "Hello World" }$
 * </pre>
 * 
 * </blockquote>
 * <p>
 * evaluates to Hello World where as,
 * </p>
 * <blockquote>
 * 
 * <pre>
 * ${ {"Hello World"}$ }$
 * </pre>
 * 
 * </blockquote>
 * <p>
 * evaluates to "Hello World" (Note the double quotes around the text). You can
 * use {}$ blocks only in a template context because in Text context the { will
 * be considered as text.
 * </p>
 * 
 * <h3>Defining and Invoking Methods</h3>
 * <p>
 * When you are defining templates, there are times the same template code is
 * repeated over and over again. You can use methods to reduce the repetition
 * and make the template code clearer.
 * </p>
 * 
 * <p>
 * For example,
 * </p>
 * <blockquote>
 * 
 * <pre>
 * $printAddressDetails (address) {
 *     &lt;address&gt;
 *         &lt;firstname&gt;$address.firstName$&lt;/firstname&gt;
 *         &lt;lastname&gt;$address.lastName$&lt;/lastname&gt;
 *         &lt;email&gt;$address.email$&lt;/email&gt;
 *     &lt;/address&gt;
 * }$
 * </pre>
 * 
 * </blockquote>
 * <p>
 * Defines a method that takes one parameter - address. You can invoke this
 * method by:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * $:printAddressDetails(address)$
 * </pre>
 * 
 * </blockquote>
 * <p>
 * You can also invoke this method by,
 * </p>
 * <blockquote>
 * 
 * <pre>
 * $address:printAddressDetails()$
 * </pre>
 * 
 * </blockquote>
 * <p>
 * When you invoke the method using the first form the evaluated value of the
 * expression before : is passed to the method as the first parameter.
 * </p>
 * <p>
 * Your methods can take any number of parameters. SimpleTemplate matches the
 * number of parameters and throws an exception if they do not match. You can
 * also chain multiple methods - each one taking the return value of the
 * previous one. So,
 * </p>
 * <blockquote>
 * 
 * <pre>
 * $italics(s) ${ "<i>" $s$ "</i>" }$
 * $bold(s) ${ "<b>" $s$ "</b>" }$
 * </pre>
 * 
 * </blockquote>
 * 
 * <blockquote>
 * 
 * <pre>
 * ${ $set hello to "Hello" $hello:bold():italics()$ }$
 * </pre>
 * 
 * </blockquote>
 * <p>
 * Gives an out put of "&lt;i&gt;&lt;b&gt;Hello&lt;/b&gt;&lt;/i&gt;".
 * </p>
 * 
 * <h3>Reusing templates using include</h3>
 * <p>
 * The ultimate reuse is however, to use a complete template or methods in
 * another template. SimpleTemplate provides a inclusion mechanism to achieve
 * this.
 * </p>
 * 
 * <p>
 * <b>File: address.st</b>
 * </p>
 * <blockquote>
 * 
 * <pre>
 * $printAddressDetails (address) {
 *     &lt;address&gt;
 *         &lt;firstname&gt;$address.firstName$&lt;/firstname&gt;
 *         &lt;lastname&gt;$address.lastName$&lt;/lastname&gt;
 *         &lt;email&gt;$address.email$&lt;/email&gt;
 *     &lt;/address&gt;
 * }$
 * </pre>
 * 
 * </blockquote>
 * <p>
 * <b>File: template.st</b>
 * </p>
 * <blockquote>
 * 
 * <pre>
 * $include "address.st"
 * $addressBook.addresses {
 *     $it:printAddressDetails()$
 * }$
 * </pre>
 * 
 * </blockquote>
 * <p>
 * With both the files in the same directory, processing template.st includes
 * the definition of printAddressDetails method and the result will contain the
 * address details.
 * </p>
 * 
 * 
 */
public class Template extends TemplateElement {

	private List<TemplateElement> children = new ArrayList<TemplateElement>();

	public Template(String fileName, int lineNumber) {
		super(fileName, lineNumber);
	}

	public void add(TemplateElement t) {
		children.add(t);
	}

	/**
	 * Applying the definitions in the {@linkplain Scope scope } to the template and
	 * return the resultant String
	 * 
	 * @param scope Scope
	 */
	@Override
	public String apply(Scope scope) {
		StringBuffer buffer = new StringBuffer();
		for (TemplateElement t : children) {
			buffer.append(t.apply(scope));
		}
		return buffer.toString();
	}

	/**
	 * Applying the definitions in the {@linkplain java.util.Map map } to the template and
	 * return the resultant String
	 * 
	 * @param scope Scope
     * @return the resolved template as a String
	 */
	public String apply(Map<String, Object> scope) {
		StringBuffer buffer = new StringBuffer();
		for (TemplateElement t : children) {
			buffer.append(t.apply(new Scope(scope)));
		}
		return buffer.toString();
	}

	@Override
	public Object getTargetInternal(Scope scope) {
		Object o = null;
		for (TemplateElement t : children)
			o = t.getTarget(scope);
		return o;
	}

	@Override
	public String getDisplayName(String indent) {
		StringBuffer sb = new StringBuffer();
		sb.append(indent + "(composite\n");
		for (TemplateElement t : children) {
			sb.append(t.getDisplayName(indent + "  ")).append('\n');
		}
		sb.append(indent + ")");
		return sb.toString();
	}

	@Override
	public String getDebugString(String indent) {
		StringBuffer sb = new StringBuffer();
		sb.append(getLineNumber() + ":" + indent + "(composite\n");
		for (TemplateElement t : children) {
			sb.append(t.getDebugString(indent + "  ")).append('\n');
		}
		sb.append(indent + ")");
		return sb.toString();
	}
}
