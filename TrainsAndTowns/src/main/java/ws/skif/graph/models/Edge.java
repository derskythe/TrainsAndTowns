package ws.skif.graph.models;

/**
 * Store edge info
 */
public class Edge {
    double weight;
    int to;

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public Edge(int to, double info) {
        this.to = to;
        this.weight = info;
    }

    public double weight() {
        return weight;
    }

    public int to() {
        return to;
    }

    public void addWeight(double delta) {
        weight += delta;
    }
}
