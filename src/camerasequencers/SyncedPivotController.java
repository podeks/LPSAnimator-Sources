package camerasequencers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pdokos
 */
public class SyncedPivotController implements KeyListener {

    List<PivotCameraSequencer> pivotSeqs;

    public SyncedPivotController() {
        pivotSeqs = new ArrayList<PivotCameraSequencer>();
    }

    public void add(PivotCameraSequencer sequencer) {
        pivotSeqs.add(sequencer);
    }

    public void pivotStep(boolean forward) {
        for (PivotCameraSequencer cs : pivotSeqs) {
            cs.pivotStep(forward);
        }
    }

    public void flipPivotAnimationSwitch() {
        for (PivotCameraSequencer cs : pivotSeqs) {
            cs.switchPivotAnimator();
        }
    }

    public void keyTyped(KeyEvent ke) {
    }

    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_PERIOD) {
            pivotStep(true);
        }
        if (ke.getKeyCode() == KeyEvent.VK_COMMA) {
            pivotStep(false);
        }
//                if (ke.getKeyCode() == KeyEvent.VK_R) {
//                    initializePanelRenderers();
//                }
        if (ke.getKeyCode() == KeyEvent.VK_P) {
            flipPivotAnimationSwitch();
        }
    }

    public void keyReleased(KeyEvent ke) {
    }
}