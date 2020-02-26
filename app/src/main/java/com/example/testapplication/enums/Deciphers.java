package com.example.testapplication.enums;

public enum Deciphers {
    INSTANCE;

    public static String CONSUMER_ID(){
        return "@id@";
    }

    public static String DATE(){
        return "@date@";
    }

    public static String NAME(){
        return "@name@";
    }

    public static String LOCATION(){
        return "@location@";
    }

    public static String STATUS(){
        return "@status@";
    }

    public static String TOTAL_PRICE() {
        return "$";
    }

    public static String CLASSIFICATION(){
        return "*";
    }

    public static String ITEM_ID(){
        return "@i@";
    }

    public static String ITEM(){
        return "@item@";
    }

    public static String QUANITY(){
        return "-";
    }
}
