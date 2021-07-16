package com.inodaf.cowpaw.data.settings

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Settings::class], version = 1)
abstract class SettingsDatabase: RoomDatabase() {
  abstract fun getAll(): SettingsDAO
}