package ch.so.agi.stac.model;

public class Bbox {
    private double west;
    private double south;
    private double east;
    private double north;
    
    public double getWest() {
        return west;
    }
    public void setWest(double west) {
        this.west = west;
    }
    public double getSouth() {
        return south;
    }
    public void setSouth(double south) {
        this.south = south;
    }
    public double getEast() {
        return east;
    }
    public void setEast(double east) {
        this.east = east;
    }
    public double getNorth() {
        return north;
    }
    public void setNorth(double north) {
        this.north = north;
    }
    
    // fluent api
    
    public Bbox west(double west) {
        this.west = west;
        return this;
    }
    
    public Bbox south(double south) {
        this.south = south;
        return this;
    }

    public Bbox east(double east) {
        this.east = east;
        return this;
    }

    public Bbox north(double north) {
        this.north = north;
        return this;
    }
}
