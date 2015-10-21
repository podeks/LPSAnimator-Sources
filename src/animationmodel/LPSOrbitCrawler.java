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

import animationcomps.LPSDrawing;
import animationcomps.LPSOrbitHopper;
import api.IndexedNavigableRootedNeighborGraph;
import basic_operations.Arithmetic;
import cayleygraphs.CayleyGraphBuilder;
import drawingcomponents.CartesianPoint3D;
import drawingcomponents.EdgeDrawingAtts;
import drawingcomponents.VertexDrawingAtts;
import directededge.DirectedEdge;
import java.awt.Toolkit;
import java.util.HashSet;
import java.util.Set;
import javax.media.opengl.GL;
import gloctahedralutility.GLOctahedralUtility;
import java.util.LinkedHashSet;
import quaterniongroup.ProjectiveUnitQuaternion;

/**
 * Model for an animation of an LPS graph which encapsulates an LPSDrawing
 * together with an EdgeSequencer in the form of an LPSOrbitHopperOld.
 *
 * The drawGraph method is designed to be called within the 'display' method of
 * a GLEventListener. A call to this method also advances the state of the
 * LPSOrbitCrawler to that of the next frame of the animation.
 *
 * Use the displayGraph method instead of drawGraph within a GLEventListener to
 * render the graph without advancing the crawler. This should be done when
 * there are multiple renderers of this LPSOrbitCrawler.
 *
 * @author pdokos
 */
public class LPSOrbitCrawler {

    private IndexedNavigableRootedNeighborGraph<ProjectiveUnitQuaternion> lpsGraph;
    private LPSDrawing lpsDrawing;
    private LPSOrbitHopper edgeSequencer;
    private int crawlStepsPlus1; //Number of steps in drawing segment joining vertices.
    private int crawlCount;
    private int delayStepsPlus1; //Length of delay between jumping to the next neighbor.
    private int delayCount;
    private boolean isOn;
    private boolean jumpMode;
    private boolean radialLayout;
    private boolean concentratedSpecVerts;
    private boolean showShadows;
    private boolean showVertices;
    private boolean showEdges;
    private boolean showCube;
    private boolean showOctahedron;
    private boolean showTest;
    private boolean showPointsAtInfinity;
    private int colorPhaseCount;
    private double a1 = 0.25;
    private double a2 = 1.0;
    private double a3 = 1.0;

    private LPSOrbitCrawler(IndexedNavigableRootedNeighborGraph<ProjectiveUnitQuaternion> graph, int crawlSteps, boolean radialInitLayout, boolean concentratedSpecVerts) {

        lpsGraph = graph;

        lpsDrawing = new LPSDrawing(lpsGraph, radialInitLayout, concentratedSpecVerts);
        edgeSequencer = new LPSOrbitHopper(lpsGraph, 2);

        this.crawlStepsPlus1 = crawlSteps; //50
        crawlCount = 1;
        delayStepsPlus1=1;
        delayCount=1;
        
        colorPhaseCount = 1;
        isOn = true;
        jumpMode=false;
        showShadows = true;
        showVertices = true;
        showPointsAtInfinity = false;
        showEdges = false;
        showOctahedron = false;
        showCube = false;

        radialLayout = radialInitLayout;
        this.concentratedSpecVerts = concentratedSpecVerts;

        showTest = true;

    }

    //Maybe remove crawlStepsPlus1 param and use default of 50.
    public LPSOrbitCrawler(int p, short q, int crawlSteps, boolean radialIntLayout, boolean concentratedSpecVerts) {
        this(CayleyGraphBuilder.getIndexedNavigableCayleyGraph(ProjectiveUnitQuaternion.getLPSGenSet(p, q), new ProjectiveUnitQuaternion(q)), 
                crawlSteps, radialIntLayout, concentratedSpecVerts);
    }

    public IndexedNavigableRootedNeighborGraph<ProjectiveUnitQuaternion> getGraph() {
        return lpsGraph;
    }
    
    public double getDimension() {
        return lpsDrawing.getDimension();
    }

    //A call to displayGraph will only call step() (which advances the animation) if isOn() returns true.
    public boolean isOn() {
        return isOn;
    }

    public void flipSwitch() {
        isOn = !isOn;
    }

