package animation;

import animationmodel.LPSOrbitCrawler;
import camerasequencers.PivotCameraSequencer;
import camerasequencers.SyncedPivotController;
import javax.media.opengl.GL;

/**
 * 
 * @author pdokos
 */
public class LPSPivotRenderer extends AbstractRenderer {

    private LPSOrbitCrawler orbitCrawler;
    
    private PivotCameraSequencer cs;
    
    private boolean advance;
    
    private float targetWidth;
    

    public LPSPivotRenderer(LPSOrbitCrawler ad, double t2, int axis, boolean advance) {
        this.orbitCrawler = ad;
        cs = new PivotCameraSequencer(t2, axis, advance);

        this.advance=advance;
        targetWidth=2.0f;
    }
    
    public void resetLPSOrbitCrawler(LPSOrbitCrawler ad) {
        this.orbitCrawler=ad;
        cs.initState();
    }
    
    public void setAdvance(boolean adv) {
        advance = adv;
    }
    
    public void pivotStep(boolean forward) {
        cs.pivotStep(forward);
    }
    
    public void addToSynchronizedKeyListener(SyncedPivotController kl) {
        kl.add(cs);
    }
    
    public void syncRenderer(LPSPivotRenderer renderer) {
        cs.syncSequencer(renderer.cs);
    }
    
    public void switchPivotAnimator() {
        cs.switchPivotAnimator();
    }
    
    public final void initState() {
        cs.initState();
    }

    @Override
    protected void draw(GL gl) {
        
        cs.next();
        cs.setCamera(gl, glu, (float) orbitCrawler.getDimension(), 640, 400);
        if (advance) {
            orbitCrawler.drawGraph(gl, 0, targetWidth, 1.0f);//1.0f);//cycleNum % 3);
        } else {
            orbitCrawler.displayGraph(gl, 0, targetWidth, 1.0f);//1.0f);//cycleNum % 3);
        }
    }

}
