package com.bir.rrmanila.banks

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Table("accredited_banks")
data class AccreditedBanks(
    @Id @Column("bank_no") val id: Int,
    @Column("rdo_no") val districtNum: Int,
    @Column("bank_code") val code: String,
    @Column("bank_abbr") val key: String,
    @Column("bank_branch") val branch: String,
    @Column("bldg_no") val buildingNum: String?,
    @Column("street") val street: String,
    @Column("district") val district: String,
    @Column("city") val city: String
)

@Repository
interface AccreditedBanksRepository : ReactiveCrudRepository<AccreditedBanks, Int> {
    fun findBanksByDistrictNum(districtNum: Int): Flux<AccreditedBanks>
}

@Table("bank_details")
data class BankDetails(
    @Id @Column("bank_abbr") val key: String,
    @Column("bank_name") val name: String,
    @Column("bank_logo") val logo: String
)

@Repository
interface BankDetailsRepository : ReactiveCrudRepository<BankDetails, String>