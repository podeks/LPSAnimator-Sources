package camerasequencers;

import java.awt.Toolkit;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author pdokos
 */
public class MainCameraSequencer {

    private boolean isOn;
    private double t;
    private double timeStep;
    private double timeInterval;
    private double speed;
    private double speedIncrement;
    private double a;//=-0.75*Math.PI;
    private double b1;// = 2.0*numOrbits + 0.75*Math.PI;
    private double b2;// = 4.0*numOrbits + 1.25*Math.PI;
    private int cycleNumber = 0;

    public MainCameraSequencer(int numOrbs) {

        setNumOrbits(numOrbs);

        isOn = true;

        speed = 1.0;
        speedIncrement = 0.05;

        t = 0;
        timeStep = speed * Math.PI / 180;
    }

    public void initialize() {
        t = 0;
        speed =1.0;
        cycleNumber = 0;
        timeStep = speed * Math.PI / 180;
        isOn=true;
    }

    public int next() {
        if (isOn) {
            return step();
        }
        return cycleNumber;
    }

    public int step() {
        t = ((t + timeStep) % timeInterval);
        if (t < timeStep) {
            cycleNumber++;
        }
        return cycleNumber;
    }

    public int stepBack() {
        t = ((t - timeStep) % timeInterval);
        if (t < 0) {
            t = timeInterval - timeStep;
            cycleNumber--;
        }
        return cycleNumber;
    }

    public boolean isOn() {
        return isOn;
    }

    public void flipSwitch() {
        isOn = !isOn;
    }

    /**
     * @param gl The GL context.
     * @param glu The GL unit.
     * @param distance The distance from the screen.
     */
    public void setCamera(GL gl, GLU glu, float distance, float componentWidth, float componentHeight) {
        // Change to projection matrix.
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        // Perspective.
        float widthHeightRatio = componentWidth / componentHeight;
        glu.gluPerspective(45, widthHeightRatio, 1, 50 * distance);

        if (t < b1) {
            double t2 = t + a;
            double cosT2 = Math.cos(t2);
            double sinT2 = Math.sin(t2);
            glu.gluLookAt(distance * cosT2, 0, distance * sinT2, 0, 0, 0, -sinT2, 0, cosT2);
        } else if (t < b2) {
            double t2 = t - b1;
            double root2over2 = Math.sqrt(2) / 2.0;
            double cosT2 = Math.cos(t2);
            double sinT2 = Math.sin(t2);
            glu.gluLookAt(distance * cosT2, distance * sinT2 * root2over2, distance * sinT2 * root2over2, 0, 0, 0,
                    -sinT2, (cosT2 - 1) * root2over2, (cosT2 + 1) * root2over2);
        } else {
            double t2 = t - b2;
            double alpha = t2 / (timeInterval - b2);
            double root2over2 = Math.sqrt(2) / 2.0;
            double root6over6 = Math.sqrt(6) / 6.0;
            double cosT2 = Math.cos(t2);
            double sinT2 = Math.sin(t2);
            double perpAdj = 4 * root6over6 + Math.sqrt(3) - alpha * (6 * root6over6 + Math.sqrt(3));
            glu.gluLookAt(-distance * sinT2 * 2 * root6over6, distance * cosT2 * root2over2 + distance * sinT2 * root6over6, distance * cosT2 * root2over2 - distance * sinT2 * root6over6, 0, 0, 0,
                    -cosT2 * 2 * root6over6 - perpAdj, -sinT2 * root2over2 + cosT2 * root6over6 - perpAdj, -sinT2 * root2over2 - cosT2 * root6over6 + perpAdj);
        }

        // Change back to model view matrix.
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void accelerate() {
        if (speed <= 15) {
            speed += this.speedIncrement + speed / 20.0;
            timeStep = this.speed * Math.PI / 180;
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void decelerate() {
        double increment = speedIncrement + speed / 20.0;
        if (speed > increment) {
            speed -= increment;
            timeStep = speed * Math.PI / 180;
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
        timeStep = speed * Math.PI / 180;
    }

    public final void setNumOrbits(int n) {
        timeInterval = (6.0 * n + 2.0 - (1.0 / 12.0)) * Math.PI;
        a = -0.75 * Math.PI;
        b1 = (2.0 * n + 0.75) * Math.PI;
        b2 = (4.0 * n + 1.25) * Math.PI;
    }

    
}
