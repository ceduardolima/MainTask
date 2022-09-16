package com.example.maintask.model.search

import com.example.maintask.model.database.entity.TeamMemberEntity

class TeamMemberSearch {

    fun searchById(team: List<TeamMemberEntity>, id: Int): Int {
        return searchByIdRecursion(team, id, 0)
    }

    private fun searchByIdRecursion(
        team: List<TeamMemberEntity>,
        id: Int,
        currentIndex: Int
    ): Int {
        if(currentIndex >= team.size)
            return -1
        if(team[currentIndex].id == id)
            return currentIndex
        return searchByIdRecursion(team, id, currentIndex + 1)
    }
}