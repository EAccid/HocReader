package com.eaccid.hocreader.data.local.db.service;

import com.eaccid.hocreader.data.local.db.entity.Directory;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DirectoryDaoService implements Crud {

    private Dao<Directory, String> dao = null;

    DirectoryDaoService(DatabaseHelper dbHelper) throws SQLException {
        dao = DaoManager.createDao(dbHelper.getConnectionSource(), Directory.class);
    }

    @Override
    public boolean createOrUpdate(Object directory) {
        boolean created = false;
        try {
            Dao.CreateOrUpdateStatus status = dao.createOrUpdate((Directory) directory);
            created = status.isCreated() || status.isUpdated();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return created;
    }

    @Override
    public boolean delete(Object directory) {
        try {
            return dao.delete((Directory) directory) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Object getById(String id) {
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Directory> getAll() {
        List<Directory> directories = new ArrayList<>();
        try {
            directories = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return directories;
    }

}
