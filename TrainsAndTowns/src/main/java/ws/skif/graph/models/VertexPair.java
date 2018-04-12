package ws.skif.graph.models;

public class VertexPair {
    private int from;
    private int to;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public VertexPair(int from, int to) {
        this.from = from;
        this.to = to;
    }
}
