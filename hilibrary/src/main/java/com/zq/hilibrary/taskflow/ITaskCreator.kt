package com.zq.hilibrary.taskflow

interface ITaskCreator {
    fun createTask(taskName: String): Task
}