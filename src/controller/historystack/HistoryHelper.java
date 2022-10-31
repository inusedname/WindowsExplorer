package controller.historystack;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HistoryHelper {

    private final CurrentHistoryIndex current = new CurrentHistoryIndex();
    private final List<HistoryNode> historyStack = new ArrayList<>();

    public int getHistoryStackSize() {
        return historyStack.size();
    }

    public void pushBackHistory(HistoryNode node) {
        if (!new File(node.getPath()).exists()) {
            return;
        }
        while (historyStack.size() - 1 != current.getIndex()) {
            historyStack.remove(historyStack.size() - 1);
        }
        historyStack.add(node);
        current.setIndex(current.getIndex() + 1);

//        for (HistoryNode historyNode : historyStack) {
//            System.out.printf("%s ", historyNode.getPath());
//        }
//        System.out.println();
    }

    public void pushBackHistory(String path) {
        pushBackHistory(new HistoryNode(path));
    }

    public HistoryNode goToPreviousHistory() {
        if (current.getIndex() > 0) {
            current.setIndex(current.getIndex() - 1);
            return historyStack.get(current.getIndex());
        }
        return null;
    }

    public HistoryNode goToNextHistory() {
        if (current.getIndex() < historyStack.size() - 1) {
            current.setIndex(current.getIndex() + 1);
            return historyStack.get(current.getIndex());
        }
        return null;
    }

    public HistoryNode getCurrentHistory() {
        return historyStack.get(current.getIndex());
    }

    public void addHistoryChangeListener(PropertyChangeListener listener) {
        current.addHistoryChangeListener(listener);
    }

    public static class HistoryNode {
        private final String path;

        HistoryNode(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
