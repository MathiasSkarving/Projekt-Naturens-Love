import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;

public class App extends PApplet {

    int temp;
    int sunset, sunrise;
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
    PImage[] background;
    String cityname;
    Boolean foundCity = false;
    float bluecol, redcol, temprange;
    String summary;
    PFont font;
    String shortsummary;
    int imageindex;
    int width = 800;
    int height = 600;
    String sunrisedate, sunsetdate;

    public static void main(String[] args) {
        PApplet.main("App");
    }

    public void settings() {
        size(800, 600);
    }

    public void setup() {
        frameRate(60);
        cityselect();
        termometer = loadImage("thermometer.png");
        byskilt = loadImage("byskilt.png");
        background = new PImage[8];
        background[0] = loadImage("clearsky.jpg");
        background[1] = loadImage("fewclouds.jpg");
        background[2] = loadImage("scatteredclouds.jpg");
        background[3] = loadImage("brokenclouds.jpg");
        background[4] = loadImage("showerrain.jpg");
        background[5] = loadImage("thunderstorm.jpg");
        background[6] = loadImage("snow.jpg");
        background[7] = loadImage("mist.jpg");

        font = createFont("Roboto-Bold.ttf", 50);

        for (int i = 0; i < 8; i++) {
            background[i].resize(800, 600);
        }
        
        getimageindex();
        lasttimecheck = millis();
    }

    public void draw() {
        background(background[imageindex]);

        if (millis() > lasttimecheck + timeinterval) {
            getimageindex();
            timeinterval = 6000;
            lasttimecheck = millis();
            System.out.println("Kort beskrivelse af vejret i dag: " + getshortsum());
            System.out.println("Beskrivelse af vejret i dag: " + getsummary());
            System.out.println("Temperatur: " + gettemp() + " grader " + "| Vindhastighed: " + getwindspeed() + " m/s"
                    + " | Skyprocent: " + getcloudiness() + "%");
            Date sunrisetime = new java.util.Date((long)getsunrise()*1000);
            Date sunsettime = new java.util.Date((long)getsunset()*1000);

            System.out.println(sunsettime);
            System.out.println(sunrisetime);
        }

        textFont(font);
        imageMode(CORNER);
        noStroke();
        image(byskilt, 20, 20, 333, 150);
        textAlign(CENTER);
        textSize(40);
        fill(0);
        text(cityname, 333 / 2 + 20, (150 / 2 + 20) * 0.9f);
        imageMode(CENTER);
        image(termometer, width - 100, (height / 2), 400, 500);
        textSize(25);
        strokeText("Temperatur: " + temp + " grader", width - 270, height / 2);
        bluecol = map(temp, -15, 45, 225, 0);
        redcol = map(temp, -15, 45, 0, 255);
        fill(redcol, 0, bluecol);
        temprange = map(temp, -15, 45, 0, 325);
        rect(width - 112, 450, 25, -temprange);
        circle(width - 100, 455, 65);
        textSize(30);
        textAlign(CORNER);
        strokeText("Vejret i dag: " + summary, 50, 200);
        strokeText("Tid: " + hour() + "-" + minute() + "-" + second(), 50, 0);
    }

    public void strokeText(String message, int x, int y) {
        fill(255);
        text(message, x - 1, y);
        text(message, x, y - 1);
        text(message, x + 1, y);
        text(message, x, y + 1);
        fill(0);
        text(message, x, y);
    }

    public void getimageindex() {
        if (getsummary().equals("clear sky")) {
            imageindex = 0;
        } else if (getsummary().equals("few clouds")) {
            imageindex = 1;
        } else if (getsummary().equals("scattered clouds")) {
            imageindex = 2;
        } else if (getsummary().equals("broken clouds")) {
            imageindex = 3;
        } else if (getshortsum().equals("Rain")) {
            imageindex = 4;
        } else if (getshortsum().equals("Thunderstorm")) {
            imageindex = 5;
        } else if (getshortsum().equals("Snow")) {
            imageindex = 6;
        } else if (getsummary().equals("mist")) {
            imageindex = 7;
        } else if (getsummary().equals("overcast clouds")) {
            imageindex = 3;
        } else {
            imageindex = 0;
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

    public int getsunrise() {
        try {
            JSONObject jsonData = loadJSONObject(url);
            int sunriseread = jsonData.getJSONObject("sys").getInt("sunrise");
            sunrise = sunriseread;

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return sunrise;
    }

    public int getsunset() {
        try {
            JSONObject jsonData = loadJSONObject(url);
            int sunsetread = jsonData.getJSONObject("sys").getInt("sunset");
            sunset = sunsetread;

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return sunset;
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
            while (foundCity != true) {
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

            url = String.format(
                    "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&appid=89a2601e9cd2d8c8c5f113cba2ba204e",
                    lat, lng);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
