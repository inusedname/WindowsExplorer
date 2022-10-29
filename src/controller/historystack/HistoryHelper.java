package controller.historystack;

import java.util.ArrayList;
import java.util.List;

public class HistoryHelper {
    private int currentHistoryIndex = -1;
    private final List<HistoryNode> historyStack = new ArrayList<>();

    public void pushBackHistory(HistoryNode node) {
        if (historyStack.size() - 1 != currentHistoryIndex) {
            historyStack.remove(historyStack.size() - 1);
        }
        historyStack.add(node);
        currentHistoryIndex++;
    }

    public void pushBackHistory(String path) {
        pushBackHistory(new HistoryNode(path));
    }

    public HistoryNode getPreviousHistory() {
        if (currentHistoryIndex > 0) {
            currentHistoryIndex--;
            return historyStack.get(currentHistoryIndex);
        }
        return null;
    }

    public HistoryNode getNextHistory() {
        if (currentHistoryIndex < historyStack.size() - 1) {
            currentHistoryIndex++;
            return historyStack.get(currentHistoryIndex);
        }
        return null;
    }

    public HistoryNode getCurrentHistory() {
        return historyStack.get(currentHistoryIndex);
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
