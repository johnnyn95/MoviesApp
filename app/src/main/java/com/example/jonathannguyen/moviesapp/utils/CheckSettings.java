package com.example.jonathannguyen.moviesapp.utils;

import android.app.Application;

public class CheckSettings  {
    private static String LANGUAGE;
    private static Boolean NOTIFICATIONS = true;
    public static volatile CheckSettings checkSettings;

    public CheckSettings(Application application,String language){
        LANGUAGE = language;
    }

    public static CheckSettings getInstance(Application application){
        if(checkSettings == null) {
            checkSettings = new CheckSettings(application,LANGUAGE);
        }
        return checkSettings;
    }
    public void setLANGUAGE(String LANGUAGE) {
        if(!(LANGUAGE == null || LANGUAGE.equals(""))) {
            this.LANGUAGE = LANGUAGE;
        } else
            this.LANGUAGE = "en-US";
    }

    public String getLANGUAGE() {
        if(LANGUAGE == null || LANGUAGE.equals(""))
        {
            return "en-US";
        } else
        return LANGUAGE;
    }

    public static Boolean getNOTIFICATIONS() {
        return NOTIFICATIONS;
    }

    public static void setNOTIFICATIONS(Boolean NOTIFICATIONS) {
        CheckSettings.NOTIFICATIONS = NOTIFICATIONS;
    }
}
