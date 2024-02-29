package tests;


import processing.core.PApplet;
import processing.data.JSONArray;

public class Screentest extends PApplet {

    JSONArray jsonarray;

    public static void main(String[] args) {
        PApplet.main("Screen");
    }

    public void settings() {
        size(800, 600);
    }

    public void setup() {
        loadcityjsonarray();
    }

    public void loadcityjsonarray() {
        jsonarray = loadJSONArray("worldcities.json");
    }

    public JSONArray getjsonArray() {
        return jsonarray;
    }
}
