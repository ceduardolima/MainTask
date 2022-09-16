package com.example.maintask.model.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.model.database.entity.TeamMemberEntity
import com.example.maintask.viewmodel.TeamViewModel

class TeamViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    private val teamMemberImageView = view.findViewById<ImageView>(R.id.team_list_item_user_img)
    private val teamMemberName = view.findViewById<TextView>(R.id.team_list_item_user_name)
    private val teamMemberId = view.findViewById<TextView>(R.id.team_list_item_user_id)
    private val deleteTeamMemberButton =
        view.findViewById<ImageButton>(R.id.team_list_delete_employee_button)

    companion object {
        fun create(parent: ViewGroup): TeamViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.team_list_item, parent,false)
            return TeamViewHolder(view)
        }
    }

    fun bind(teamMemberEntity: TeamMemberEntity, teamViewModel: TeamViewModel) {
        setViewInformation(teamMemberEntity)

        deleteTeamMember(teamMemberEntity.id, teamViewModel)
    }

    private fun setViewInformation(teamMemberEntity: TeamMemberEntity) {
        teamMemberName.text = teamMemberEntity.name
        teamMemberId.text = teamMemberEntity.id.toString()
    }

    private fun deleteTeamMember(id: Int, teamViewModel: TeamViewModel) {
        deleteTeamMemberButton.setOnClickListener {
            teamViewModel.deleteTeamMemberById(id)
        }
    }
}