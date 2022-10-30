package controller.historystack;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

// Observer pattern
public class CurrentHistoryIndex {
    private int currentHistoryIndex = -1;
    private final PropertyChangeSupport currentHistoryChangeSupport;

    public int getIndex() {
        return currentHistoryIndex;
    }
    public CurrentHistoryIndex() {
        currentHistoryChangeSupport = new PropertyChangeSupport(this);
    }

    public void addHistoryChangeListener(PropertyChangeListener listener) {
        currentHistoryChangeSupport.addPropertyChangeListener(listener);
    }

    public void removeHistoryChangeListener(PropertyChangeListener listener) {
        currentHistoryChangeSupport.removePropertyChangeListener(listener);
    }
    public void setIndex(int newHistoryIndex) {
        int oldHistoryIndex = currentHistoryIndex;
        currentHistoryIndex = newHistoryIndex;
        currentHistoryChangeSupport.firePropertyChange("currentHistory", oldHistoryIndex, newHistoryIndex);
    }
}
