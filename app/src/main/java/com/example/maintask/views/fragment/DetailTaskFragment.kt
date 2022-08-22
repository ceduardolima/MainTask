package com.example.maintask.views.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.maintask.R
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.database.entity.TaskEntity
import com.example.maintask.viewmodel.DetailTaskViewModel
import com.example.maintask.viewmodel.RoomViewModel
import com.example.maintask.viewmodel.RoomViewModelFactory
import java.time.LocalDate

class DetailTaskFragment : Fragment() {
    private lateinit var taskTitle: TextView
    private lateinit var deadline: TextView
    private lateinit var taskAuthor: TextView
    private lateinit var taskDescription: TextView
    private lateinit var taskActions: TextView
    private lateinit var taskTools: TextView
    private lateinit var goToTimerFragmentButton: Button
    private lateinit var detailTaskViewModel: DetailTaskViewModel
    private var taskId: Int = -1
    private var bundleExtra: IntArray = emptyArray<Int>().toIntArray()
    private val roomViewModel: RoomViewModel by viewModels {
        val roomApplication = (requireActivity().application as RoomApplication)
        RoomViewModelFactory(
            roomApplication.taskRepository,
            roomApplication.actionRepository,
            roomApplication.taskActionRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailTaskViewModel = ViewModelProvider(this)[DetailTaskViewModel::class.java]
        taskId = requireArguments().getInt("task_id")
        setupCurrentTask()
        setupActionIdList()
        setupStringActionList()
        Log.i("teste", "onCreate")
    }

    private fun setupCurrentTask() =
        roomViewModel.allTasks.observe(requireActivity()) { taskList ->
            for (task in taskList) {
                if (taskId == task.id) {
                    detailTaskViewModel.setCurrentTask(task)
                    break
                }
            }
        }

    private fun setupActionIdList() =
        roomViewModel.allTasActionRelations.observe(requireActivity()) { relationList ->
            val actionIdList = mutableListOf<Int>()
            for (relation in relationList)
                if (relation.taskId == taskId && !actionIdList.contains(relation.actionIn))
                    actionIdList.add(relation.actionIn)
            detailTaskViewModel.setActionIdList(actionIdList)
            bundleExtra = actionIdList.toIntArray()
        }

    private fun setupStringActionList(){
        detailTaskViewModel.actionIdList.observe(requireActivity()){ idList ->
            setupRoomObserverActions(idList)
        }
    }

    private fun setupRoomObserverActions(actionIdList: List<Int>) =
        roomViewModel.allActions.observe(requireActivity()) { actionsList ->
            val actionStringList = mutableListOf<String>()
            for(action in actionsList){
                if (actionIdList.contains(action.id)){
                    actionStringList.add(action.action)
                }
            }
            detailTaskViewModel.setActionStringList(actionStringList)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("teste", "onCreateView")
        val view = inflater.inflate(R.layout.fragment_detail_task, container, false)
        initializeVariables(view)
        setFragmentInformation()
        changeToTimerFragment()
        changeToTimerFragment()
        return view
    }

    private fun changeToTimerFragment() {
        goToTimerFragmentButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putIntArray("actions_id", bundleExtra)
            findNavController()
                .navigate(R.id.action_detailTaskFragment_to_timerFragment, bundle)
        }
    }

    private fun initializeVariables(view: View) {
        taskTitle = view.findViewById(R.id.details_task_title)
        deadline = view.findViewById(R.id.details_task_days)
        taskAuthor = view.findViewById(R.id.details_task_author)
        taskDescription = view.findViewById(R.id.details_task_description)
        taskActions = view.findViewById(R.id.details_task_actions)
        taskTools = view.findViewById(R.id.details_task_tools)
        goToTimerFragmentButton = view.findViewById(R.id.details_task_start_os)
    }

    private fun setFragmentInformation() {
        detailTaskViewModel.currentTask.observe(requireActivity()) { task ->
            detailTaskViewModel.actionStringList.observe(requireActivity()) { stringList ->
                setAllText(task, stringList.joinToString(", "))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setAllText(currentTask: TaskEntity, actionsString: String){
        taskTitle.text = currentTask.title
        deadline.text = daysLeft(LocalDate.parse(currentTask.date).dayOfYear )
        taskAuthor.text = "Autor: ${currentTask.author}"
        taskDescription.text = "Descrição: ${currentTask.description}"
        taskActions.text = "Ações: $actionsString"
        taskTools.text = "Ferramentas: ${currentTask.tools}"
    }

    private fun daysLeft(days: Int): String {
        val today = LocalDate.now().dayOfYear
        val daysLeft = days - today

        return when {
            daysLeft == 0 -> "Para hoje"
            daysLeft > 0 -> "Faltam ${daysLeft} dias"
            else -> "Esta à ${daysLeft * (-1)} dia(s) atrasado"
        }
    }
}