package top.shadowpixel.shadowjoin.data.database;

import lombok.Cleanup;
import lombok.var;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowjoin.util.Logger;
import top.shadowpixel.shadowcore.api.database.sqlite.SimpleSQLiteDatabase;

import java.io.File;
import java.sql.SQLException;

public class SQLiteDatabase extends SimpleSQLiteDatabase {

    public SQLiteDatabase(File file, String table) {
        super(ShadowJoin.getInstance(), file, table);
    }

    @Override
    public void doAfterConnecting() {
        var table = ((ShadowJoin) plugin).getConfiguration().getString("Data.SQLite.Table");

        try {
            @Cleanup var stmt = getConnection().createStatement();
            //TODO Change stmt.
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + table + "  (UUID VARCHAR(50) PRIMARY KEY , Format TEXT)");
        } catch (SQLException e) {
            Logger.error("An error occurred while creating table", e);
        }

        ((ShadowJoin) plugin).getDataManager().loadOnline();
    }
}
