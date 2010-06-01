package com.jaliansystems.simpletemplate.example;

public class Address {
	private String street;
	private String province;
	private String cityPinCode;
	private String country;

	public Address(String street, String province, String cityPinCode,
			String country) {
		super();
		this.street = street;
		this.province = province;
		this.cityPinCode = cityPinCode;
		this.country = country;
	}

	public String getStreet() {
		return street;
	}

	public String getProvince() {
		return province;
	}

	public String getCityPinCode() {
		return cityPinCode;
	}

	public String getCountry() {
		return country;
	}

}
