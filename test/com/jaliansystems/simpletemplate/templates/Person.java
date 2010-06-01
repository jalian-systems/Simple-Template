package com.jaliansystems.simpletemplate.templates;

public class Person {
	private Address address ;
	private String name;
	private String[] names;
	private boolean minor;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setNames(String... names) {
		this.names = names;
	}
	
	public String[] getNames() {
		return names;
	}

	public void setMinor(boolean b) {
		this.minor = b;
	}
	
	public boolean isMinor() {
		return minor;
	}
}
