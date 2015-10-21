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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author pdokos
 */
public class AboutDialog {

    private AboutDialog() {
    }

    public static void showAboutDialog() {
        final CardLayout cardLayout = new MyCardLayout();
        final JPanel cardHolder = new JPanel(cardLayout);
        final JFrame frame = new JFrame("About LPS Visualizer");

        JEditorPane[] editorPanels = {new JEditorPane(), new JEditorPane()};

        editorPanels[0].setEditorKit(new HTMLEditorKit());
        editorPanels[1].setEditorKit(new HTMLEditorKit());
        editorPanels[0].setText(AboutDialog.getDescr());
        editorPanels[1].setText(AboutDialog.getMoreDescr());

        JScrollPane[] editorScrollPanes = {new JScrollPane(editorPanels[0]), new JScrollPane(editorPanels[1])};

        Font font = new Font("Segoe UI", Font.PLAIN, 13);
        String bodyRule = "body { font-family: " + font.getFamily() + "; " + "font-size: " + font.getSize() + "pt; }";

        for (int i = 0; i < editorPanels.length; i++) {
            editorPanels[i].setCaretPosition(0);
            editorScrollPanes[i].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            editorScrollPanes[i].setPreferredSize(new Dimension(748, 640));
            editorScrollPanes[i].setMinimumSize(new Dimension(10, 10));
            ((HTMLDocument) editorPanels[i].getDocument()).getStyleSheet().addRule(bodyRule);
            editorPanels[i].setEditable(false);
            editorPanels[i].setFocusable(false);
            int padding = 40;
            editorPanels[i].setBorder(BorderFactory.createEmptyBorder(padding - 20, padding, padding - 5, padding));
            cardHolder.add(editorScrollPanes[i], String.valueOf(i));
        }

        int padding = 6;
        cardHolder.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

        final String[] names = {"About LPS Visualizer", "About the fixed point symmetry and the 3D embedding"};
        final String[] buttonNames = {"More", "Back"};

        final JButton nextButton = new JButton(buttonNames[0]);
        nextButton.addActionListener(new ActionListener() {
            private int nameIndex = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.next(cardHolder);
                nameIndex = (nameIndex + 1) % names.length;
                nextButton.setText(buttonNames[nameIndex]);
                frame.setTitle(names[nameIndex]);
                frame.pack();
                frame.setLocationRelativeTo(null);
            }
        });

