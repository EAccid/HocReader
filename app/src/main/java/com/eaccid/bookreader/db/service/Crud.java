package com.eaccid.bookreader.db.service;

import java.util.List;

public interface Crud {

    public boolean createOrUpdate(Object object);
    public boolean delete(Object object);
    public Object getById(String id);
    public List<?> getAll();

}
