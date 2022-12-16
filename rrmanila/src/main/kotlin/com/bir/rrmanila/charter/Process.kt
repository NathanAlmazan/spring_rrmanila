package com.bir.rrmanila.charter

import org.springframework.data.annotation.Id
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.*

enum class ProcessType {
    CLIENT, OFFICER
}

@Table("application_process")
data class Process(
    @Id @Column("process_id") val id: Int,
    @Column("process_step") val step: Int,
    @Column("process_desc") val description: String,
    @Column("process_type") val processType: ProcessType,
    @Column("process_duration") val duration: String?,
    @Column("process_agent") val agent: String?,
    @Column("paid") val paid: Boolean,
    @Column("charter_uid") val charterUUID: UUID
)

@Repository
interface ProcessRepository : ReactiveCrudRepository<Process, Int> {
    @Query("SELECT * FROM application_process WHERE charter_uid = $1 ORDER BY process_step")
    fun findByCharterUUID(charterUUID: UUID): Flux<Process>
}