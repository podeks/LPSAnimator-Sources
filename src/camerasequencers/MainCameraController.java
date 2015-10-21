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
package camerasequencers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author pdokos
 */
public class MainCameraController implements KeyListener {
    
    private MainCameraSequencer cs;
    
    public MainCameraController(MainCameraSequencer sequencer) {
        cs = sequencer;
    }

        public void keyTyped(KeyEvent ke) {
        }

        public void keyPressed(KeyEvent ke) {

            if (ke.getKeyCode() == KeyEvent.VK_P) {
                cs.flipSwitch();
            } else if (ke.getKeyCode() == KeyEvent.VK_PERIOD) {
                if (cs.isOn()) {
                    cs.accelerate();
                }
            } else if (ke.getKeyCode() == KeyEvent.VK_COMMA) {
                if (cs.isOn()) {
                    cs.decelerate();
                }
            } else {
                if (!cs.isOn()) {
                    switch (ke.getKeyCode()) {

                        case KeyEvent.VK_UP:
                            cs.step();
                            break;
                        case KeyEvent.VK_DOWN:
                            cs.stepBack();
                            break;
                        case KeyEvent.VK_LEFT:
                            cs.stepBack();
                            break;
                        case KeyEvent.VK_RIGHT:
                            cs.step();
                            break;
                    }
                }

            }
        }

        public void keyReleased(KeyEvent ke) {
        }
    }