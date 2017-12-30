/**
 * Provides the classes necessary to create a in-memory template and
 * applying it to a scope to generate a String.
 * 
 * <p>In most programs, we need to output some structured data - be it XML, a property file or any other format. Generation of such data is usually accomplished with use of control logic interspersed with print statements. In these cases we are not generating random text - rather we are outputting ‘statements’ in a specific language. Having a meta language and an engine that allows us to convert a model from a program into such a language greatly simplifies the effort. Template engines like SimpleTemplate and associated languages accomplishes that task.</p>
 * <h3>Why SimpleTemplate?</h3>
 * <p>SimpleTemplate removes all the cruft that is part of other template engines. It does one thing - that is converting a model into another language - and does it well. SimpleTemplate assumes that the generated output is some structured data and needs another tool to format it into readable format if needed.</p>
 * <h3>Motivation</h3>
 * In one of our projects we need to generate XML output from Hibernate models. In our search we found StringTemplate (http://www.stringtemplate.org) - that suited most of our needs - but not quite.
 * Since the model objects are deeply nested we need a mechanism to create an alias to refer to a deeply nested objects.
 * We need a way to index into arrays, lists and maps. The StringTemplate mechanism of using rest and first to access the elements is cumbersome and error prone in our context.
 * StringTemplate has lot more functionality than we needed and pulling in Antrl to just use the templates just didn’t seem right.
 * SimpleTemplate uses a hand crafted, simple top down parser to parse the templates.
 * <h3>Features</h3>
 * <ol>
 * <li>Simple template directives.</li>
 * <li>User selectable template directive separators.</li>
 * <li>Alias creation using with and set directives.</li>
 * <li>Support for indexing into arrays, collections, maps.</li>
 * <li>Looping on an array or a collection.</li>
 * <li>Simple include mechanism to reuse template files.</li>
 * <li>Conditional evaluation using if and ifelse. No dangling else!</li>
 * <li>Custom list separation settings (prefix, suffix and separators).</li>
 * <li>Template files can be read from file system, class path or just a string.</li>
 * <li>Support for methods aka. sub templates.</li>
 * </ol>
 */
package com.jaliansystems.simpletemplate;