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
package gloctahedralutility;

import javax.media.opengl.GL;

/**
 *
 * @author pdokos
 */
public class GLOctahedralUtility {    
    
    public static void drawAxes(GL gl, double scale) {
        
        gl.glColor3d(1.0, 1.0, 0.0);
        gl.glLineWidth(1.0f);

        gl.glBegin(GL.GL_LINES);
        gl.glVertex3d(scale, 0, 0);
        gl.glVertex3d(-scale, 0, 0);
        gl.glEnd();
        gl.glBegin(GL.GL_LINES);
        gl.glVertex3d(0, scale, 0);
        gl.glVertex3d(0, -scale, 0);
        gl.glEnd();
        gl.glBegin(GL.GL_LINES);
        gl.glVertex3d(0, 0, scale);
        gl.glVertex3d(0, 0, -scale);
        gl.glEnd();
        
        for (int sgn = -1; sgn <= 1; sgn += 2) {
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3d(0, sgn * scale, scale);
            gl.glVertex3d(0, -sgn * scale, -scale);
            gl.glEnd();
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3d(sgn * scale, 0, scale);
            gl.glVertex3d(-sgn * scale, 0, -scale);
            gl.glEnd();
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3d(sgn * scale, scale, 0);
            gl.glVertex3d(-sgn * scale, -scale, 0);
            gl.glEnd();
        }

        for (int sgn1 = -1; sgn1 <= 1; sgn1 += 2) {
            for (int sgn2 = -1; sgn2 <= 1; sgn2 += 2) {
                gl.glBegin(GL.GL_LINES);
                gl.glVertex3d(sgn1 * scale, sgn2 * scale, scale);
                gl.glVertex3d(-sgn1 * scale, -sgn2 * scale, -scale);
                gl.glEnd();
            }
        }
        
    }
    
    public static void drawOctahedron(GL gl, double scale) {
        
        gl.glColor3d(0, 0.8, 1.0);
        gl.glLineWidth(4.0f);
        for (int sgn1 = -1; sgn1 <= 1; sgn1 += 2) {
            for (int sgn2 = -1; sgn2 <= 1; sgn2 += 2) {
                gl.glBegin(GL.GL_LINES);
                gl.glVertex3d(0, 0, sgn2 * scale);
                gl.glVertex3d(sgn1 * scale, 0, 0);
                gl.glEnd();
                gl.glBegin(GL.GL_LINES);
                gl.glVertex3d(0, 0, sgn2 * scale);
                gl.glVertex3d(0, sgn1 * scale, 0);
                gl.glEnd();
                gl.glBegin(GL.GL_LINES);
                gl.glVertex3d(sgn1 * scale, 0, 0);
                gl.glVertex3d(0, sgn2 * scale, 0);
                gl.glEnd();
            }
        }
    }
    
    public static void drawCube(GL gl, double scale) {
        
        gl.glColor3d(0.4, 0.7, 1.0);//(1.0, 0.3, 0.6);
        gl.glLineWidth(4.0f);//(edgeAtt.getWidth());
        
        for (int sgn1 = -1; sgn1 <= 1; sgn1 += 2) {
            for (int sgn2 = -1; sgn2 <= 1; sgn2 += 2) {
                gl.glBegin(GL.GL_LINES);
                gl.glVertex3d(sgn1 * scale, sgn2 * scale, scale);
                gl.glVertex3d(sgn1 * scale, sgn2 * scale, -scale);
                gl.glEnd();
                gl.glBegin(GL.GL_LINES);
                gl.glVertex3d(sgn1 * scale, scale, sgn2 * scale);
                gl.glVertex3d(sgn1 * scale, -scale, sgn2 * scale);
                gl.glEnd();
                gl.glBegin(GL.GL_LINES);
                gl.glVertex3d(scale, sgn1 * scale, sgn2 * scale);
                gl.glVertex3d(-scale, sgn1 * scale, sgn2 * scale);
                gl.glEnd();
            }
        }
       
    }
    
    public static void drawCubeRefPlaneInts(GL gl, double scale) {
        
        gl.glColor3d(0.0, 1.0, 0.2);
        gl.glLineWidth(2.0f);

        for (int sgn = -1; sgn <= 1; sgn += 2) {
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3d(sgn * scale, 0, scale);
            gl.glVertex3d(sgn * scale, 0, -scale);
            gl.glEnd();
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3d(sgn * scale, scale, 0);
            gl.glVertex3d(sgn * scale, -scale, 0);
            gl.glEnd();
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3d(0, sgn * scale, scale);
            gl.glVertex3d(0, sgn * scale, -scale);
            gl.glEnd();
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3d(scale, sgn * scale, 0);
            gl.glVertex3d(-scale, sgn * scale, 0);
            gl.glEnd();
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3d(0, scale, sgn * scale);
            gl.glVertex3d(0, -scale, sgn * scale);
            gl.glEnd();
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3d(scale, 0, sgn * scale);
            gl.glVertex3d(-scale, 0, sgn * scale);
            gl.glEnd();
        }
        
        for (int sgn1 = -1; sgn1 <= 1; sgn1 += 2) {
            for (int sgn2 = -1; sgn2 <= 1; sgn2 += 2) {
                gl.glBegin(GL.GL_LINES);
                gl.glVertex3d(sgn1 * scale, scale, sgn2 * scale);
                gl.glVertex3d(sgn1 * scale, -scale, -sgn2 * scale);
                gl.glEnd();
                gl.glBegin(GL.GL_LINES);
                gl.glVertex3d(scale, sgn1 * scale, sgn2 * scale);
                gl.glVertex3d(-scale, sgn1 * scale, -sgn2 * scale);
                gl.glEnd();
                gl.glBegin(GL.GL_LINES);
                gl.glVertex3d(scale, sgn2 * scale, sgn1 * scale);
                gl.glVertex3d(-scale, -sgn2 * scale, sgn1 * scale);
                gl.glEnd();
            }
        }

    }
    
    public static void drawOctRefPlaneInts(GL gl, double scale) {
        
        double halfScale = scale/2;
        
        gl.glColor3d(0.0, 1.0, 0.2);
        gl.glLineWidth(2.0f);
        for (int sgn = -1; sgn <= 1; sgn += 2) {
            for (int sgn1 = -1; sgn1 <= 1; sgn1 += 2) {
                for (int sgn2 = -1; sgn2 <= 1; sgn2 += 2) {
                    gl.glBegin(GL.GL_LINES);
                    gl.glVertex3d(0, 0, sgn * scale);
                    gl.glVertex3d(sgn1 * halfScale, sgn2 * halfScale, 0);
                    gl.glEnd();
                    gl.glBegin(GL.GL_LINES);
                    gl.glVertex3d(0, sgn * scale, 0);
                    gl.glVertex3d(sgn1 * halfScale, 0, sgn2 * halfScale);
                    gl.glEnd();
                    gl.glBegin(GL.GL_LINES);
                    gl.glVertex3d(sgn * scale, 0, 0);
                    gl.glVertex3d(0, sgn1 * halfScale, sgn2 * halfScale);
                    gl.glEnd();
                }
            }
        }
    }
    
}
