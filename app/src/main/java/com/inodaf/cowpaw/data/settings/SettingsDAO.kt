package com.inodaf.cowpaw.data.settings

import androidx.room.Query

interface SettingsDAO {
  @Query("SELECT * FROM `settings`")
  fun getAll(): Settings
}