package com.jaliansystems.simpletemplate.example;

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
}
