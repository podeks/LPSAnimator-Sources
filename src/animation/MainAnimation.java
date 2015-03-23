package animation;

import camerasequencers.MainCameraSequencer;
import animationmodel.LPSOrbitCrawler;
import camerasequencers.MainCameraController;
import com.sun.opengl.util.Animator;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.opengl.GL;
import javax.media.opengl.GLCanvas;
import lpsanimator.TopComponent;

/**
 * An interactive GL renderer for an LPSOrbitCrawler animation which
 * encapsulates a Camera sequencer for moving the view in circular orbits
 * around the graph. The circular orbits are perpendicular to the three types of
 * ...
 *
 * MainAnimation.java <BR>
 * author: pdokos
 */
public class MainAnimation extends AbstractRenderer {//implements GLEventListener {

    private TopComponent parentFrame;
    private GLCanvas panel; 
    
    private LPSOrbitCrawler crawler;
    
    private MainCameraSequencer cs;
    private MainCameraController cameraController;
    
    private KeyListener spacebarControl;

    public MainAnimation(TopComponent topComp, LPSOrbitCrawler model) {
        crawler=model;
        parentFrame = topComp;
        
        cs = new MainCameraSequencer(1);
        cameraController = new MainCameraController(cs);
        spacebarControl = new KeyListener() {

            public void keyTyped(KeyEvent ke) {
            }

            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
                    boolean adOn = crawler.isOn();
                    boolean csOn = cs.isOn();
                    if (adOn && csOn) {
                        cs.flipSwitch();
                        crawler.flipSwitch();
                    } else {
                        if (!csOn) {
                            cs.flipSwitch();
                        }
                        if (!adOn) {
                            crawler.flipSwitch();
                        }
                    }
                }
            }

            public void keyReleased(KeyEvent ke) {
            }
        };
        
        panel = new GLCanvas();
        panel.setFocusable(false);        
        panel.addGLEventListener(this);
        
        show();        
    }
    
    public void attachToAnimator(Animator animator) {
        animator.add(panel);
    }

    public boolean isOn() {
        return cs.isOn() && crawler.isOn();
    }
    
    public void resetLPSOrbitCrawler(LPSOrbitCrawler ad) {
        crawler=ad;
        cs.initialize();
    }
    
    public final void show() {
        Container contentPane = parentFrame.getContentPane();
        contentPane.setLayout(new GridLayout(1, 1));
        panel.reshape(0, 0, contentPane.getWidth(), contentPane.getHeight());
        parentFrame.add(panel);
        parentFrame.addKeyListener(cameraController);
        parentFrame.addKeyListener(spacebarControl);
    }
    
    public void hide() {
        parentFrame.removeKeyListener(cameraController);
        parentFrame.removeKeyListener(spacebarControl);
        parentFrame.remove(panel);
    }

    @Override
    protected void draw(GL gl) {
        int cycleNum = cs.next();
        cs.setCamera(gl, glu, (float) crawler.getDimension(), 8, 5);
        crawler.drawGraph(gl, cycleNum % 3, 5.0f, 2.5f);
        gl.glDisable(GL.GL_POINT_SMOOTH);
    }

}
