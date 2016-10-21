package com.eaccid.bookreader.db.service;

import com.eaccid.bookreader.db.entity.Book;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;


import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;

public class BookDaoService implements Crud {

    private Dao<Book, String> dao = null;

    BookDaoService(DatabaseHelper dbHelper) throws SQLException {
        dao = DaoManager.createDao(dbHelper.getConnectionSource(), Book.class);
    }

    @Override
    public boolean createOrUpdate(Object book) {

        boolean created = false;

        try {
            Dao.CreateOrUpdateStatus status = dao.createOrUpdate((Book) book);
            created = status.isCreated() || status.isUpdated();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return created;
    }

    @Override
    public boolean delete(Object book) {
        try {
            return dao.delete((Book) book) == 1;
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

    public List<Book> getAll() {

        List<Book> books = new ArrayList<>();
        try {
            books = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

}
