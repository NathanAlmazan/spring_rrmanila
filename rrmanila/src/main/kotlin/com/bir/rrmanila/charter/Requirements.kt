package com.bir.rrmanila.charter

import org.springframework.data.annotation.Id
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.*

@Table("requirements")
data class Requirements(
    @Id @Column("req_id") val id: Int,
    @Column("req_name") val name: String
)

@Repository
interface RequirementsRepository : ReactiveCrudRepository<Requirements, Int> {
    @Query("SELECT u.* FROM requirements u " +
            "INNER JOIN category_requirements r ON u.req_id = r.req_id " +
            "INNER JOIN req_category e ON r.categ_id = e.categ_id " +
            "WHERE e.categ_id = :id")
    fun findRequirementsByCategoryId(id: Int): Flux<Requirements>
}

/*
    Table to filter charter requirements by
    charter applicants or other conditions
*/
@Table("req_category")
data class Category(
    @Id @Column("categ_id") val id: Int,
    @Column("categ_name") val name: String,
    @Column("additional") val additional: Boolean
)

@Repository
interface CategoryRepository : ReactiveCrudRepository<Category, Int> {
    @Query("SELECT u.* FROM req_category u " +
            "INNER JOIN charter_requirements r ON u.categ_id = r.categ_id " +
            "INNER JOIN charter e ON r.charter_uid = e.charter_uid " +
            "WHERE e.charter_uid = :uuid")
    fun findCategoriesByCharterUUID(uuid: UUID): Flux<Category>
}
