/*
 * Copyright (C) 2015 Pericles Dokos
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package lpsanimator;

import com.apple.eawt.AboutHandler;
import com.apple.eawt.AppEvent;
import com.apple.eawt.Application;
import javax.swing.WindowConstants;

/**
 *
 * @author pdokos
 */
public class MainClass {

    public static void main(String args[]) {
        
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "LPS Visualizer");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        
        Application macApplication = Application.getApplication();
        // About menu handler
        macApplication.setAboutHandler(new AboutHandler() {
            public void handleAbout(AppEvent.AboutEvent ae) {
                AboutDialog.showAboutDialog();
            }
        });
        
        final TopComponent frame = new TopComponent();
        
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(640, 400); 
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

//frame.getContentPane().setPreferredSize(new Dimension(640, 388));