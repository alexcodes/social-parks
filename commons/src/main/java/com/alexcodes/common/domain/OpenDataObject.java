package com.alexcodes.common.domain;

public class OpenDataObject {
    public Integer id;
    public String name;
    public Location location;
    public Type type;

    public static enum Type {
        LEISURE, CULTURE, CATERING
    }
}
