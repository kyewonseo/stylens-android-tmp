package net.bluehack.stylens.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UiUtil {

    public static String toStringGson(Object object) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        return gson.toJson(object);
    }

}
