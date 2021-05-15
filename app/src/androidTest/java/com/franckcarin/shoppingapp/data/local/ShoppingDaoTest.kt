package com.franckcarin.shoppingapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.franckcarin.shoppingapp.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao


    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.shoppingDao()
    }


    @After
    fun tearDown() {

    }


    @Test
    fun insertShoppingItem() = runBlockingTest {
        val shoppingitem = ShoppingItem("name", 1, 1f,"url", id =1)
        dao.insertShoppingItem(shoppingitem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).contains(shoppingitem)
    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingitem = ShoppingItem("name", 1, 1f,"url", id =1)
        dao.insertShoppingItem(shoppingitem)
        dao.deleteShoppingItem(shoppingitem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).doesNotContain(shoppingitem)
    }

    @Test
    fun observePriceSum() = runBlockingTest {
        val shoppingitem1 = ShoppingItem("name", 1, 1f,"url", id =1)
        val shoppingitem2 = ShoppingItem("name", 4, 10f,"url", id =2)
        val shoppingitem3 = ShoppingItem("name", 0, 1f,"url", id =3)

        dao.insertShoppingItem(shoppingitem1)
        dao.insertShoppingItem(shoppingitem2)
        dao.insertShoppingItem(shoppingitem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()

        assertThat(totalPriceSum).isEqualTo(1 * 1f + 4 * 10f  )

    }



}