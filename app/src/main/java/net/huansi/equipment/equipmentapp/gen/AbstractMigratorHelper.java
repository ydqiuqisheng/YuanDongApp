package net.huansi.equipment.equipmentapp.gen;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by shanz on 2017/5/11.
 */

public abstract class AbstractMigratorHelper {
    public abstract void onUpgrade(SQLiteDatabase db);
}
