package ws.skif.graph.models;

import java.util.LinkedList;

public class RouteList {

    private LinkedList<Route> routes;

    public LinkedList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(LinkedList<Route> routes) {
        this.routes = routes;
    }

    public RouteList(LinkedList<Route> routes) {
        this.routes = routes;
    }

    public RouteList() {
        routes = new LinkedList<>();
    }
}
