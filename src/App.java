import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import java.util.Scanner;

public class App extends PApplet {

    int temp;
    int windspeed;
    int cloudiness;
    int timeinterval = 0;
    int lasttimecheck;
    JSONArray jsonArray;
    String url;
    int index;
    String lat;
    String lng;
    PImage byskilt;
    PImage termometer;
    PImage background;
    String cityname;
    Boolean foundCity = false;
    float bluecol, redcol, temprange;
    String summary;
    PFont font;
    String shortsummary;

    public static void main(String[] args) {
        PApplet.main("App");

    }

    public void settings() {
        size(800, 600);
    }

    public void setup() {
        frameRate(60);
        cityselect();
        byskilt = loadImage("byskilt.png");
        termometer = loadImage("thermometer.png");
        background = loadImage("background.jpg");
        font = createFont("Roboto-Bold.ttf", 50);
        background.resize(800, 600);
        lasttimecheck = millis();
    }

    public void draw() {
        background(background);
        textFont(font);
        imageMode(CORNER);
        image(byskilt, 20, 20, 333, 150);
        textAlign(CENTER);
        textSize(40);
        fill(0);
        text(cityname, 333/2+20, (150/2+20)*0.9f);
        imageMode(CENTER);
        image(termometer, width-100, (height / 2), 400, 500);
        textSize(20);
        text("Temperatur: " + temp + " grader", width-250,height/2);
        bluecol = map(temp,-15,45,225,0);
        redcol = map(temp, -15,45,0,255);
        fill(redcol,0,bluecol);
        noStroke();
        temprange = map(temp,-15,45,0,325);
        rect(width-112, 450, 25, -temprange);
        circle(width-100, 455,65);
        fill(0);
        textSize(30);
        text("Vejret i dag: " + summary, 333/2+20,200);

        if (millis() > lasttimecheck + timeinterval) {
            timeinterval = 6000;
            lasttimecheck = millis();
            System.out.println("Kort beskrivelse af vejret i dag: " + getshortsum());
            System.out.println("Beskrivelse af vejret i dag: " + getsummary());
            System.out.println("Temperatur: " + gettemp() + " grader " + "| Vindhastighed: " + getwindspeed() + " m/s"
                    + " | Skyprocent: " + getcloudiness() + "%");
        }
    }

    public int gettemp() {
        try {
            JSONObject jsonData = loadJSONObject(url);
            int tempread = jsonData.getJSONObject("main").getInt("temp");
            this.temp = tempread;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return temp;
    }

        public String getsummary() {
        try {
            JSONObject jsonData = loadJSONObject(url);
            JSONArray weatherArray = jsonData.getJSONArray("weather");
            String summaryread = weatherArray.getJSONObject(0).getString("description");
            this.summary = summaryread;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return summary;
    }

    public String getshortsum() {
        try {
            JSONObject jsonData = loadJSONObject(url);
            JSONArray weatherArray = jsonData.getJSONArray("weather");
            String ssummaryread = weatherArray.getJSONObject(0).getString("main");
            this.shortsummary = ssummaryread;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return shortsummary;
    }

    public int getwindspeed() {
        try {
            JSONObject jsonData = loadJSONObject(url);
            int windspeedread = jsonData.getJSONObject("wind").getInt("speed");
            this.windspeed = windspeedread;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return windspeed;
    }

    public int getcloudiness() {
        try {
            JSONObject jsonData = loadJSONObject(url);
            int cloudinessread = jsonData.getJSONObject("clouds").getInt("all");
            this.cloudiness = cloudinessread;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cloudiness;
    }

    public void cityselect() {
        try {
            jsonArray = loadJSONArray("dk.json");
            while (foundCity != true)
            {
                Scanner cityinput = new Scanner(System.in);
                System.out.print("Enter a city name: ");
                cityname = cityinput.nextLine();

                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject cityObject = jsonArray.getJSONObject(i);
                    if (cityObject.getString("city").equalsIgnoreCase(cityname)) {
                        index = i;
                        foundCity = true;
                    }
                }
            }

            JSONObject city = jsonArray.getJSONObject(index);
            lat = city.getString("lat");
            lng = city.getString("lng");

            System.out.println(lat + ":" + lng);

            url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&lang=%s&appid=89a2601e9cd2d8c8c5f113cba2ba204e",
             lat, lng,"da");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
