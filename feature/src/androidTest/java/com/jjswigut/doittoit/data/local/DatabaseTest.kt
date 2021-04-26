package com.jjswigut.doittoit.data.local

//  So this was my plan to test the database but I ran into some issues testing flow.
//  Given more time, I would have liked to test that Each List and Task were inserted
//  and retrieved correctly.

//@RunWith(AndroidJUnit4ClassRunner::class)
//@SmallTest
//class MainDatabaseTest {
//
//    @ExperimentalCoroutinesApi
//    @get:Rule
//    var mainCoroutineRule = MainCoroutineRule()
//
//    private lateinit var database: MainDatabase
//    private lateinit var dao: Dao
//
//    @Before
//    fun setUp() {
//        database = Room.inMemoryDatabaseBuilder(
//            ApplicationProvider.getApplicationContext(),
//            MainDatabase::class.java
//        ).allowMainThreadQueries().build()
//
//        dao = database.dao()
//    }
//
//    @After
//    fun tearDown() {
//        database.close()
//    }
//
//    @ExperimentalCoroutinesApi
//    @Test
//    fun insertList() = mainCoroutineRule.runBlocking {
//        val list = ListEntity(listId = 34, name = "Shopping List")
//        dao.insertList(list)
//
//        val allLists = dao.getAllLists().first()
//
//        assertThat(allLists.first()).isEqualTo(list)
//
//    }
//
//}
