package com.example.loginmvp.data.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.loginmvp.data.dao.ProductDao;
import com.example.loginmvp.data.entities.Product;

@Database(entities = {Product.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract ProductDao productDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE products ADD COLUMN rating REAL NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE products ADD COLUMN sold INTEGER NOT NULL DEFAULT 0");

            // 🛠 Gán giá trị rating ngẫu nhiên từ 3.0 đến 5.0
            database.execSQL("UPDATE products SET rating = (ABS(RANDOM()) % 20 + 30) / 10.0 WHERE rating = 0");

            // 🛠 Gán giá trị sold ngẫu nhiên từ 1 đến 500
            database.execSQL("UPDATE products SET sold = ABS(RANDOM()) % 500 + 1 WHERE sold = 0");
        }
    };



    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .addMigrations(MIGRATION_1_2)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}