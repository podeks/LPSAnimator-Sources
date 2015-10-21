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
