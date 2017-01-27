package com.eaccid.hocreader.data.local.db.service;

import java.util.List;

public interface Crud {
    boolean createOrUpdate(Object object);
    boolean delete(Object object);
    Object getById(String id);
    List<?> getAll();
}
