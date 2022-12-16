package com.bir.rrmanila.offices

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Table("contacts")
data class Contacts(
    @Id @Column("contact_id") val id: Int,
    @Column("contact_num") val number: String,
    @Column("contact_type") val contactType: String,
    @Column("contact_person_id") val personId: Int
)

@Repository
interface ContactsRepository : ReactiveCrudRepository<Contacts, Int> {
    fun findByPersonId(personId: Int): Flux<Contacts>
}
