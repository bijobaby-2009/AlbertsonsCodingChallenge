package com.example.albertsonscodingchallenge

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.albertsonscodingchallenge.database.AppDatabase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testDatabaseCreation() {
        assertNotNull(database)
    }

    @Test
    fun testProductDao() {
        val productDao = database.productDao()
        assertNotNull(productDao)
    }

    @Test
    fun testDatabaseInstance() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val instance1 = AppDatabase.getInstance(context)
        val instance2 = AppDatabase.getInstance(context)
        assertEquals(instance1, instance2)
    }


}