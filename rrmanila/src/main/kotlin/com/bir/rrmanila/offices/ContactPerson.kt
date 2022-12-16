package com.bir.rrmanila.offices

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Table("contact_person")
data class ContactPerson(
    @Id @Column("cp_id") val id: Int,
    @Column("cp_name") val name: String,
    @Column("cp_position") val position: String,
    @Column("cp_office_id") val officeId: Int
)

@Repository
interface ContactPersonRepository : ReactiveCrudRepository<ContactPerson, Int> {
    fun findByOfficeId(officeId: Int): Flux<ContactPerson>
}