    public void switchLayout() {
        lpsDrawing.switchEmbedding();
        radialLayout = !radialLayout;
    }

    public boolean isRadialLayout() {
        return radialLayout;
    }
    
    public void switchSpVertConc() {
        lpsDrawing.switchSpVertConc();
        concentratedSpecVerts = !concentratedSpecVerts;
    }

    public boolean isSpVertConc() {
        return concentratedSpecVerts;
    }

    public void flipShowVerts() {
        if (!showVertices && showPointsAtInfinity) {
            showPointsAtInfinity = !showPointsAtInfinity;
        }
        showVertices = !showVertices;
    }

    public void flipShowPointsAtInfinity() {
        if (showVertices && !showPointsAtInfinity) {
            showVertices = !showVertices;
        }
        showPointsAtInfinity = !showPointsAtInfinity;
    }

    public void flipShowShadows() {
        showShadows = !showShadows;
    }

    public void flipShowEdges() {
        showEdges = !showEdges;
    }

    public void flipShowCube() {
        showCube = !showCube;
    }

    public void flipShowOctahedron() {
        showOctahedron = !showOctahedron;
    }

    public void flipShowTest() {
        showTest = !showTest;
    }

    public void step() {
        crawlCount = (1 + crawlCount) % (crawlStepsPlus1);
        if (crawlCount==0) {
            edgeSequencer.next();
        }
        incrementColor();
    }
    
    private void incrementColor() {
        colorPhaseCount = (1 + colorPhaseCount) % (4 * (crawlStepsPlus1+delayStepsPlus1));
    }

    public void stepBack() {
        crawlCount = Arithmetic.reduce(crawlCount - 1, crawlStepsPlus1);
        colorPhaseCount = Arithmetic.reduce(colorPhaseCount - 1, 4 * (crawlStepsPlus1+delayStepsPlus1));
    }

