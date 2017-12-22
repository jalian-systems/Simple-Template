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
import java.util.List;

public class AddressBook {

	private final String belongs;
	private final List<AddressBookEntry> addresses = new ArrayList<AddressBookEntry>();

	public AddressBook(String belongs) {
		this.belongs = belongs;
	}

	public String getBelongs() {
		return belongs;
	}

	public void add(AddressBookEntry entry) {
		addresses.add(entry);
	}

	public List<AddressBookEntry> getAddresses() {
		return addresses;
	}

	public static AddressBook populate() {
		AddressBook addressBook = new AddressBook("John Doe");

		AddressBookEntry abe = createABE("Joseph", "Doe", "NoName Street",
				"Software Province", "598123", "US",
				"joseph.doe@thebestcompany.com", "+1231012122", "+1800123144");
		abe.setAffiliation("Charity at its best", "Founding Member");
		abe.setAffiliation("The Axe Murderers Association", "Associate Member");
		addressBook.add(abe);
		addressBook.add(createABE(null, "Apple Inc.", "1 Infinite Loop",
				"Cupertino", "CA 95014", "United States", null, null,
				"1-800-MYAPPLE"));
		return addressBook;
	}

	private static AddressBookEntry createABE(String firstName,
			String lastName, String street, String province,
			String cityPinCode, String country, String email, String homePhone,
			String workPhone) {
		AddressBookEntry abe = new AddressBookEntry(firstName, lastName);
		Address address = new Address(street, province, cityPinCode, country);
		abe.setAddress(address);
		abe.setEmail(email);
		if (workPhone != null)
			abe.setPhoneNumber("work", workPhone);
		if (homePhone != null)
			abe.setPhoneNumber("home", homePhone);
		return abe;
	}
}
