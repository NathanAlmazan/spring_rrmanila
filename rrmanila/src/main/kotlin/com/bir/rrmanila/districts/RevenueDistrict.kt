package com.bir.rrmanila.districts

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Table("districts")
data class RevenueDistrict (
        @Id @Column("rdo_no") val number: Int,
        @Column("rdo_name") val name: String
)

@Repository
interface RevenueDistrictRepository : ReactiveCrudRepository<RevenueDistrict, Int>