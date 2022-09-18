package com.example.maintask.views.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.model.adapters.TeamAdapter
import com.example.maintask.model.viewHolder.TeamViewHolder
import com.example.maintask.model.database.entity.TeamMemberEntity
import com.example.maintask.viewmodel.TeamViewModel
import com.google.android.material.textfield.TextInputLayout

class TeamFragment : Fragment() {
    private  val teamViewModel: TeamViewModel by viewModels()
    private lateinit var addEmployeeButton: ImageButton
    private lateinit var idTextEdit: TextInputLayout
    private lateinit var teamList: RecyclerView
    private lateinit var adapter: ListAdapter<TeamMemberEntity, TeamViewHolder>
    private lateinit var progressBar: ProgressBar
    private lateinit var container: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teamViewModel.loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val teamFragment = inflater.inflate(R.layout.fragment_team, container, false)
        initializeView(teamFragment)
        setEmployeeListObserver()
        addNewEmployee()
        createRecyclerView()
        observerShowDataIfWasLoaded()

        return teamFragment
    }

    private fun initializeView(view: View) {
        addEmployeeButton = view.findViewById(R.id.team_add_button)
        idTextEdit = view.findViewById(R.id.team_textedit_id)
        teamList = view.findViewById(R.id.team_list)
        progressBar = view.findViewById(R.id.team_progress_bar)
        container = view.findViewById(R.id.team_fragment_container)
    }

    private fun setEmployeeListObserver() {
        teamViewModel.team.observe(requireActivity()) { team ->
            if (team != null) {
                adapter.submitList(team)
            }
        }
    }

    private fun createRecyclerView() {
        adapter = TeamAdapter(teamViewModel)
        teamList.layoutManager = LinearLayoutManager(context)
        teamList.adapter = adapter
    }

    private fun addNewEmployee() {
        addEmployeeButton.setOnClickListener {
            val idString = idTextEdit.editText?.text.toString().trim()
            val id = verifyId(idString)
            if (id != TeamViewModel.DONT_EXIST)
                setNewTeamMember(id)
        }
    }

    private fun setNewTeamMember(id: Int) {
        val e = TeamMemberEntity(id, "nome", "-", 1000)
        val result = teamViewModel.setNewTeamMember(e)
        if (result == TeamViewModel.DUPLICATED)
            Toast.makeText(
                requireContext(),
                "Membro ja foi inserido anteriormente.",
                Toast.LENGTH_LONG
            ).show()
    }

    private fun verifyId(id: String): Int {
        return if(id.isNotEmpty()) {
            id.toInt()
        } else {
            Toast.makeText(
                requireContext(),
                "Insira um ID válido.",
                Toast.LENGTH_LONG
            ).show()

            TeamViewModel.DONT_EXIST
        }
    }

    private fun observerShowDataIfWasLoaded() {
        teamViewModel.dataWasLoaded.observe(requireActivity()) { wasLoaded ->
            if(wasLoaded) ableVisibility()
            else disableVisibility()
        }
    }

    private fun ableVisibility() {
        container.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun disableVisibility() {
        container.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        disableVisibility()
        super.onDestroy()
    }

}