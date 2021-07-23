import javax.swing.*;
import java.awt.*;

public class RoundButton extends JButton {
    public RoundButton(){
        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);
        setBackground(Color.white);
        setContentAreaFilled(false);

        // Change background color to a light gray when user hovers over button
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(Color.decode("#e6ebf0"));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(Color.white);
            }
        });
    }

    /*
     * Adjust color of rounded rectangle when button is pressed
     */
    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(Color.decode("#e6ebf0"));
        } else {
            g.setColor(getBackground());
        }
        g.fillRoundRect(3, 3, getSize().width - 9, getSize().height - 9, 10, 10);
        super.paintComponent(g);
    }

    /*
     * Paint rounded border instead of using RoundedBorder so that the shape can be tailored easier
     */
    protected void paintBorder(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.darkGray);
        g.drawRoundRect(3, 3, getSize().width - 9, getSize().height - 9, 10, 10);
    }

}
