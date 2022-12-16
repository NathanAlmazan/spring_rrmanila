package com.bir.rrmanila.districts

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Table("zones")
data class Zones (
    @Id @Column("zone_id") val id: Int,
    @Column("zone_num") val number: Int,
    @Column("zone_barangay") val barangay: String,
    @Column("zone_boundary") val boundary: String?,
    @Column("rdo_no") val districtNum: Int
)

@Repository
interface ZonesRepository : ReactiveCrudRepository<Zones, Int> {
    fun findByDistrictNum(districtNum: Int): Flux<Zones>
}