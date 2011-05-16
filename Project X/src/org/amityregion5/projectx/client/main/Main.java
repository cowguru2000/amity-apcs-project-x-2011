/**
 * Copyright (c) 2011 Amity AP CS A Students of 2010-2011.
 *
 * ex: set filetype=java expandtab tabstop=4 shiftwidth=4 :
 *
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation.
 */
package org.amityregion5.projectx.client.main;

import org.amityregion5.projectx.client.communication.MulticastCommunicationHandler;
import org.amityregion5.projectx.client.gui.ServerChooserWindow;
import org.amityregion5.projectx.client.gui.SplashScreen;

/**
 * Main class for loading the Client
 * 
 * @author Daniel Centore
 * 
 */
public class Main {

    public static final int SPLASH_TIME = 1000; // how long to show the splash screen (ms)

    public static void main(String[] args)
    {
        final ServerChooserWindow chooser = new ServerChooserWindow();

        MulticastCommunicationHandler mch = new MulticastCommunicationHandler();
        mch.registerListener(chooser);
        mch.start();

        SplashScreen s = new SplashScreen();
        try
        {
            Thread.sleep(SPLASH_TIME);
        } catch (InterruptedException e)
        {
        }

        s.setVisible(false);
        chooser.setVisible(true);
    }

}