        final JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });

        JPanel btnHolder = new JPanel();

        btnHolder.add(closeButton);
        btnHolder.add(nextButton);

        frame.add(cardHolder, BorderLayout.CENTER);
        frame.add(btnHolder, BorderLayout.SOUTH);
        frame.getRootPane().setDefaultButton(nextButton);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public static class MyCardLayout extends CardLayout {

        @Override
        public Dimension preferredLayoutSize(Container parent) {

            Component current = findCurrentComponent(parent);
            if (current != null) {
                Insets insets = parent.getInsets();
                Dimension pref = current.getPreferredSize();
                pref.width += insets.left + insets.right;
                pref.height += insets.top + insets.bottom;
                return pref;
            }
            return super.preferredLayoutSize(parent);
        }

        public Component findCurrentComponent(Container parent) {
            for (Component comp : parent.getComponents()) {
                if (comp.isVisible()) {
                    return comp;
                }
            }
            return null;
        }
    }

    private static String getDescr() {
        return "<p>This application generates animated 'drawings' of LPS Ramanujan "
                + "graphs that make their <i>octahedral</i> fixed-"
                + "point symmetry apparent.  The LPS graphs are an infinite "
                + "family of graphs, parameterized by pairs of "
                + "unequal odd primes p and q, that are best known for their "
                + "<i>optimal expansion</i> properties.  The values of "
                + "p and q, which can be changed using the arrow keys, determine "
                + "the edge density of the graph (each vertex "
                + "has exactly p+1 neighbors) and the size of the vertex set "
                + "(either q<sup>3</sup>-q or (q<sup>3</sup>-q)/2), respectively.  In this "
                + "application, a bound of 32 is set for the possible "
                + "values for p and q.  The largest graph LPS(29, 31) has "
                + "29,760 vertices and 446,400 edges, which requires about 100MB "
                + "of memory to model and animate.  Because of their size, it is "
                + "impossible (except for the very smallest instances) to "
                + "draw entire LPS graphs in a way that is visually "
                + "intelligible - press 'E' on the keyboard to see a "
                + "complete drawing of the graph with all of its edges "
                + "displayed (*).  We therefore settle for showing only small "
                + "edge subsets incrementally over time in some "
                + "meaningful way.</p> "
                + "<p>The animation displays the orbit (symmetric images), "
                + "under the octahedral group action, of a continuously "
                + "depicted random walk on the graph.  For any "
                + "directed edge along the walk, if there is more than "
                + "one neighbor of the source which is in the orbit of "
                + "the target, then all of the edges joining the two "
                + "orbits are shown.  By default, the full vertex set of "
                + "the graph is initially displayed.  Since this obstructs "
                + "the edge animation, the option to show/hide the vertex "
                + "set is provided by pressing 'V' on the keyboard.  "
                + "There are two variations of the vertex layout (**), "
                + "which can be toggled either by pressing 'L' on the keyboard, "
                + "or by selection from the 'Layout' menu.  "
                + "Other than this, the vertex positions remain unchanged "
                + "throughout the animation.  </p>"
                + ""
                + "<p> The final main feature of this application is the option "
                + "to view the edge animation from four fixed perspectives. "
                + "Three of these are along representative axes for "
                + "the three types of rotational cyclic subgroups of the "
                + "cube/octahedral group, i.e. a line through a pair of "
                + "opposite vertices, and lines through the centers of two "
                + "opposite edges and of two opposite faces.  The fourth "
                + "is one along a plane perpendicular to an axis through "
                + "two opposite vertices of the cube (or faces of the "
                + "octahedron).  Select from the 'View' menu to switch views "
                + "or press 'S' on the keyboard.  While in 'Multi-Perspective' "
                + "mode you can click on any panel to maximize/"
                + "minimize it inside the window.  </p>"
                + ""
                + "<p> (*) Click on 'Keyboard Actions' under the 'Help' menu for a "
                + "summary of all of the interactive features.  </p>"
                + "<p>(**) Press 'More' for a description of the layouts, and why the "
                + "cube/octahedral rotational symmetries "
                + "correspond to the graph symmetries that fix the root vertex.</p>";


    }

    private static String getMoreDescr() {
        return "<p>Both the 3D embedding of the vertex set, as well as the octahedral fixed point "
                + "symmetry of the the graph, are based upon the quaternion aspect of the LPS "
                + "construction; the definitions on which this implementation are based can be found "
                + "in [DSV].  The vertices of the graph correspond to quaternions over the finite field "
                + "&#x1D53D;<sub>q</sub> of nonzero norm, modulo scalar multiplication, i.e. to elements of "
                + "the group of 'projective "
                + "unit quaternions' (*).  We can think of this as a subset of projective three-space "
                + "over &#x1D53D;<sub>q</sub>, which decomposes as the union of the affine three-space of quaternions "
                + "with 'real' part 1, together with the projective plane (the "
                + "'points at infinity') associated with the purely imaginary quaternions.</p>"
                + "<p>The graph itself is a Cayley graph for this group with respect to a very special "
                + "generating set S of size p+1.  This means that the edge relations are defined by "
                + "the quaternion multiplication by the elements of S at each vertex.  For graphs "
                + "of this kind, the symmetries that fix the vertex corresponding to the identity "
                + "element (the 'root' vertex) can be characterized as those permutations of the "
                + "vertex set that both respect the multiplication operation (group automorphisms), "
                + "and preserve the set S.  In the LPS context, these are defined by the conjugation "
                + "action of an octahedral group that sits inside of the projective unit group ([D]).</p>"
                //+ "[which seems to have gone unnoticed until now :-].</p>"
                + "<p>This action is precisely the one on projective three-space which is induced by "
                + "the sum of familiar representation of the octahedral group on the three-space "
                + "of 'purely imaginary' quaternions with the trivial representation on the 'real' part.  "
                + "This lends itself to a natural embedding of the graph into &#8477;<sup>3</sup>, in which the "
                + "classical rotations of the cube/octahedron <i>-are-</i> the graph symmetries fixing the "
                + "root (**).  The embedding uses the imaginary components of the quaternion "
                + "representatives for the coordinates, with the elements of the field &#x1D53D;<sub>q</sub> represented "
                + "by integers of absolute value less than or equal to (q-1)/2.  The embedding "
                + "of the affine component is then straightforward - in particular, the 'root' vertex "
                + "corresponding to the multiplicative identity of the quaternions is positioned "
                + "at the origin.  For the embedding of the projective plane, a little more care is "
                + "taken to do so in such a way that the induced octahedral action on the projective "
                + "plane corresponds to the classical action on &#8477;<sup>3</sup>.  This is done by choosing a  "
                + "system of representatives, under the octahedral group action on the projective "
                + "plane, which lies within the affine plane x=1.  The orbits of these points as "
                + "quaternions are used as the representatives by which the embedding is defined.  "
                + "Thus, the points at infinity are distributed among the six affine planes defined "
                + "by setting one of the components equal to 1 or -1.  These planes are then "
                + "pushed out one unit beyond the outermost faces of the cubic grid that is formed "
                + "by the embedded affine component - press 'I' to show/hide the set of points "
                + "at infinity (try this with p=3 and q=31; press 'X' to switch between two choices "
                + "of fundamental representatives for the special vertices).</p>"
                + "<p>The only problem is with those points at infinity that have non-trivial stabilizers "
                + "under the action of the octahedral group.  These 'special' vertices, which are "
                + "the points at infinity colored green or yellow,  need to be represented as pairs "
                + "of antipodal points.  For each such pair, one of the points is designated as the "
                + "primary representative of the vertex and is colored as usual, while the other point "
                + "is designated as a 'shadow' and is colored gray.  Edges to the special vertices "
                + "are represented as 2 line segments (or 4  line segments, if the neighbor also "
                + "corresponds to a special vertex) joining the neighbor to the two points representing "
                + "the vertex.  The shadow edges are also given a complementary color - press 'H' "
                + "on the keyboard to show/hide the shadows while the walk is passing through a "
                + "special vertex, or while the full edge set is showing.  With the shadows hidden "
                + "you see a true depiction of the graph, but the correspondence between the "
                + "rotational symmetry and the graph symmetry does not quite hold for the special  "
                + "vertices and their incident edges.</p>"
                + "<p>"
                + "(*) Note that quaternion algebras over &#x1D53D;<sub>q</sub> are not division "
                + "algebras.  Rather, they are isomorphic to the two by two matrix algebra, "
                + "and the projective unit groups are isomorphic to the projective general linear "
                + "group." 
                + "</p>"
                + "<p>(**) The embedding described here goes by the name 'Grid' under the 'Layout' Menu.  "
                + "A variation, called the 'Radial' Layout, is included, in which the positions "
                + "of the vertices are scaled so that the metric distance from the root vertex "
                + "is equal to the path distance (which does not affect the correspondence between "
                + "rotational symmetry and graph symmetry).  Overlaps arising from this "
                + "are compensated for by nudging the orbits a bit off the spheres (these are most "
                + "apparent with the yellow vertices).  Press 'L' to toggle between the two layouts, "
                + "or select from the 'Layout' Menu.</p>"
                + "<p>"
                + "References: <br>"
                + "[D] P. Dokos, Automorphisms of LPS Graphs, 2014 (Unpublished). <br>"
                + "[DSV] G. Davidoff, P. Sarnak, and A. Valette, <i>Elementary Number Theory, Group Theory, and "
                + "Ramanujan Graphs</i>.  Cambridge University Press, 2003."
                + "</p>";
    }
}
