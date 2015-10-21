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