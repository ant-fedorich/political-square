package eltonio.projects.politicalcompassquiz

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import eltonio.projects.politicalcompassquiz.repository.AppDatabase
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class DatabaseRule : TestWatcher() {
    private val TAG = "DatabaseRule"
    lateinit var context: Context
    lateinit var database: AppDatabase

    override fun starting(description: Description?) {
        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        Log.i(TAG, "starting: $description")

    }

    override fun finished(description: Description?) {
        database.close()
        Log.i(TAG, "finished: $description")
    }
}