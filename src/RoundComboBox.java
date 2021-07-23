import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class RoundComboBox extends JComboBox {
    public RoundComboBox(String[] inputList){
        super(inputList);
        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);
        setOpaque(false);

        // Replaces a default blue, square border placed around the text with a rounded white border
        setUI(new BasicComboBoxUI());
        Object o = getAccessibleContext().getAccessibleChild(0);
        if (o instanceof JComponent) {
            JComponent c = (JComponent) o;
            c.setBorder(new RoundedBorder());
            c.setForeground(Color.darkGray);
            c.setBackground(Color.white);
        }

        // Used to set color of scrollbar as I was unable to adjust the color using the UIManager
        addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                Object popup = comboBox.getUI().getAccessibleChild(comboBox, 0);
                Component c = ((Container) popup).getComponent(0);
                if (c instanceof JScrollPane)
                {
                    JScrollPane scrollpane = (JScrollPane) c;
                    JScrollBar scrollBar = scrollpane.getVerticalScrollBar();
                    scrollBar.setUI(new BasicScrollBarUI(){
                        @Override
                        protected void configureScrollBarColors() {
                            this.thumbColor = Color.lightGray;
                        }
                    });
                }
            }
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });

    }

    protected void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRoundRect(0, 0, getSize().width - 1, getSize().height - 1, 10, 10);
        super.paintComponent(g);
    }
}
