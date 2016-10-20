package com.eaccid.bookreader.db.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "books")
public class Book implements Serializable{

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true, columnName = "id")
    int id;

    @DatabaseField
    String name;

    @DatabaseField
    int Pages;

    @ForeignCollectionField(foreignFieldName = "book")
    private ForeignCollection<Word> words;


    public Book() {

    }

    public Book(String name, int pages) {
        this.name = name;
        Pages = pages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPages() {
        return Pages;
    }

    public void setPages(int pages) {
        Pages = pages;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Pages=" + Pages +
                '}';
    }
}
