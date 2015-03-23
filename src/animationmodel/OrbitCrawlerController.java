package animationmodel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author pdokos
 */
public class OrbitCrawlerController implements KeyListener {

    private LPSOrbitCrawler ad;

    public OrbitCrawlerController(LPSOrbitCrawler orbCrawler) {
        ad = orbCrawler;
    }

    public void resetCrawler(LPSOrbitCrawler orbCrawler) {
        ad = orbCrawler;
    }

    public void keyTyped(KeyEvent ke) {
    }

    public void keyPressed(KeyEvent ke) {

        if (ke.getKeyCode() == KeyEvent.VK_W) {
            ad.flipSwitch();
        } else if (ke.getKeyCode() == KeyEvent.VK_J) {
            ad.jump();
        } else if (ke.getKeyCode() == KeyEvent.VK_H) {
            ad.flipShowShadows();
        } else if (ke.getKeyCode() == KeyEvent.VK_T) {
            ad.flipShowTest();
        } else if (ke.getKeyCode() == KeyEvent.VK_V) {
            ad.flipShowVerts();
        } else if (ke.getKeyCode() == KeyEvent.VK_I) {
            ad.flipShowPointsAtInfinity();
        } else if (ke.getKeyCode() == KeyEvent.VK_X) {
            ad.switchSpVertConc();
        } else if (ke.getKeyCode() == KeyEvent.VK_E) {
            ad.flipShowEdges();
        } else if (ke.getKeyCode() == KeyEvent.VK_C) {
            ad.flipShowCube();
        } else if (ke.getKeyCode() == KeyEvent.VK_O) {
            ad.flipShowOctahedron();
        } else if (ke.getKeyCode() == KeyEvent.VK_MINUS) {
            ad.incrementCrawlSteps();
        } else if (ke.getKeyCode() == KeyEvent.VK_EQUALS) {
            ad.decrementCrawlSteps();
        } else if (ke.getKeyCode() == KeyEvent.VK_OPEN_BRACKET) {
            ad.decrementDelaySteps();
        } else if (ke.getKeyCode() == KeyEvent.VK_CLOSE_BRACKET) {
            ad.incrementDelaySteps();
        } else {
            if (!ad.isOn()) {
                switch (ke.getKeyCode()) {

                    case KeyEvent.VK_LEFT:
                        ad.stepBack();
                        break;
                    case KeyEvent.VK_RIGHT:
                        ad.step();
                        break;
                }
            }
        }
    }

    public void keyReleased(KeyEvent ke) {
    }
}