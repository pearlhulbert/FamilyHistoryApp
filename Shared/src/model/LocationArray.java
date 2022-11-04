package model;

public class LocationArray {
    private Location[] data;

    public LocationArray(Location[] data) {
        this.data = data;
    }

    public int getLength() {
        return data.length;
    }

    public Location at(int index) {
        return data[index];
    }
}
