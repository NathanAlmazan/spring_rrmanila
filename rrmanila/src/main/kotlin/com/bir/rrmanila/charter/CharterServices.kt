package com.bir.rrmanila.charter

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.BiFunction
import kotlin.math.pow
import kotlin.math.sqrt

@Service
class CharterServices(private val client: DatabaseClient) {

    val mappingFunction: BiFunction<Row, RowMetadata, Charter> =
        BiFunction<Row, RowMetadata, Charter> { row: Row, _: RowMetadata? ->
            Charter(
                row["charter_uid", UUID::class.java]!!,
                row["charter_title", String::class.java]!!,
                row["charter_desc", String::class.java]!!,
                row["total_fee", String::class.java]!!.toDouble(),
                row["total_duration", String::class.java]!!
            )
        }

    suspend fun searchCharter(query: String): List<Charter> {
        // split query into array of words
        val words = query.split(" ")

        // convert each word to ILIKE query
        val queries = words.map { word -> "charter_title ILIKE '%$word%' OR charter_desc ILIKE '%$word%'" }
        val dbQuery = queries.joinToString(" OR ")

        val result: Flow<Charter> = client
            .sql("SELECT * FROM charter WHERE $dbQuery")
            .map(mappingFunction)
            .all()
            .asFlow()

        val charterList = ArrayList<Charter>()
        result.collect { charter -> charterList.add(charter) }

        // limit results for better performance
        if (charterList.size > 25) charterList.slice(1..25)

        // compute cosine distance
        for (charter in charterList) {
            val titleDistance = computeCosineDistance(query, charter.title)
            val descDistance = computeCosineDistance(query, charter.description)
            charter.score = titleDistance + descDistance
        }

        // sort descending by score
        charterList.sortByDescending { it.score }

        return charterList
    }

    fun computeCosineDistance(query: String, result: String): Double {
        val wordsA = query.replace("[^A-Za-z0-9 ]".toRegex(), "").lowercase().split(" ")
        val wordsB = result.replace("[^A-Za-z0-9 ]".toRegex(), "").lowercase().split(" ")

        val setA = HashMap<String, Int>()
        val setB = HashMap<String, Int>()

        wordsA.forEach { word ->
            // if exist update count else add word to set A
            if (setA.containsKey(word)) setA.computeIfPresent(word) { _, v -> v + 100 }
            else setA[word] = 1

            // add word to second set
            if (!setB.containsKey(word)) setB[word] = 0
        }

        wordsB.forEach { word ->
            // if exist update count else add word to set A
            if (setB.containsKey(word)) setB.computeIfPresent(word) { _, v -> v + 100 }
            else setB[word] = 1

            // add word to first set
            if (!setA.containsKey(word)) setA[word] = 0
        }

        // Get L2 norm
        var setANorm = 0.0
        var setBNorm = 0.0

        // Summing up squares of each frequency
        for ((_, value) in setA) setANorm += value.toDouble().pow(2.0)
        for ((_, value) in setB) setBNorm += value.toDouble().pow(2.0)

        // taking a square root of summation
        setANorm = sqrt(setANorm)
        setBNorm = sqrt(setBNorm)

        // compute cosine distance
        var cosineDistance = 0.0
        for ((key, countA) in setA) {
            // Get count in B
            val countB = setB[key]
            if (countB != null && countA > 0 && countB > 0) {
                cosineDistance += (countA / setANorm) * (countB / setBNorm)
            }
        }

        return cosineDistance
    }
}