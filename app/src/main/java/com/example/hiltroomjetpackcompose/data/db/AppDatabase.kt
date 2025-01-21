package com.example.hiltroomjetpackcompose.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hiltroomjetpackcompose.utils.constant.ApiConstant

@Database(entities = [UserInfoEntity::class], version = ApiConstant.DB_VERSION,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userInfoDao(): UserInfoDao
}














