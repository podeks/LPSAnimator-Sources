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