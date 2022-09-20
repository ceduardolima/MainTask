package com.example.maintask.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.model.adapters.TeamAdapter
import com.example.maintask.model.database.entity.EmployeeEntity
import com.example.maintask.model.database.entity.TeamMemberEntity
import com.example.maintask.model.viewHolder.TeamViewHolder
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
        teamViewModel.observerAddNewTeamMember(requireActivity()) {
                employee -> verifyEmployee(employee)
        }
    }

    private fun verifyEmployee(employee: EmployeeEntity?): Boolean =
        when {
            employee == null -> {
                Toast.makeText(requireContext(), "ID não existe.", Toast.LENGTH_SHORT)
                    .show()
                false
            }
            teamViewModel.isDuplicated(employee.id) -> {
                Toast.makeText(
                    requireContext(),
                    "O ID já foi previamente adicionado.",
                    Toast.LENGTH_LONG
                ).show()
                false
            }
            else -> true
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val teamFragment = inflater.inflate(R.layout.fragment_team, container, false)
        initializeView(teamFragment)
        createRecyclerView()
        observeUpdateRecyclerView()
        listenerSearchForNewEmployee()
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

    private fun createRecyclerView() {
        adapter = TeamAdapter(teamViewModel)
        teamList.layoutManager = LinearLayoutManager(context)
        teamList.adapter = adapter
    }

    private fun observeUpdateRecyclerView() {
        teamViewModel.team.observe(requireActivity()) { team ->
            if (team != null) {
                adapter.submitList(team)
            }
        }
    }

    private fun listenerSearchForNewEmployee() {
        addEmployeeButton.setOnClickListener {
            val idString = idTextEdit.editText?.text.toString().trim()
            val success = verifyId(idString)
            if (success) {
                val id = idString.toInt()
                searchTeamMember(id)
            }
        }
    }

    private fun verifyId(id: String): Boolean {
        return if(id.isNotEmpty()) {
            true
        } else {
            Toast.makeText(
                requireContext(),
                "Insira um ID válido.",
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }

    private fun searchTeamMember(id: Int) {
        teamViewModel.searchEmployeeById(id)
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