package com.bir.rrmanila.offices

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Table("offices")
data class BirOffices(
    @Id @Column("office_id") val id: Int,
    @Column("office_name") val name: String,
    @Column("office_address") val address: String,
    @Column("office_email") val email: String,
    @Column("rdo_no") val districtNum: Int?
)

@Repository
interface BirOfficesRepository : ReactiveCrudRepository<BirOffices, Int> {
    fun findByDistrictNum(districtNum: Int): Flux<BirOffices>
}
