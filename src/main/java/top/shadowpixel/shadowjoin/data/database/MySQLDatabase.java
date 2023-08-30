package top.shadowpixel.shadowjoin.data.database;

import lombok.Cleanup;
import lombok.var;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowjoin.util.Logger;
import top.shadowpixel.shadowcore.api.database.mysql.SimpleMySQLDatabase;

import java.sql.SQLException;

public class MySQLDatabase extends SimpleMySQLDatabase {

    public MySQLDatabase(String host, String port, String database, String user, String password) {
        super(ShadowJoin.getInstance(), host, port, database, user, password);
    }

    @Override
    public void doAfterConnecting() {
        var table = ((ShadowJoin) plugin).getConfiguration().getString("Data.MySQL.Table");

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
