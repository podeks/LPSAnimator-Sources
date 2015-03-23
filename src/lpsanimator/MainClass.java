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