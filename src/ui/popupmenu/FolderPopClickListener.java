package ui.popupmenu;

import java.awt.event.MouseEvent;

public class FolderPopClickListener extends PopClickListener {
    @Override
    void doPop(MouseEvent e) {
        PopupMenu menu = new FolderPopupMenu();
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}
