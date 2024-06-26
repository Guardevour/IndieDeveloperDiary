package org.guardevour.developerdiary.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.guardevour.developerdiary.room.entities.Domain
import org.guardevour.developerdiary.room.entities.Field
import org.guardevour.developerdiary.room.entities.Note
import org.guardevour.developerdiary.room.entities.Project
import org.guardevour.developerdiary.room.entities.Relation
import org.guardevour.developerdiary.room.entities.Requirement
import org.guardevour.developerdiary.room.entities.Table
import org.guardevour.developerdiary.room.entities.Tag
import org.guardevour.developerdiary.room.entities.Task

@Dao
interface RoomDao {
    @Delete
    fun delete(project: Project)

    @Delete
    fun delete(table: Table)

    @Delete
    fun delete(field: Field)

    @Delete
    fun delete(tag: Tag)

    @Delete
    fun delete(note: Note)

    @Delete
    fun delete(domain: Domain)

    @Delete
    fun delete(requirement: Requirement)

    @Delete
    fun delete(relation: Relation)

    @Delete
    fun delete(task: Task)

    @Insert
    fun newProject(project: Project)

    @Insert
    fun addTag(tag: Tag)

    @Insert
    fun addTable(table: Table)

    @Insert
    fun addField(field: Field)

    @Insert
    fun addRelation(relation: Relation)

    @Update
    suspend fun save(project: Project)

    @Update
    suspend fun save(table: Table)

    @Update
    suspend fun save(field: Field)

    @Update
    suspend fun save(tag: Tag)

    @Update
    suspend fun save(note: Note)

    @Update
    suspend fun save(domain: Domain)

    @Update
    suspend fun save(requirement: Requirement)

    @Query("Select max(uid) from project")
    fun getLastProject(): Int

    @Query("Select max(uid) from `table`")
    fun getLastTable(): Int

    @Query("Select max(uid) from field")
    fun getLastField(): Int

    @Query("Select max(uid) from relation")
    fun getLastRelation(): Int

    @Query("Select * from project")
    fun getAllProjects(): List<Project>

    @Query("Select * from project where uid = :uid")
    fun getProjectByID(uid: Int): Project

    @Query("Select * from tag where project_name = :projectId")
    fun getAllTags(projectId: Int): List<Tag>

    @Query("Select * from `table` where project_name = :projectId")
    fun getAllTables(projectId: Int): List<Table>

    @Query("Select * from `table` where uid = :tableId")
    fun getTable(tableId: Int): Table

    @Query("Select * from field where uid = :fieldId")
    fun getField(fieldId: Int): Field

    @Query("Select * from field where table_name = :tableId")
    fun getAllFields(tableId: Int): List<Field>

    @Query("Select * from field where table_name in (:tableId)")
    fun getAllFields(tableId: List<Int>): List<Field>

    @Query("Select * from field where table_name in (:tableId) and is_primary_key = 1")
    fun getAllForeignFields(tableId: List<Int>): List<Field>

    @Query("Select * from relation where field_id in (:uids)")
    fun getAllRelations(uids: IntArray): List<Relation>

    @Query("Select * from task where project_id = :prId")
    fun getAllTasks(prId: Int): List<Task>

    @Query("Select * from Note where project_name = :prId")
    fun getAllNotes(prId: Int): List<Note>

    @Query("Select * from requirement where domain_id = :domainId")
    fun getAllRequirements(domainId: Int): List<Requirement>

    @Query("Select * from domain where project_name = :prId")
    fun getAllDomains(prId: Int): List<Domain>

    @Query("Select max(uid) from task")
    fun getLastTask(): Int

    @Query("Select max(uid) from domain")
    fun getLastDomain(): Int


    @Query("Select max(uid) from requirement")
    fun getLastRequirement(): Int


    @Query("Select max(uid) from note")
    fun getLastNote(): Int


    @Insert
    suspend fun addTask(task: Task)

    @Update
    fun complete(task: Task)

    @Insert
    fun insert(note: Note)

    @Insert
    fun insert(domain: Domain)

    @Insert
    fun insert(requirement: Requirement)





}