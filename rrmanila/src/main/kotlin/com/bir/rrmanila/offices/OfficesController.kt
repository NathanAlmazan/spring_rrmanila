package com.bir.rrmanila.offices

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
class OfficesController {
    @Autowired lateinit var birOfficesRepository: BirOfficesRepository
    @Autowired lateinit var contactPersonRepository: ContactPersonRepository
    @Autowired lateinit var contactsRepository: ContactsRepository
    @Autowired lateinit var revenueDistrictRepository: RevenueDistrictRepository


    @QueryMapping("allOffices")
    suspend fun getAllBirOffices(): Flow<BirOffices> = birOfficesRepository.findAll().asFlow()

    @QueryMapping("officeById")
    suspend fun getBirOfficeById(@Argument officeId: Int): BirOffices? = birOfficesRepository.findById(officeId).awaitFirstOrNull()

    @SchemaMapping(typeName = "BirOffice", field = "directory")
    suspend fun getBirOfficeDirectory(birOffice: BirOffices): Flow<ContactPerson> = contactPersonRepository.findByOfficeId(birOffice.id).asFlow()

    @SchemaMapping(typeName = "BirOffice", field = "district")
    suspend fun getBirOfficeDistrict(birOffice: BirOffices): RevenueDistrict? {
        if (birOffice.districtNum == null) return null
        return revenueDistrictRepository.findById(birOffice.districtNum).awaitFirstOrNull()
    }

    @SchemaMapping(typeName = "ContactPerson", field = "contacts")
    suspend fun getDirectoryContacts(contactPerson: ContactPerson): Flow<Contacts> = contactsRepository.findByPersonId(contactPerson.id).asFlow()

}