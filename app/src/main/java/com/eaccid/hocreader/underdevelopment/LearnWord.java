package com.eaccid.hocreader.underdevelopment;

import com.eaccid.hocreader.data.local.db.entity.Word;
import com.j256.ormlite.field.DatabaseField;

public class LearnWord {

    @DatabaseField(columnName = "id", canBeNull = false, id = true)
    private long id;

    @DatabaseField(foreign = true, foreignColumnName = "id")
    private Word word;

    @DatabaseField
    private long percent;
}
