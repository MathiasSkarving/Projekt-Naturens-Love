import java.util.Scanner;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class Cityselector extends PApplet {

    private Boolean foundCity = false;
    private String cityname;
    private int index;
    private String lat;
    private String lng;
    private String url;
    String country;

// Constructor med input
    public Cityselector(String citynameinput) {
        cityname = citynameinput;
    }

    public void setFoundCity(Boolean value) {
        foundCity = value;
    }

    public Boolean getFoundCity() {
        return foundCity;
    }

    public String getCityName() {
        return cityname;
    }

    public String getUrl() {
        return this.url;
    }

    public void cityselect(JSONArray jsonArray) {
        try {
            // Selection loop
            while (foundCity != true) {
                // Søger igennem JSON arrayet efter byen
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject cityObject = jsonArray.getJSONObject(i);
                    if (cityObject.getString("city").equalsIgnoreCase(cityname)) {
                        index = i;
                        foundCity = true;
                    }
                }
                if (foundCity != true) {
                    System.out.println("Byen findes ikke! Skriv et nyt bynavn!");
                    try (Scanner cityinput = new Scanner(System.in)) {
                        System.out.print("Enter a city name: ");
                        cityname = cityinput.nextLine();
                    }
                }
            }

            // Find byen fra index
            JSONObject city = jsonArray.getJSONObject(index);
            lat = city.getString("lat");
            lng = city.getString("lng");

            System.out.println(lat + ":" + lng);

            // Formatér dataurl
            this.url = String.format(
                    "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&appid=89a2601e9cd2d8c8c5f113cba2ba204e",
                    lat, lng);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cityselectfejl");
        }
    }

}
