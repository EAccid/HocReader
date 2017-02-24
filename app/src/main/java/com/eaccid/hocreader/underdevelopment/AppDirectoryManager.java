package com.eaccid.hocreader.underdevelopment;

import com.eaccid.hocreader.underdevelopment.Directory;
import com.eaccid.hocreader.data.local.db.service.DatabaseManager;
import com.eaccid.hocreader.underdevelopment.DirectoryDaoService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


//TODO save directories into db
public class AppDirectoryManager {

    private final String LOG_TAG = "AppDirectoryManager";
    private DatabaseManager mDatabaseManager;

    public AppDirectoryManager(DatabaseManager mDatabaseManager) {
        this.mDatabaseManager = mDatabaseManager;
    }

    public List<Directory> getAllDirectoriess() {
        try {
            DirectoryDaoService ds = mDatabaseManager.getDirectoryService();
            return ds.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
