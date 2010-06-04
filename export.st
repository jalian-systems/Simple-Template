This AddressBook belongs to $addressbook.belongs$

$addressbook.addresses {
    Name: $it.firstName$ $it.lastName$
    $if it.address {
        $with it.address {
            $street$
            $province$
            $cityPinCode$
            $country$
        }$
    }$
    $if it.email {
        E-Mail: $it.email$
    }$
    $if it.affiliations {
        $it.affiliations {
            $key$ ===> $value$
        }$
    }$
	
    $if it.phoneNumbers {
        $it.phoneNumbers {
            $set entry to it
            $entry.type$  has a number $entry.number$
        }$
    }$

    The first phone number is: $set p to it.phoneNumbers[0] $p.number$
    The second phone number is: $set p to it.phoneNumbers[1] $p.number$
}$
