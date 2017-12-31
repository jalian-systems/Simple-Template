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

package com.jaliansystems.simpletemplate.templates;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jaliansystems.simpletemplate.Scope;


public class ScopeTest {

	public static final class ObjectExtension {
		public String getKey() {
			return "Objects Value" ;
		}
	}

	@Test
	public void testChecksInItsScopeBeforeParents() throws Exception {
		Scope parent = new Scope();
		parent.put("Key", "Parents Value");
		Scope child = new Scope(parent);
		child.put("Key", "Childs Value");
		assertEquals("Parents key value overridden by child", "Childs Value", child.resolve("Key"));
	}
	
	@Test
	public void testReturnsValueFromParentIfNotExistInChild() throws Exception {
		Scope parent = new Scope();
		parent.put("Key", "Parents Value");
		Scope child = new Scope(parent);
		child.put("Key1", "Childs Value");
		assertEquals("Parents key value given if child doesnt have one", "Parents Value", child.resolve("Key"));
	}
	
	@Test
	public void testScopeWithAObjectChecksTheObjectBeforeTheParent() throws Exception {
		Scope parent = new Scope();
		parent.put("Key", "Parents Value");
		Scope child = new Scope(parent, new ObjectExtension());
		assertEquals("Objects value is checked before the parents", "Objects Value", child.resolve("key"));
	}
}
