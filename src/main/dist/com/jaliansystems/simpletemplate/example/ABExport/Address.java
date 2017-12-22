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
