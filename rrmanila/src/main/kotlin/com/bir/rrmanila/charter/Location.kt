package com.bir.rrmanila.charter

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import java.util.*

@Table("application_location")
data class Location (
    @Id @Column("location_id") val id: Int,
    @Column("location_type") val applicant: String,
    @Column("location_desc") val location: String,
    @Column("charter_uid") val charterUUID: UUID
)

interface LocationRepository : ReactiveCrudRepository<Location, Int> {
    fun findByCharterUUID(charterUUID: UUID): Flux<Location>
}