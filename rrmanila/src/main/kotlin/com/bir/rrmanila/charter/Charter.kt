package com.bir.rrmanila.charter

import org.springframework.data.annotation.Id
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.*

@Table("charter")
data class Charter(
    @Id @Column("charter_uid") val uuid: UUID,
    @Column("charter_title") val title: String,
    @Column("charter_desc") val description: String,
    @Column("total_fee") val fee: Double,
    @Column("total_duration") val duration: String,
    @Transient var score: Double = 0.0
)

@Repository
interface CharterRepository : ReactiveCrudRepository<Charter, UUID> {
    @Query("SELECT * FROM charter LIMIT :limit")
    fun findAllWithLimit(limit: Int): Flux<Charter>
}
