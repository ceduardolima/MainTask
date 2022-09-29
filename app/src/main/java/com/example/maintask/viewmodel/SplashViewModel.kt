package com.example.maintask.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.model.database.entity.TaskActionRelationEntity
import com.example.maintask.model.database.entity.TaskEntity
import com.example.maintask.model.repository.FirestoreRepository
import com.google.firebase.firestore.DocumentSnapshot

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private val fireStoreRepository = FirestoreRepository(application)
    var roomViewModel: RoomViewModel
    private val _hasAnimationEnded = MutableLiveData<Boolean>()
    val hasAnimationEnded: LiveData<Boolean>
        get() = _hasAnimationEnded

    private val _taskActionList = MutableLiveData<List<Pair<TaskEntity, List<ActionEntity>>>>()
    val taskActionList: LiveData<List<Pair<TaskEntity, List<ActionEntity>>>>
        get() = _taskActionList


    init {
        val roomApplication = (application as RoomApplication)
        roomViewModel = RoomViewModelFactory(
            roomApplication.taskRepository,
            roomApplication.actionRepository,
            roomApplication.taskActionRepository,
            roomApplication.teamMemberRepository,
            roomApplication.employeeRepository
        ).create(RoomViewModel::class.java)
    }


    fun setHasAnimationEnded(hasEnded: Boolean) {
        this._hasAnimationEnded.value = hasEnded
    }

    fun launchFirebaseData() {
        fireStoreRepository.getTasks()
    }

    fun observerIfTaskWasLaunched(lifecycleOwner: LifecycleOwner) {
        fireStoreRepository.task.observe(lifecycleOwner, Observer { docList ->
            val taskActionList = mutableListOf<Pair<TaskEntity, List<ActionEntity>>>()
            for (doc in docList) {
                val taskEntity = docToTaskEntity(doc)
                val actionList = docToActionEntity(doc)
                taskActionList.add(Pair(taskEntity, actionList))
            }
            _taskActionList.value = taskActionList
        })
    }

    private fun docToTaskEntity(doc: DocumentSnapshot): TaskEntity {
        return TaskEntity(
            doc.id.toInt(),
            doc.data?.get("title") as String,
            doc.data?.get("date") as String,
            (doc.data?.get("status") as Long).toInt(),
            doc.data?.get("is_emergency") as Boolean,
            (doc.data?.get("author") as String),
            (doc.data?.get("description") as String),
            (doc.data?.get("tools") as String)
        )
    }

    private fun docToActionEntity(doc: DocumentSnapshot): List<ActionEntity> {
        val actionList = doc.data?.getValue("actions") as List<*>
        val newActionList = mutableListOf<ActionEntity>()
        for (action in actionList) {
            val hashMap = action as HashMap<*, *>
            newActionList.add(
                ActionEntity(
                    action = hashMap["action"] as String,
                    order = (hashMap["order"] as Long).toInt(),
                    elapsedTime = "00:00:00",
                    id = (hashMap["id"] as Long).toInt()
                )
            )
        }
        Log.i("task-teste", "ação = $newActionList")
        return newActionList
    }

    fun getTaskActionRelationEntity(
        taskEntity: TaskEntity,
        actionsEntityList: List<ActionEntity>
    ): MutableList<TaskActionRelationEntity> {
        val relationList = mutableListOf<TaskActionRelationEntity>()
        for (action in actionsEntityList) {
            val relationEntity = TaskActionRelationEntity(
                taskEntity.id,
                action.id,
                action.id
            )
            relationList.add(relationEntity)
        }
        return relationList
    }

    fun insertData(
        taskEntity: TaskEntity,
        actionsEntityList: List<ActionEntity>,
        relation: List<TaskActionRelationEntity>
    ){
        roomViewModel.insertTask(taskEntity)
        roomViewModel.insertActionList(actionsEntityList)
        roomViewModel.insertRelationList(relation)
    }

    fun deleteDatabaseData() {
        roomViewModel.deleteAll()
    }
}