package lpsanimator;

import java.awt.BorderLayout;
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
public class KeyCommandsDialog {

    private KeyCommandsDialog() {
    }    
    
    public static class MyBorderLayout extends BorderLayout {

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
    
    public static void showKeyCommandsDialog() {
        final JFrame frame = new JFrame("Keyboard Actions");
        final JPanel contentHolder = new JPanel(new MyBorderLayout());
        
        JEditorPane editorPanel = new JEditorPane();

        editorPanel.setEditorKit(new HTMLEditorKit());
        editorPanel.setText(KeyCommandsDialog.getKeyCommands());
        editorPanel.setPreferredSize(new Dimension(1036, 615));

        JScrollPane editorScrollPane = new JScrollPane(editorPanel);

        Font font = new Font("Segoe UI", Font.PLAIN, 13);
        String bodyRule = "body { font-family: " + font.getFamily() + "; " + "font-size: " + font.getSize() + "pt; }";

        editorPanel.setCaretPosition(0);
        editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        editorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        editorScrollPane.setPreferredSize(new Dimension(1036, 615));
        editorScrollPane.setMinimumSize(new Dimension(10, 10));
        ((HTMLDocument) editorPanel.getDocument()).getStyleSheet().addRule(bodyRule);
        editorPanel.setEditable(false);
        editorPanel.setFocusable(false);
        int padding = 20;
        editorPanel.setBorder(BorderFactory.createEmptyBorder(padding - 20, padding, padding - 5, padding));
        contentHolder.add(editorScrollPane);

        padding = 6;
        contentHolder.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

        final JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });

        JPanel btnHolder = new JPanel();
        btnHolder.add(closeButton);

        frame.add(contentHolder, BorderLayout.CENTER);
        frame.add(btnHolder, BorderLayout.SOUTH);
        frame.getRootPane().setDefaultButton(closeButton);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
    }
    
    private static String getKeyCommands() {
        return "<p style=\"font-size:14pt;\"><b>Changing the Graph</b></p>"
                + "<pre style=\"font-family:Segoe UI;\">  <kbd style=\"font-size:14pt;\">ARROWKEYS</kbd> : Change the graph LPS(p, q).  Note that this functionality is disabled when the animation is paused.  <br>"
                + "                     -Use the <i>horizontal</i> arrow keys to change the parameter p - this determines the degree of the graph to be p+1.  <br>"
                + "                     -Use the <i>vertical</i> arrow keys to change the parameter q - this determines the number of vertices of the graph to be either q<sup>3</sup>-q or (q<sup>3</sup>-q)/2.  <br>"
                + "</pre>"
                + "<hr>"
                + "<a style=\"font-size:14pt;\"><b>View and Layout</b></a>"
                + "<pre style=\"font-family:Segoe UI;\">  <kbd style=\"font-size:14pt;\">S</kbd> : Switch between 'Main' View and 'Multi-Perspective' View.  Click on a panel in Multi-Perspective mode to maximize/minimize inside the window. <br>"
                + "  <kbd style=\"font-size:14pt;\">L</kbd> : Switch between 'Grid' (vertex) Layout and 'Radial' Layout.<br>"
                + "</pre>"
                + "<hr>"
                + "<a style=\"font-size:14pt;\"><b>Overlays</b></a>"
                + "<pre style=\"font-family:Segoe UI;\">"
                + "  <kbd style=\"font-size:14pt;\">V</kbd> : show/hide the vertex set       <kbd style=\"font-size:14pt;\">C</kbd> : show/hide a cube  <br>"
                + "  <kbd style=\"font-size:14pt;\">E</kbd> : show/hide the edge set         <kbd style=\"font-size:14pt;\">O</kbd> : show/hide an octahedron<br>"
                + "</pre>"
                + "<hr>"
                + "<a style=\"font-size:14pt;\"><b>Animation</b></a>"
                + "<pre style=\"font-family:Segoe UI;\"> <b><i>Orbit Walker:</i></b> <br>"
                + "        <kbd style=\"font-size:14pt;\">W</kbd>  : pause/play the orbit walker        <kbd style=\"font-size:14pt;\">J</kbd> : jump to next orbit while the walker is paused<br>"
                + "   <kbd style=\"font-size:14pt;\"> -/=</kbd> : decelerate/accelerate the orbit walker while it is playing <br>"
                + "    <kbd style=\"font-size:14pt;\">[/]</kbd>  : reduce/increase delay between steps while the walker is playing"
                + "</pre>"
                + "<pre style=\"font-family:Segoe UI;\"><b><i>Camera:</i></b> <br>"
                + "                        <kbd style=\"font-size:14pt;\">P</kbd> : pause/play the camera sequencer (Main View);                      pause/play a 180 degree pivot (Multi-Perspective View)<br>"
                + "  <kbd style=\"font-size:14pt;\">COMMA/PERIOD</kbd> : decelerate/accelerate the camera sequencer (Main View);      pivot backward/forward one degree (Multi-Perspective View)"
                + "</pre>"
                + "<pre style=\"font-family:Segoe UI;\">"
                + "            <kbd style=\"font-size:14pt;\">SPACEBAR</kbd> : pause both the camera sequencer and the orbit walker if both are on / play both if either or both are off (Main View).<br>"
                + "                               pause/play the orbit walker (Multi-Perspective View)"
                + "</pre>"
                + "<pre style=\"font-family:Segoe UI;\">"
                + "         <kbd style=\"font-size:14pt;\">ARROWKEYS</kbd> : If the orbit walker is paused, or the camera sequencer (while in Main View) is paused, then the functionality of the<br>"
                + "                               arrow keys described above is disabled and replaced with the following.<br>"
                + "                             -Use the <i>horizontal</i> arrow keys to advance the walker, together with the camera (if in Main View).  <br>"
                + "                             -Use the <i>vertical</i> arrow keys to advance the camera."
                + "</pre>"
                + "<hr>"
                + "<a style=\"font-size:14pt;\"><b>Miscellaneous</b></a>"
                + "<pre style=\"font-family:Segoe UI;\">  <kbd style=\"font-size:14pt;\">H</kbd> : show/hide the 'shadow' edges and vertices associated to the 'special vertices' <br>"
                + "  <kbd style=\"font-size:14pt;\">I</kbd> : show/hide the 'points at infinity'  -  <kbd style=\"font-size:14pt;\">X</kbd> : switch between two alternative layouts for the 'special vertices'"
                + "</pre>"
                + "";
    }
    
}