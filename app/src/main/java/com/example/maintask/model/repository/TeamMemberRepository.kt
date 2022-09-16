package com.example.maintask.model.repository

import androidx.annotation.WorkerThread
import com.example.maintask.model.database.dao.TeamMemberDao
import com.example.maintask.model.database.entity.TeamMemberEntity
import kotlinx.coroutines.flow.Flow

class TeamMemberRepository(private val teamMemberDao: TeamMemberDao) {
    val team: Flow<List<TeamMemberEntity>> = teamMemberDao.getMembers()

    @WorkerThread
    suspend fun insert(teamMember: TeamMemberEntity) {
        teamMemberDao.insert(teamMember)
    }

    @WorkerThread
    fun getMemberById(id: Int): Flow<TeamMemberEntity> {
        return teamMemberDao.getMemberById(id)
    }

    @WorkerThread
    suspend fun setWorkTime(id: Int, elapsedTime: Long) {
        teamMemberDao.setWorkTime(id, elapsedTime)
    }

    @WorkerThread
    suspend fun deleteMemberById(id: Int) {
        teamMemberDao.deleteMemberById(id)
    }

    @WorkerThread
    suspend fun deleteAll() {
        teamMemberDao.deleteAll()
    }
}