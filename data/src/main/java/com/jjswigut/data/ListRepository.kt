package com.jjswigut.data

// class ListRepository @Inject constructor(
//    private val dao: Dao) {
//    fun getAll(): Maybe<List<SampleEntity>> {
//        return sampleDao.getAll()
//            .subscribeOn(Schedulers.io())
//    }
//
//    fun add(entity: SampleEntity): Maybe<Long> {
//        return sampleDao.insert(entity)
//            .subscribeOn(Schedulers.io())
//    }
//
//    fun addAll(entities: List<SampleEntity>): Maybe<List<Long>> {
//        return sampleDao.insertAll(entities)
//            .subscribeOn(Schedulers.io())
//    }
//
//    fun delete(entity: SampleEntity): Single<Int> {
//        return sampleDao.delete(entity)
//            .subscribeOn(Schedulers.io())
//    }
// }