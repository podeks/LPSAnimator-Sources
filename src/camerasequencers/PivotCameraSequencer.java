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

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author pdokos
 */
public class PivotCameraSequencer {
    private int axis;
    private double initT2;
    private double offset;
    private double cosT2;
    private double sinT2;
    
    private static double root2over2 = Math.sqrt(2) / 2.0;
    private static double oneDegree=Math.PI/180.0;
    
    private boolean pivotAnimationMode;


    public PivotCameraSequencer(double t2, int axis, boolean advance) {
        
        initT2=t2;
        initState();
        this.axis=axis;

        pivotAnimationMode=false;
    }

    
    public void pivotStep(boolean forward) {
        if (pivotAnimationMode==false) {
            pivot(forward);
        }
    }
    
    private boolean pivot(boolean forward){
        
        if (forward) {
            offset+=oneDegree;
        } else {
            offset-=oneDegree;
        }
        
        boolean multOfPiReached=false;
        
        if ((Math.abs(offset-Math.PI))<(oneDegree/2.0)){
            offset=Math.PI;
            multOfPiReached = true;
            
        } else if ((Math.abs(offset-2*Math.PI))<(oneDegree/2.0)){
            offset=0;
            multOfPiReached = true;
            
        }
        cosT2 = Math.cos(initT2+offset);
        sinT2 = Math.sin(initT2+offset);
        return multOfPiReached;
        
    }
    
public void syncSequencer(PivotCameraSequencer cs) {
        cs.offset=offset;
        cs.pivotAnimationMode = pivotAnimationMode;
        cs.cosT2 = Math.cos(cs.initT2+offset);
        cs.sinT2 = Math.sin(cs.initT2+offset);
        
    }
    
    public void switchPivotAnimator() {
        pivotAnimationMode=!pivotAnimationMode;
    }
    
    public void next() {
        if (pivotAnimationMode) {
            if (pivot(true)){
                switchPivotAnimator();
            }
            
        }
    }
    
    public final void initState() {
        offset=0;
        cosT2 = Math.cos(initT2+offset);
        sinT2 = Math.sin(initT2+offset);
    }

    public void setCamera(GL gl, GLU glu, float distance, float componentWidth, float componentHeight) {
        // Change to projection matrix.
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        // Perspective.
        float widthHeightRatio = componentWidth / componentHeight;
        glu.gluPerspective(45, widthHeightRatio, 1, 50 * distance);

        if (axis == 0) {
            glu.gluLookAt(distance * cosT2, 0, distance * sinT2, 0, 0, 0, -sinT2, 0, cosT2);
        } else if (axis == 1) {
            glu.gluLookAt(distance * cosT2, distance * sinT2 * root2over2, distance * sinT2 * root2over2, 0, 0, 0,
                    -sinT2, (cosT2 - 1) * root2over2, (cosT2 + 1) * root2over2);
        } else if (axis == 2) {
            //double alpha = (initT2+offset) / ((2.0 * 1.0 + 3.25 - (1.0 / 12.0)) * Math.PI);
            double perpAdj = 1;//2 * Math.sqrt(6) / 3.0 + Math.sqrt(3) - alpha * (Math.sqrt(6) + Math.sqrt(3));
            glu.gluLookAt(-distance * sinT2 * Math.sqrt(6) / 3.0, distance * cosT2 * root2over2 + distance * sinT2 * Math.sqrt(6) / 6.0, distance * cosT2 * root2over2 - distance * sinT2 * Math.sqrt(6) / 6.0, 0, 0, 0,
                    -cosT2 * Math.sqrt(6) / 3.0 - perpAdj, -sinT2 * root2over2 + cosT2 * Math.sqrt(6) / 6.0 - perpAdj, -sinT2 * root2over2 - cosT2 * Math.sqrt(6) / 6.0 + perpAdj);
        }

        // Change back to model view matrix.
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
}
