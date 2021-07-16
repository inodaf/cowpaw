package com.inodaf.cowpaw.data.settings

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class Settings(
  @ColumnInfo(name = "is_onboarded") val isOnboarded: Boolean
)