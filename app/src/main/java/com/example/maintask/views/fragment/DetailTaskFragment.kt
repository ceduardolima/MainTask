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
import androidx.navigation.fragment.findNavController
import com.example.maintask.R
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.database.entity.TaskEntity
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
    private lateinit var currentTask: TaskEntity
    private var stringOfActions = mutableListOf<String>()
    private var actionIdList = mutableListOf<Int>()
    private var taskId: Int = -1
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
        val newTaskId = requireArguments().getInt("task_id")
        Log.i("vida", newTaskId.toString())
        if(newTaskId == taskId){
            taskId = -1
        } else taskId = newTaskId

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_detail_task, container, false)
        initializeVariables(view)
        setFragmentInformation()

        goToTimerFragmentButton.setOnClickListener {
            val bundle = Bundle()
            val actionsIdArray = actionIdList.toIntArray()
            bundle.putIntArray("actions_id", actionsIdArray)
            findNavController()
                .navigate(R.id.action_detailTaskFragment_to_timerFragment, bundle)
        }

        return view
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
            else -> "Esta a ${daysLeft * (-1)} dias atrasado"
        }
    }

    private fun setCurrentTask(block: () -> Unit) =
        roomViewModel.allTasks.observe(requireActivity()) { taskList ->
            Log.i("vida", taskList.toString())
            for (task in taskList) {
                if (taskId == task.id) {
                    currentTask = task
                    block()
                    break
                }
            }
        }

    private fun setActionIdList(block: () -> Unit) =
        roomViewModel.allTasActionRelations.observe(requireActivity()) { relationList ->
            if(actionIdList.isEmpty()) {
                for (relation in relationList) {
                    if (relation.taskId == taskId && !actionIdList.contains(relation.actionIn)) {
                        actionIdList.add(relation.actionIn)
                    }
                }
            }
            block()
        }

    private fun setStringActionList(block: () -> Unit) =
        roomViewModel.allActions.observe(requireActivity()) { actionsList ->
            Log.i("vida", actionsList.toString())
            for(action in actionsList){
                if (actionIdList.contains(action.id)){
                    stringOfActions.add(action.action)
                }
            }
            block()
        }

    private fun setFragmentInformation() {
        if (taskId != -1) {
            setCurrentTask() {
                setActionIdList() {
                    setStringActionList() {
                        setAllText(currentTask, stringOfActions.joinToString(", "))
                    }
                }
            }
        }
        else setAllText(currentTask, stringOfActions.joinToString(", "))
    }

    override fun onDestroyView() {
        stringOfActions = mutableListOf()
        actionIdList = mutableListOf()
        super.onDestroyView()
    }
}