package model;

public class NamesArray {
    String[] data;

    public NamesArray(String[] data) {
        this.data = data;
    }

    public int getLength() {
        return data.length;
    }

    public String at(int index) {
        return data[index];
    }
}
