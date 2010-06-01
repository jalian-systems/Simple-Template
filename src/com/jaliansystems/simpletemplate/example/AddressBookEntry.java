package com.jaliansystems.simpletemplate.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookEntry {
	private final String firstName ;
	private final String lastName ;
	private Address address ;
	private String email ;
	private Map<String, String> affiliations ;
	private List<PhoneNumber> phoneNumbers ;

	public AddressBookEntry(String firstName, String lastName) {
		super();
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
}
