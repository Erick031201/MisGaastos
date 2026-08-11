package com.example.misgastos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Gasto::class],
    version = 1,
    exportSchema = false
)
abstract class MisGastosDatabase : RoomDatabase() {

    abstract fun gastoDao(): GastoDao

    companion object {
        @Volatile
        private var INSTANCE: MisGastosDatabase? = null

        fun getDatabase(context: Context): MisGastosDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MisGastosDatabase::class.java,
                    "mis_gastos_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}

