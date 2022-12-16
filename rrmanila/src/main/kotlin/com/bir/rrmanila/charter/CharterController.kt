package com.bir.rrmanila.charter

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class CharterController {
    @Autowired lateinit var charterRepository: CharterRepository
    @Autowired lateinit var categoryRepository: CategoryRepository
    @Autowired lateinit var requirementsRepository: RequirementsRepository
    @Autowired lateinit var locationRepository: LocationRepository
    @Autowired lateinit var processRepository: ProcessRepository
    @Autowired lateinit var referencesRepository: ReferencesRepository
    @Autowired lateinit var charterServices: CharterServices

    @QueryMapping("allCharter")
    suspend fun getAllCharter(@Argument limit: Int?): Flow<Charter> {
        return if (limit != null) charterRepository.findAllWithLimit(limit).asFlow()
        else charterRepository.findAll().asFlow()
    }

    @QueryMapping("searchCharter")
    suspend fun searchCharter(@Argument search: String): List<Charter> = charterServices.searchCharter(search)

    @QueryMapping("charterByUuid")
    suspend fun getCharterByUuid(@Argument uuid: UUID): Charter? = charterRepository.findById(uuid).awaitFirstOrNull()

    @SchemaMapping(typeName = "Charter", field = "locations")
    suspend fun getCharterLocations(charter: Charter): Flow<Location> = locationRepository.findByCharterUUID(charter.uuid).asFlow()

    @SchemaMapping(typeName = "Charter", field = "process")
    suspend fun getCharterProcess(charter: Charter): Flow<Process> = processRepository.findByCharterUUID(charter.uuid).asFlow()

    @SchemaMapping(typeName = "Charter", field = "applicants")
    suspend fun getCharterApplicants(charter: Charter): Flow<Category> = categoryRepository.findCategoriesByCharterUUID(charter.uuid).asFlow()

    @SchemaMapping(typeName = "Category", field = "requirements")
    suspend fun getCategoryRequirements(category: Category): Flow<Requirements> = requirementsRepository.findRequirementsByCategoryId(category.id).asFlow()

    @SchemaMapping(typeName = "Requirements", field = "references")
    suspend fun getRequirementReferences(requirements: Requirements): Flow<References> = referencesRepository.findReferencesByRequirementId(requirements.id).asFlow()

    @SchemaMapping(typeName = "Process", field = "references")
    suspend fun getProcessReferences(process: Process): Flow<References> = referencesRepository.findReferencesByProcessId(process.id).asFlow()
}