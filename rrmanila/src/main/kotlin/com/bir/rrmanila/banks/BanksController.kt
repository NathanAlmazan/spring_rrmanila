package com.bir.rrmanila.banks

import com.bir.rrmanila.districts.RevenueDistrict
import com.bir.rrmanila.districts.RevenueDistrictRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class BanksController {
    @Autowired lateinit var accreditedBanksRepository: AccreditedBanksRepository
    @Autowired lateinit var bankDetailsRepository: BankDetailsRepository
    @Autowired lateinit var revenueDistrictRepository: RevenueDistrictRepository

    @QueryMapping("banksByDistrict")
    suspend fun getBanksByDistrict(@Argument district: Int): Flow<AccreditedBanks> = accreditedBanksRepository.findBanksByDistrictNum(district).asFlow()

    @SchemaMapping(typeName = "Banks", field = "details")
    suspend fun getBankDetails(accreditedBanks: AccreditedBanks): BankDetails? = bankDetailsRepository.findById(accreditedBanks.key).awaitFirstOrNull()

    @SchemaMapping(typeName = "Banks", field = "revenueDistrict")
    suspend fun getBankDistrict(accreditedBanks: AccreditedBanks): RevenueDistrict? = revenueDistrictRepository.findById(accreditedBanks.districtNum).awaitFirstOrNull()
}