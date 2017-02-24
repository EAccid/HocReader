package com.eaccid.hocreader.underdevelopment;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "directories")
public class Directory {

    @DatabaseField(columnName = "id", canBeNull = false, id = true)
    private long id;

    @DatabaseField
    private String path;

    public Directory(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Directory{" +
                "id=" + id +
                ", path='" + path + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Directory directory = (Directory) o;
        if (getId() != directory.getId()) return false;
        return getPath().equals(directory.getPath());
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + getPath().hashCode();
        return result;
    }
}
