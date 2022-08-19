package com.example.maintask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.maintask.model.database.TaskRoomDatabase
import com.example.maintask.model.repository.ActionRepository
import com.example.maintask.model.repository.TaskActionRelationRepository
import com.example.maintask.model.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {


}