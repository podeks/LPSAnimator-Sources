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
import camerasequencers.SyncedPivotController;
import com.sun.opengl.util.Animator;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import lpsanimator.TopComponent;

/**
 *
 * @author pdokos
 */
public class MultiviewRendering {

    private LPSOrbitCrawler crawler;
    
    private TopComponent parentFrame;
    private List<GLCanvas> panels;
    private List<PanelMouseListener> panelMouseListeners;
    private List<LPSPivotRenderer> panelRenderers;
    private SyncedPivotController pivotController;
    private KeyListener miscControls;

    public MultiviewRendering(TopComponent topComp, LPSOrbitCrawler model) {

        parentFrame = topComp;
        crawler = model;

        panels = new ArrayList<GLCanvas>();
        for (int i = 0; i < 4; i++) {
            GLCanvas glCanvas = new GLCanvas(createGLCapabilites());
            glCanvas.setFocusable(false);
            panels.add(glCanvas);
        }

        panelRenderers = new ArrayList<LPSPivotRenderer>();
        panelRenderers.add(new LPSPivotRenderer(crawler, 0, 0, true));
        panelRenderers.add(new LPSPivotRenderer(crawler, Math.PI / 2.0, 1, false));
        panelRenderers.add(new LPSPivotRenderer(crawler, Math.acos(Math.sqrt(3) / 3), 1, false));
        panelRenderers.add(new LPSPivotRenderer(crawler, Math.PI / 2.0, 2, false));

        panelMouseListeners = new ArrayList<PanelMouseListener>();

        for (int i = 0; i < 4; i++) {
            LPSPivotRenderer renderer = panelRenderers.get(i);
            GLCanvas canvas = panels.get(i);
            PanelMouseListener panelMouseListener = new PanelMouseListener(i);
            panelMouseListeners.add(panelMouseListener);
            canvas.addMouseListener(panelMouseListener);
            canvas.addGLEventListener(renderer);
        }

        pivotController = new SyncedPivotController();
        for (LPSPivotRenderer renderer : panelRenderers) {
            renderer.addToSynchronizedKeyListener(pivotController);
        }
        
        miscControls = new KeyListener() {
            
            public void keyTyped(KeyEvent ke) {
            }

            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
                    crawler.flipSwitch();
                }
                if (!crawler.isOn() && ke.getKeyCode() == KeyEvent.VK_UP) {
                    pivotController.pivotStep(true);
                }
                if (!crawler.isOn() && ke.getKeyCode() == KeyEvent.VK_DOWN) {
                    pivotController.pivotStep(true);
                }
            }

            public void keyReleased(KeyEvent ke) {
            }
        };
    }

    public void attachToAnimator(Animator animator) {
        for (GLCanvas canvas : panels) {
            animator.add(canvas);
        }
    }

    public void resetOrbitCrawlers(LPSOrbitCrawler orbitCrawler) {
        crawler=orbitCrawler;
        for (LPSPivotRenderer renderer : panelRenderers) {
            renderer.resetLPSOrbitCrawler(orbitCrawler);
            renderer.initState();
        }
    }

    private void initializePanelRenderers() {
        for (LPSPivotRenderer renderer : panelRenderers) {
            renderer.initState();
        }
    }
    
    private void resetPanelMouseListeners() {
        for (PanelMouseListener listener : panelMouseListeners) {
            listener.reset();
        }
    }

    public void hide() {
        parentFrame.removeKeyListener(pivotController);
        parentFrame.removeKeyListener(miscControls);
        for (int i = 0; i < 4; i++) {
            parentFrame.remove(panels.get(i));
            panelRenderers.get(i).setAdvance(false);
        }
    }

    public void show() {
        resetPanelMouseListeners();
        initializePanelRenderers();
        Container contentPane = parentFrame.getContentPane();
        contentPane.setLayout(new GridLayout(2, 2));
        for (GLCanvas stPanel : panels) {
            stPanel.reshape(0, 0, contentPane.getWidth() / 2, contentPane.getHeight() / 2);
            parentFrame.add(stPanel);
        }
        panelRenderers.get(0).setAdvance(true);
        parentFrame.addKeyListener(pivotController);
        parentFrame.addKeyListener(miscControls);
    }

    private GLCapabilities createGLCapabilites() {

        GLCapabilities capabilities = new GLCapabilities();
        capabilities.setHardwareAccelerated(true);

        // try to enable 2x anti aliasing - should be supported on most hardware
        capabilities.setNumSamples(2);
        capabilities.setSampleBuffers(true);

        return capabilities;
    }
    
    private class PanelMouseListener implements MouseListener {

        int panelIndex;
        boolean fullMode;

        public PanelMouseListener(int panelIndex) {
            this.panelIndex = panelIndex;
            fullMode = false;
        }

        public void reset() {
            fullMode = false;
        }

        public void mouseClicked(MouseEvent me) {
            if (!fullMode) {
                for (int i = 0; i < 4; i++) {
                    if (i != panelIndex) {
                        parentFrame.remove(panels.get(i));
                    }
                }
                parentFrame.getContentPane().setLayout(new GridLayout(1, 1));
                panels.get(panelIndex).reshape(0, 0, parentFrame.getContentPane().getWidth(), parentFrame.getContentPane().getHeight());
                panelRenderers.get(panelIndex).setAdvance(true);
            } else {
                int height = parentFrame.getContentPane().getHeight();
                int width = parentFrame.getContentPane().getWidth();
                panelRenderers.get(panelIndex).setAdvance(false);
                parentFrame.remove(panels.get(panelIndex));
                syncRenderers();
                parentFrame.getContentPane().setLayout(new GridLayout(2, 2));
                for (GLCanvas stPanel : panels) {
                    stPanel.reshape(0, 0, width / 2, height / 2);
                    parentFrame.add(stPanel);
                }
                panelRenderers.get(0).setAdvance(true);
            }
            fullMode = !fullMode;
            parentFrame.pack();
        }

        public void mousePressed(MouseEvent me) {
        }

        public void mouseReleased(MouseEvent me) {
        }

        public void mouseEntered(MouseEvent me) {
        }

        public void mouseExited(MouseEvent me) {
        }

        private void syncRenderers() {
            for (int i = 0; i < 4; i++) {
                if (i != panelIndex) {
                    panelRenderers.get(panelIndex).syncRenderer(panelRenderers.get(i));
                }
            }
        }
    }
}