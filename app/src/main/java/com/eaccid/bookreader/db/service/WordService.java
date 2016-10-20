package com.eaccid.bookreader.db.service;

import com.eaccid.bookreader.db.entity.Word;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WordService implements Crud{

    private Dao<Word, String> dao;

    protected WordService(DatabaseHelper dbHelper) throws SQLException {
        dao = DaoManager.createDao(dbHelper.getConnectionSource(), Word.class);
    }

    public List<Word> getAll() {

        List<Word> words = new ArrayList<>();
        try {
            words = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return words;
    }

    @Override
    public boolean createOrUpdate(Object word) {

        boolean created = false;

        try {
            Dao.CreateOrUpdateStatus status = dao.createOrUpdate((Word) word);
            created = status.isCreated() || status.isUpdated();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return created;
    }

    @Override
    public boolean delete(Object word) {
        try {
            return dao.delete((Word) word) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Object getById(int id) {
        try {
            return dao.queryForId(String.valueOf(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
