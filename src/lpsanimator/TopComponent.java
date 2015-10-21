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
package lpsanimator;

import animation.MainAnimation;
import animation.MultiviewRendering;
import animationmodel.LPSOrbitCrawler;
import animationmodel.OrbitCrawlerController;
import com.sun.opengl.util.Animator;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

/**
 *
 * @author pdokos
 */
public class TopComponent extends JFrame {
    
    private List<Integer> primes = Arrays.asList(3, 5, 7, 11, 13, 17, 19, 23, 29, 31);
    private int i;
    private int j;

    //GL Animator
    private Animator animator;
    
    //Animation Model.
    private LPSOrbitCrawler ad;
    
    private MainAnimation mainAnimation; //Main View.
    private MultiviewRendering multivewRendering; 
    
    private OrbitCrawlerController orbCrawlKeyListener;
    
    private boolean multiviewMode;
    private boolean radialMode;
    private JMenuBar menuBar;
    private JMenu layoutMenu;
    private JMenu viewMenu;
    private JMenu helpMenu;
    private JRadioButtonMenuItem layoutMenuItemGrid;
    private JRadioButtonMenuItem layoutMenuItemRadial;
    private JRadioButtonMenuItem viewMenuItemMoving;
    private JRadioButtonMenuItem viewMenuItemStill;
    private JMenuItem helpMenuItemKeyComms;
    private JMenuItem helpMenuItemDescr;

    
    public TopComponent() {
        
        int p = 11;
        short q = 7;

        setTitle("LPS(" + p + "," + q + ")");
        createMenuBar();
        setBackground(Color.black);

        animator = new Animator();
        
        ad = new LPSOrbitCrawler(p, q, 50, false, true);
        i=primes.indexOf(p);
        j=primes.indexOf((int) q);
        ConsoleOutput.print(ad.getGraph(), p, (short) q);
        
        mainAnimation = new MainAnimation(this, ad);
        mainAnimation.attachToAnimator(animator);
        
        multivewRendering = new MultiviewRendering(this, ad);
        multivewRendering.attachToAnimator(animator);
        
        orbCrawlKeyListener = new OrbitCrawlerController(ad);

        multiviewMode = false;
        radialMode = false;

        this.addKeyListener(orbCrawlKeyListener);
        this.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent ke) {
            }

            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_S) {
                    switchViews();
                    if (multiviewMode) {
                        viewMenuItemStill.setSelected(true);
                    } else {
                        viewMenuItemMoving.setSelected(true);
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_L) {
                    switchLayout();
                    if (radialMode) {
                        layoutMenuItemRadial.setSelected(true);
                    } else {
                        layoutMenuItemGrid.setSelected(true);
                    }
                }
                
            }

        public void keyReleased(KeyEvent ke) {
            int keyCode = ke.getKeyCode();

            if ((!isMultipanelMode() && mainAnimation.isOn()) || (isMultipanelMode() && ad.isOn())) { //(cs.isOn() && ad.isOn()) || 
                boolean change = incrementPrimeIndex(keyCode);
                if (i == j) {
                    incrementPrimeIndex(keyCode);
                }
                if (change) {
                    int q = primes.get(j);
                    int p = primes.get(i);
                    setTitle("LPS(" + p + "," + q + ")");
                    System.out.println("=====================================");
                    ad = new LPSOrbitCrawler(p, (short) q, 50, ad.isRadialLayout(), ad.isSpVertConc());
                    ConsoleOutput.print(ad.getGraph(), p, (short) q);
                    resetOrbitCrawlers();
                    orbCrawlKeyListener.resetCrawler(ad);
                }
            }
        }

        private boolean incrementPrimeIndex(int keyCode) {

            switch (keyCode) {

                case KeyEvent.VK_UP:
                    j = (j + 1) % primes.size();
                    return true;
                case KeyEvent.VK_DOWN:
                    if (j == 0) {
                        j = primes.size() - 1;
                    } else {
                        j = (j - 1) % primes.size();
                    }
                    return true;
                case KeyEvent.VK_LEFT:
                    if (i == 0) {
                        i = primes.size() - 1;
                    } else {
                        i = (i - 1) % primes.size();
                    }
                    return true;
                case KeyEvent.VK_RIGHT:
                    i = (i + 1) % primes.size();
                    return true;
            }
            return false;
        }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {
                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });

    }

    @Override
    public void setVisible(boolean show) {
        if (!show) {
            animator.stop();
        }
        super.setVisible(show);
        if (show) {
            animator.start();
        }
    }
    
    public boolean isMultipanelMode() {
        return multiviewMode;
    }
    
    public void resetOrbitCrawlers() {
        mainAnimation.resetLPSOrbitCrawler(ad);
        multivewRendering.resetOrbitCrawlers(ad);
    }


    public void switchLayout() {
        ad.switchLayout();
        radialMode = !radialMode;
    }


    public void switchViews() {
        if (multiviewMode) {
            multivewRendering.hide();
            mainAnimation.show();
        } else {
            mainAnimation.hide();
            multivewRendering.show();
        }
        multiviewMode = !multiviewMode;
        pack();
    }

    private void createMenuBar() {
        menuBar = new JMenuBar();

        layoutMenu = new JMenu("Layout");
        menuBar.add(layoutMenu);

        layoutMenuItemGrid = new JRadioButtonMenuItem("Grid", true);
        layoutMenuItemRadial = new JRadioButtonMenuItem("Radial");

        ActionListener layoutMenuItemLister = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if ((ae.getActionCommand().equals("GRID") && radialMode) || (ae.getActionCommand().equals("RADIAL") && !radialMode)) {
                    switchLayout();
                }
            }
        };

        layoutMenuItemGrid.setActionCommand("GRID");
        layoutMenuItemGrid.addActionListener(layoutMenuItemLister);

        layoutMenuItemRadial.setActionCommand("RADIAL");
        layoutMenuItemRadial.addActionListener(layoutMenuItemLister);

        ButtonGroup layoutGroup = new ButtonGroup();
        layoutGroup.add(layoutMenuItemGrid);
        layoutGroup.add(layoutMenuItemRadial);

        layoutMenu.add(layoutMenuItemGrid);
        layoutMenu.add(layoutMenuItemRadial);

        viewMenu = new JMenu("View");
        menuBar.add(viewMenu);
        viewMenuItemMoving = new JRadioButtonMenuItem("Main View", true);
        viewMenuItemStill = new JRadioButtonMenuItem("Multi-Perspective");

        ActionListener viewMenuItemLister = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if ((ae.getActionCommand().equals("MAIN") && multiviewMode) || (ae.getActionCommand().equals("MULTI") && !multiviewMode)) {
                    switchViews();
                }
            }
        };

        viewMenuItemMoving.setActionCommand("MAIN");
        viewMenuItemMoving.addActionListener(viewMenuItemLister);

        viewMenuItemStill.setActionCommand("MULTI");
        viewMenuItemStill.addActionListener(viewMenuItemLister);

        ButtonGroup viewGroup = new ButtonGroup();
        viewGroup.add(viewMenuItemMoving);
        viewGroup.add(viewMenuItemStill);

        viewMenu.add(viewMenuItemMoving);
        viewMenu.add(viewMenuItemStill);
        
        helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        helpMenuItemKeyComms = new JMenuItem("Keyboard Actions");
        helpMenuItemKeyComms.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KeyCommandsDialog.showKeyCommandsDialog();
            }
        });
        helpMenuItemDescr = new JMenuItem("About LPS Visualizer");
        helpMenuItemDescr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog.showAboutDialog();
            }
        });
        helpMenu.add(helpMenuItemDescr);
        helpMenu.add(helpMenuItemKeyComms);

        setJMenuBar(menuBar);
    }

}
