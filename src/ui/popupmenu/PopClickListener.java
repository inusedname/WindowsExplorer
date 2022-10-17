package ui.popupmenu;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

abstract class PopClickListener extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger())
            doPop(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger())
            doPop(e);
    }

    abstract void doPop(MouseEvent e);
}
