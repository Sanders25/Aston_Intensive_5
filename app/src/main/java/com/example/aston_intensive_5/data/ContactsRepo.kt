package com.example.aston_intensive_5.data

import com.example.aston_intensive_5.data.Contact

class ContactsRepo {

    fun getContacts(): List<Contact> = listOf(
        Contact(
            "Андрей Иванов",
            "+7(871)532-52-12"
        ),
        Contact(
            "Михаил Олейников",
            "+7(911)138-47-19"
        ),
        Contact(
            "Вениамин Паравозов",
            "+7(163)665-61-37"
        ),
        Contact(
            "Алексей Дёмин",
            "+7(633)862-78-11"
        )
    )
}