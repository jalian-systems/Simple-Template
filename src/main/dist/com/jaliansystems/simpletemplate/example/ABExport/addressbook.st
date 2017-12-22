<?xml version="1.0" encoding="utf-8"?>

<addressbook>
    <belongs>$addressBook.belongs$</belongs>
    $addressBook.addresses {
        <entry>
            <firstname>$it.firstName$</firstName>
            <lastName>$it.lastName$</lastName>
            <phoneNumber>
                $it.phoneNumbers {
                    <$it.type$>$it.number$</$it.type$>
                }$
            </phoneNumber>
        </entry>
    }$
</addressbook>
