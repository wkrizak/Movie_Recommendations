import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/*
 * RoundedBorder class used by comboboxes to round corners
 */
public class RoundedBorder extends AbstractBorder {
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Adjusted width to prevent overlap when objects are side by side
        int r = 12;
        int w = width  - 5;
        int h = height - 1;
        Area round = new Area(new RoundRectangle2D.Float(x, y, w, h, r, r));
        Container parent = c.getParent();

        // Removes a portion of the rectangle on the right of the border and paints with background color
        if(parent!=null) {
            g2.setColor(parent.getBackground());
            Area corner = new Area(new Rectangle2D.Float(x, y, width, height));
            corner.subtract(round);
            g2.fill(corner);
        }

        // Paints background of popup
        if (c instanceof JPopupMenu){
            g2.setPaint(c.getBackground());
            g2.fill(round);
        }
        g2.setPaint(c.getForeground());
        g2.draw(round);
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(4, 8, 4, 8);
    }
    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.right = 8;
        insets.top = insets.bottom = 4;
        return insets;
    }
}
