package com.bir.rrmanila.charter

import org.springframework.data.annotation.Id
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

enum class ReferenceType {
    TEXT, URL
}

@Table("references")
data class References(
    @Id @Column("ref_id") val id: Int,
    @Column("ref_name") val keyword: String,
    @Column("ref_def") val definition: String,
    @Column("ref_type") val referenceType: ReferenceType
)

@Repository
interface ReferencesRepository : ReactiveCrudRepository<References, Int> {
    @Query("SELECT u.* FROM \"references\" u " +
            "INNER JOIN requirement_references r ON u.ref_id = r.ref_id " +
            "INNER JOIN requirements e ON r.req_id = e.req_id " +
            "WHERE e.req_id = :id")
    fun findReferencesByRequirementId(id: Int): Flux<References>

    @Query("SELECT u.* FROM \"references\" u " +
            "INNER JOIN process_references r ON u.ref_id = r.ref_id " +
            "INNER JOIN application_process e ON r.process_id = e.process_id " +
            "WHERE e.process_id = :id")
    fun findReferencesByProcessId(id: Int): Flux<References>
}
