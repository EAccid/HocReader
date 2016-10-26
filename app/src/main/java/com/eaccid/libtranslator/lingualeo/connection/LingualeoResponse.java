package com.eaccid.libtranslator.lingualeo.connection;

import javax.json.*;

import java.io.InputStream;
import java.util.ArrayList;

public class LingualeoResponse {

    //TODO Gson, JsonPath
    JsonObject jsonObject;

    public void setData(InputStream in) {
        JsonReader jsonReader = Json.createReader(in);
        jsonObject = jsonReader.readObject();
    }

    public String getString(String key) {
        if (isEmpty()) return "";

        String returnValue = jsonObject.getString(key, "");

        if (returnValue.isEmpty()) {
            try {
                returnValue = getListString(key).get(0);
            } catch (Exception ignored) {
            }
        }

        return returnValue;

    }

    public ArrayList<String> getListString(String key) {
        ArrayList<String> listResponse = new ArrayList<>();

        try {
            for (JsonValue jv : jsonObject.values()
                    ) {
                if (jv.getValueType() == JsonValue.ValueType.ARRAY)
                    for (JsonValue jsonValue : (JsonArray) jv
                            ) {
                        JsonObject jsonObject = (JsonObject) jsonValue;
                        String value = jsonObject.getString(key, "");
                        if (!value.isEmpty())
                            listResponse.add(value);
                    }
            }

        } catch (Exception ignored) {
        }
        return listResponse;
    }

    public Boolean getBoolean(String key) {
        if (isEmpty()) return false;
        return jsonObject.getBoolean(key, false);
    }

    public boolean isEmpty() {
        return jsonObject == null || jsonObject.size() == 0;
    }

    @Override
    public String toString() {
        if (jsonObject != null) {
            return jsonObject.toString();
        }
        return super.toString();
    }
}