    public void incrementCrawlSteps() {
        if (crawlStepsPlus1 < 150) {
            crawlStepsPlus1 += 1 + crawlStepsPlus1 / 40;//Math.round(Math.log(crawlStepsPlus1)/2.0);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void decrementCrawlSteps() {
        if (crawlStepsPlus1 > 5) {
            crawlStepsPlus1 -= 1 + crawlStepsPlus1 / 40;
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
    
    public void incrementDelaySteps() {
        if (delayStepsPlus1 <= 101) {
            delayStepsPlus1 += 2 + delayStepsPlus1/10;//25;//Math.round(Math.log(crawlStepsPlus1)/2.0);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void decrementDelaySteps() {
        if (delayStepsPlus1 >= 2) {//26
            delayStepsPlus1 -= 1+ delayStepsPlus1/5;
        } else {
            delayStepsPlus1=1;
            Toolkit.getDefaultToolkit().beep();
        }
    }
    
    public void jump() {
        if (!isOn()) {
            flipSwitch();
            jumpMode=true;
            delayCount=0;
        }
    }
    
    //Remove...
    public int getCrawlSteps() {
        return crawlStepsPlus1;
    }
    
    //Remove...
    public void setCrawlSteps(int num) {
        crawlStepsPlus1=num;
    }

    /*
     * Called within the 'display' method of the GLEventListeners which render
     * this LPSOrbitCrawler.  A call to this method also advances the state of the
     * LPSOrbitCrawler to that of the next frame of the animation.
     * 
     * Use the displayGraph method instead of drawGraph within a GLEventListener 
     * to render the graph without advancing the crawler.  This should be done 
     * when there are multiple renderers of this LPSOrbitCrawler.
     */
    //public void drawGraph(GL gl, int cycleRes) {
    //    displayGraph(gl, cycleRes, true, 5.0f, 2.5f);
    //}

    public void drawGraph(GL gl, int cycleRes, float targetWidth, float lineWidth) {
        displayGraph(gl, cycleRes, true, targetWidth, lineWidth);
    }

    /*
     * Called within the 'display' method of the GLEventListeners which render
     * this LPSOrbitCrawler.  Use this method instead of drawGraph within a 
     * GLEventListener to render the graph without advancing the crawler.  This 
     * should be done when there are multiple renderers of this LPSOrbitCrawler.
     */
    public void displayGraph(GL gl, int cycleRes, float targetSize, float lineWidth) {
        displayGraph(gl, cycleRes, false, targetSize, lineWidth);//5.0f, 2.5f);//1.0f, 1.0f);
    }

    
    
    private void displayGraph(GL gl, int cycleRes, boolean advance, float targetSize, float lineWidth) {

        //First advance the crawl, or increment delay timer if conditions dictate such.
        if (isOn() && advance) {
            if (crawlCount == crawlStepsPlus1 - 1 && delayCount != 0) {
                delayCount = (1 + delayCount) % (delayStepsPlus1);
                incrementColor();
            } else {
                step();
                if (crawlCount == 0) {
                    delayCount = 1 % (delayStepsPlus1); //Initialize to 1 to set delay to start at the end of the crawl.
                    if (jumpMode) {
                        flipSwitch();
                        jumpMode=false;
                    }
                }
            }
        }
        
        double lambda = ((double) crawlCount) / (crawlStepsPlus1-1);

        double sin = a1 * Math.sin(Math.PI * colorPhaseCount / ((double) 2 * (crawlStepsPlus1+delayStepsPlus1)));
        double cos = a1 * Math.cos(Math.PI * colorPhaseCount / ((double) 2 * (crawlStepsPlus1+delayStepsPlus1)));

        //Draw Eges
        Set<DirectedEdge<ProjectiveUnitQuaternion>> edgesToDraw = edgeSequencer.getEdges();
        Set<ProjectiveUnitQuaternion> sources = new HashSet<ProjectiveUnitQuaternion>();
        Set<ProjectiveUnitQuaternion> inQueue = new HashSet<ProjectiveUnitQuaternion>();
        Set<ProjectiveUnitQuaternion> targets = new HashSet<ProjectiveUnitQuaternion>();
        for (DirectedEdge<ProjectiveUnitQuaternion> e : edgesToDraw) {

            EdgeDrawingAtts edgeAtt = lpsDrawing.getEdgeDrawingAtts().getDrawingAtts(e);

            if (edgeSequencer.getBackEdges().contains(e)) {
                sources.add(e.getSource());
                inQueue.add(e.getTarget());
            } else if (edgeSequencer.getFrontEdges().contains(e)) {
                targets.add(e.getTarget());
                inQueue.add(e.getSource());
            } else {
                inQueue.add(e.getSource());
                inQueue.add(e.getTarget());
            }

            /**
             * *********************************************************************************************
             */
            CartesianPoint3D src = lpsDrawing.getEmbedding().getPosition(e.getSource());
            CartesianPoint3D tgt = lpsDrawing.getEmbedding().getPosition(e.getTarget());

            /**
             * *********************************************************************************************
             */
            double r = edgeAtt.getR();
            double g = a2 * (edgeAtt.getG() + sin);
            double b = a3 * (edgeAtt.getB() - cos);

            if (cycleRes == 1) {
                double hold = r;
                r = g;
                g = b;
                b = hold;
            } else if (cycleRes == 2) {
                double hold = r;
                r = (1.0 / a3) * b;
                b = (1.0 / a2) * g;
                g = hold / 2.0;
            }
            gl.glColor3d(r, g, b);
            gl.glLineWidth(lineWidth);

            int crawlType;

            if (edgeSequencer.getFrontEdges().contains(e)) {
                crawlType = 1;
            } else if (edgeSequencer.getBackEdges().contains(e)) {
                crawlType = -1;
            } else {
                crawlType = 0;
            }
            drawSegment(gl, src, tgt, crawlType, lambda);

            // Draw the Shadow Edges associated with points at infinity.
            if (showShadows) {
                drawShadowEdges(gl, e, crawlType, lambda);
            }
        }

        //Draw Vertices
        drawVertices(gl, sources, 3.0f * targetSize / 5.0f, 1.0 -lambda);
        drawVertices(gl, inQueue, targetSize, 1.0);
        drawVertices(gl, targets, targetSize, lambda);
        if (showShadows) {
            drawShadowVertices(gl, sources, 3.0f * targetSize / 5.0f);
            drawShadowVertices(gl, inQueue, targetSize);
            drawShadowVertices(gl, targets, targetSize);
        }

        //Interactive features.
        if (showVertices) {
            drawVertices(gl, lpsGraph.getVertices(), 1.0f, 1.0);
            if (showShadows) {
                drawShadowVertices(gl, lpsGraph.getVertices(), 1.0f);
            }
        }
        if (showPointsAtInfinity) {
            drawPointsAtInfinity(gl, lpsGraph.getVertices(), 0.4f*targetSize);//1.0f);
        }
        if (showEdges) {
            drawEdges(gl);
        }
        if (showCube) {
            GLOctahedralUtility.drawCube(gl, lpsDrawing.getDimension() / 7.0);// /8.0);
            GLOctahedralUtility.drawAxes(gl, lpsDrawing.getDimension() / 4.0);
            GLOctahedralUtility.drawCubeRefPlaneInts(gl, lpsDrawing.getDimension() / 7.0);//8.0);
        }
        if (showOctahedron) {
            GLOctahedralUtility.drawOctahedron(gl, lpsDrawing.getDimension() / 3.5);
            GLOctahedralUtility.drawOctRefPlaneInts(gl, lpsDrawing.getDimension() / 3.5);
        }

        sources.clear();
        inQueue.clear();
        targets.clear();
    }

    private void drawVertices(GL gl, Set<ProjectiveUnitQuaternion> verts, float pointSize, double lambda) {

        for (ProjectiveUnitQuaternion n : verts) {

            VertexDrawingAtts nodeAtts = lpsDrawing.getVertDrawingAtts().getDrawingAtts(n);
            gl.glColor3d(lambda*nodeAtts.getR(), lambda*nodeAtts.getG(), lambda*nodeAtts.getB());
            gl.glEnable(GL.GL_POINT_SMOOTH);
            gl.glPointSize(pointSize);

            CartesianPoint3D s = lpsDrawing.getEmbedding().getPosition(n);

            gl.glBegin(GL.GL_POINTS);
            gl.glVertex3d(s.getX(), s.getY(), s.getZ());
            gl.glEnd();
        }
        gl.glDisable(GL.GL_POINT_SMOOTH);
    }

    private void drawShadowVertices(GL gl, Set<ProjectiveUnitQuaternion> verts, float pointSize) {
        gl.glColor3d(0.4, 0.4, 0.4);
        gl.glEnable(GL.GL_POINT_SMOOTH);
        gl.glPointSize(pointSize);
        for (ProjectiveUnitQuaternion n : verts) {
            if (isSpecialFixedPt(n)) {
                CartesianPoint3D s = lpsDrawing.getEmbedding().getPosition(n);
                gl.glBegin(GL.GL_POINTS);
                gl.glVertex3d(-s.getX(), -s.getY(), -s.getZ());
                gl.glEnd();
            }
        }
        gl.glDisable(GL.GL_POINT_SMOOTH);
    }

    private void drawPointsAtInfinity(GL gl, Set<ProjectiveUnitQuaternion> verts, float pointSize) {
        gl.glEnable(GL.GL_POINT_SMOOTH);
        gl.glPointSize(pointSize);
        for (ProjectiveUnitQuaternion n : verts) {
            if (n.getQuat().getEntry(0) == 0) {
                VertexDrawingAtts nodeAtts = lpsDrawing.getVertDrawingAtts().getDrawingAtts(n);
                gl.glColor3d(nodeAtts.getR(), nodeAtts.getG(), nodeAtts.getB());
                CartesianPoint3D s = lpsDrawing.getEmbedding().getPosition(n);
                gl.glBegin(GL.GL_POINTS);
                gl.glVertex3d(s.getX(), s.getY(), s.getZ());
                gl.glEnd();
                if (isSpecialFixedPt(n)) {
                    gl.glColor3d(0.4, 0.4, 0.4);
                    gl.glBegin(GL.GL_POINTS);
                    gl.glVertex3d(-s.getX(), -s.getY(), -s.getZ());
                    gl.glEnd();
                }
            }
        }
        gl.glDisable(GL.GL_POINT_SMOOTH);
    }

    //
    // Draw the Shadow Edges associated with points at infinity.
    //
    private void drawShadowEdges(GL gl, DirectedEdge<ProjectiveUnitQuaternion> e, int crawlType, double lambda) {
        ProjectiveUnitQuaternion source = e.getSource();
        ProjectiveUnitQuaternion target = e.getTarget();
        CartesianPoint3D srcPos = lpsDrawing.getEmbedding().getPosition(source);
        CartesianPoint3D tgtPos = lpsDrawing.getEmbedding().getPosition(target);
        EdgeDrawingAtts edgeAtt = lpsDrawing.getEdgeDrawingAtts().getDrawingAtts(e);

        if (isSpecialFixedPt(source)) {
            gl.glColor3d(1 - edgeAtt.getR() / 3.0, 1 - edgeAtt.getG() / 3.0, 1 - edgeAtt.getB() / 3.0);
            if (isSpecialFixedPt(target)) {
                if (showTest) {
                    drawSegment(gl, srcPos, tgtPos.negative(), crawlType, lambda);
                    drawSegment(gl, srcPos.negative(), tgtPos, crawlType, lambda);
                }
                drawSegment(gl, srcPos.negative(), tgtPos.negative(), crawlType, lambda);
            } else {
                drawSegment(gl, srcPos.negative(), tgtPos, crawlType, lambda);
            }
        } else if (isSpecialFixedPt(target)) {
            gl.glColor3d(1 - edgeAtt.getR() / 3.0, 1 - edgeAtt.getG() / 3.0, 1 - edgeAtt.getB() / 3.0);
            drawSegment(gl, srcPos, tgtPos.negative(), crawlType, lambda);
        }
    }

    private boolean isSpecialFixedPt(ProjectiveUnitQuaternion elt) {
        int[] coxeterOrbitClass = elt.getQuat().getCoxeterOrbitClass();
        return coxeterOrbitClass[0] == 0 && (coxeterOrbitClass[1] == 2 || coxeterOrbitClass[1] == 3);
    }

    private void drawEdges(GL gl) {
        for (ProjectiveUnitQuaternion s : lpsGraph.getVertices()) {
            CartesianPoint3D src = lpsDrawing.getEmbedding().getPosition(s);
            Set<ProjectiveUnitQuaternion> nonDecrNeighbors = new LinkedHashSet<ProjectiveUnitQuaternion>(lpsGraph.getNeighborsInSameShell(s));
            nonDecrNeighbors.addAll(lpsGraph.getNeighborsInNextShell(s));
            for (ProjectiveUnitQuaternion t : nonDecrNeighbors) {
                CartesianPoint3D tgt = lpsDrawing.getEmbedding().getPosition(t);
                EdgeDrawingAtts edgeAtt = lpsDrawing.getEdgeDrawingAtts().getDrawingAtts(new DirectedEdge<ProjectiveUnitQuaternion>(s, t));
                gl.glColor3d(edgeAtt.getR(), edgeAtt.getG(), edgeAtt.getB());
                gl.glLineWidth(1.0f);
                drawSegment(gl, src, tgt, 0, 0);

                // Draw the Shadow Edges associated with points at infinity.
                if (showShadows) {
                    drawShadowEdges(gl, new DirectedEdge<ProjectiveUnitQuaternion>(s, t), 0, 0);
                }
            }
        }
    }

    private void drawSegment(GL gl, CartesianPoint3D src, CartesianPoint3D tgt, int type, double lambda) {
        if (type == 1) {
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3d(src.getX(), src.getY(), src.getZ());
            gl.glVertex3d(src.getX() + lambda * (tgt.getX() - src.getX()), src.getY() + lambda * (tgt.getY() - src.getY()), src.getZ() + lambda * (tgt.getZ() - src.getZ()));
            gl.glEnd();
        } else if (type == -1) {
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3d(src.getX() + lambda * (tgt.getX() - src.getX()), src.getY() + lambda * (tgt.getY() - src.getY()), src.getZ() + lambda * (tgt.getZ() - src.getZ()));
            gl.glVertex3d(tgt.getX(), tgt.getY(), tgt.getZ());
            gl.glEnd();
        } else {
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3d(src.getX(), src.getY(), src.getZ());
            gl.glVertex3d(tgt.getX(), tgt.getY(), tgt.getZ());
            gl.glEnd();
        }
    }
    
}
