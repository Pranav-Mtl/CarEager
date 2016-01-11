package com.careager;

import com.careager.Configuration.FontsOverride;

/**
 * Created by Pranav Mittal on 10/31/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/Montserrat-Regular.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE","fonts/Montserrat-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/Montserrat-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/Montserrat-Regular.ttf");


    }

}

