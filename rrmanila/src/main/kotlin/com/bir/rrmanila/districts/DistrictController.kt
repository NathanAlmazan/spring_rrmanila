package com.bir.rrmanila.districts

import com.bir.rrmanila.offices.BirOffices
import com.bir.rrmanila.offices.BirOfficesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class DistrictController {
    @Autowired lateinit var revenueDistrictRepository: RevenueDistrictRepository
    @Autowired lateinit var zonesRepository: ZonesRepository
    @Autowired lateinit var birOfficesRepository: BirOfficesRepository

    @QueryMapping("allDistricts")
    suspend fun getAllRevenueDistricts(): Flow<RevenueDistrict> = revenueDistrictRepository.findAll().asFlow()

    @QueryMapping("districtByNum")
    suspend fun getRevenueDistrictByNum(@Argument districtNum: Int): RevenueDistrict? = revenueDistrictRepository.findById(districtNum).awaitFirstOrNull()

    @SchemaMapping(typeName = "RevenueDistrict", field = "zones")
    suspend fun getRevenueDistrictZones(revenueDistrict: RevenueDistrict): Flow<Zones> = zonesRepository.findByDistrictNum(revenueDistrict.number).asFlow()

    @SchemaMapping(typeName = "RevenueDistrict", field = "offices")
    suspend fun getRevenueDistrictOffices(revenueDistrict: RevenueDistrict): Flow<BirOffices> = birOfficesRepository.findByDistrictNum(revenueDistrict.number).asFlow()
}