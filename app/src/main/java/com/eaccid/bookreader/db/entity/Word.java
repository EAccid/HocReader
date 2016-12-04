package com.eaccid.bookreader.db.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "words")
public class Word implements Serializable {

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true, columnName = "_id")
    private long id;

    @DatabaseField(index = true, indexName = "word_index")
    private String word;

    @DatabaseField
    private String translation;

    @DatabaseField
    private String context;

    @DatabaseField
    private int page;

    @DatabaseField(foreign = true, foreignColumnName = "id")
    private  Book book;

    @DatabaseField
    private boolean enabledOnline;

    public Word() {

    }

    public Word(String word, String translation, String context, int page, Book book, boolean enabledOnline) {
        this.word = word;
        this.translation = translation;
        this.context = context;
        this.page = page;
        this.book = book;
        this.enabledOnline = enabledOnline;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return word;
    }

    public void setName(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public boolean isEnabledOnline() {
        return enabledOnline;
    }

    public void setEnabledOnline(boolean enabledOnline) {
        this.enabledOnline = enabledOnline;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id='" + id + '\'' +
                ", word='" + word + '\'' +
                ", translation='" + translation + '\'' +
                ", context='" + context + '\'' +
                ", page=" + page +
                ", book=" + book +
                ", enabledOnline=" + enabledOnline +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word1 = (Word) o;

        if (!word.equals(word1.word)) return false;
        return book.equals(word1.book);

    }

    @Override
    public int hashCode() {
        int result = word.hashCode();
        result = 31 * result + book.hashCode();
        return result;
    }
}
