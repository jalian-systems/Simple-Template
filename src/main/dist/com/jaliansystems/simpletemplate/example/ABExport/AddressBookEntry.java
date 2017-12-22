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

package com.jaliansystems.simpletemplate.example.ABExport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookEntry {
	private final String firstName ;
	private final String lastName ;
	private String homePage ;
	private Address address ;
	private String email ;
	private Map<String, String> affiliations ;
	private List<PhoneNumber> phoneNumbers ;

	public AddressBookEntry(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Map<String, String> getAffiliations() {
		return affiliations;
	}

	public void setAffiliation(String organization, String membership) {
		if (affiliations == null) {
			affiliations = new HashMap<String, String>();
		}
		affiliations.put(organization, membership);
	}

	public List<PhoneNumber> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumber(String type, String number) {
		if (phoneNumbers == null)
			phoneNumbers = new ArrayList<PhoneNumber>();
		phoneNumbers.add(new PhoneNumber(type, number));
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public String getHomePage() {
		return homePage;
	}
}
