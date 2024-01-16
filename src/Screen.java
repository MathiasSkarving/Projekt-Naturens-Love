import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class Screen extends PApplet {
    int timeinterval = 0;
    int lasttimecheck;
    PImage byskilt;
    PImage termometer;
    PImage[] background;
    float bluecol, redcol, temprange;
    PFont font;
    int imageindex;
    int width = 800;
    int height = 600;
    String sunsetdate, sunrisedate;
    String pattern = "MM/dd/yyyy HH:mm:ss";
    DateFormat df = new SimpleDateFormat(pattern);
    Datagatherer myData = new Datagatherer();
    Cityselector myCity;
    private String dataurl;
    private String cityname;
    Scanner cityinput = new Scanner(System.in);

    public static void main(String[] args) {
        PApplet.main("Screen");
    }

    public void settings() {
        size(800, 600);
    }

    public void setup() {
        System.out.print("Skriv bynavn: ");
        cityname = cityinput.nextLine();
        // Lav nyt Cityselector objekt
        myCity = new Cityselector(cityname);
        // Finder byens data
        myCity.cityselect(loadJSONArray("worldcities.json"));
        myCity.setFoundCity(true);
        // Få url til bydata
        dataurl = myCity.getUrl();
        termometer = loadImage("thermometer.png");
        byskilt = loadImage("byskilt.png");
        background = new PImage[10];
        background[0] = loadImage("clearsky.jpg");
        background[1] = loadImage("fewclouds.jpg");
        background[2] = loadImage("scatteredclouds.jpg");
        background[3] = loadImage("brokenclouds.jpg");
        background[4] = loadImage("showerrain.jpg");
        background[5] = loadImage("thunderstorm.jpg");
        background[6] = loadImage("snow.jpg");
        background[7] = loadImage("mist.jpg");
        background[8] = loadImage("drizzle.jpg");
        background[9] = loadImage("overcastclouds.jpg");
        font = createFont("Roboto-Bold.ttf", 50);

        for (int i = 0; i < 10; i++) {
            background[i].resize(800, 600);
        }

        // Load vejrdata til dataklassen
        myData.getJSONdata(loadJSONObject(dataurl));
        getimageindex();
        timeinterval = 500;
        lasttimecheck = millis();
    }

    public void draw() {
        update();
    }

    public void restart() {
        // restart-funktion
        myCity.setFoundCity(false);
        setup();
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
        if (myData.getsummary().equals("clear sky")) {
            imageindex = 0;
        } else if (myData.getsummary().equals("few clouds")) {
            imageindex = 1;
        } else if (myData.getsummary().equals("scattered clouds")) {
            imageindex = 2;
        } else if (myData.getsummary().equals("broken clouds")) {
            imageindex = 3;
        } else if (myData.getshortsum().equals("Rain")) {
            imageindex = 4;
        } else if (myData.getshortsum().equals("Thunderstorm")) {
            imageindex = 5;
        } else if (myData.getshortsum().equals("Snow")) {
            imageindex = 6;
        } else if (myData.getsummary().equals("mist")) {
            imageindex = 7;
        } else if (myData.getsummary().equals("fog")) {
            imageindex = 7;
        } else if (myData.getsummary().equals("overcast clouds")) {
            imageindex = 9;
        } else if (myData.getshortsum().equals("Drizzle")) {
            imageindex = 8;
        } else {
            imageindex = 0;
        }
    }

    public void keyPressed() {
            if (key == 'r') {
                restart();
            }
        }  

    public void update() {
        background(background[imageindex]);

        if (millis() > lasttimecheck + timeinterval) {
            // Loop til vejrdata
            myData.getJSONdata(loadJSONObject(dataurl));
            getimageindex();
            timeinterval = 6000;
            lasttimecheck = millis();
            // Datoformatering
            Date sunrisetime = new java.util.Date((long) myData.getsunrise() * 1000);
            Date sunsettime = new java.util.Date((long) myData.getsunset() * 1000);
            sunsetdate = df.format(sunsettime);
            sunrisedate = df.format(sunrisetime);
        }

        // Layout
        textFont(font);
        imageMode(CORNER);
        noStroke();
        image(byskilt, 20, 20, 333, 150);
        textAlign(CENTER);
        textSize(40);
        fill(0);
        text(myCity.getCityName(), 333 / 2 + 20, (150 / 2 + 20) * 0.9f);
        imageMode(CENTER);
        image(termometer, width - 100, (height / 2), 400, 500);
        textSize(25);
        strokeText("Temperatur: " + myData.gettemp() + " grader", width - 270, height / 2);
        bluecol = map(myData.gettemp(), -45, 50, 225, 0);
        redcol = map(myData.gettemp(), -45, 50, 0, 255);
        fill(redcol, 0, bluecol);
        temprange = map(myData.gettemp(), -45, 50, 0, 325);
        rect(width - 112, 450, 25, -temprange);
        circle(width - 100, 455, 65);
        textSize(30);
        textAlign(CORNER);
        strokeText("Vejret i dag: " + translatesummary(), 25, 225);
        strokeText("Tid: " + hour() + "-" + minute() + "-" + second(), 25, 275);
        strokeText("Solopgang: " + sunrisedate, 25, 525);
        strokeText("Solnedgang: " + sunsetdate, 25, 575);
    }

    public String translatesummary() {
        // Oversættelse af vejrbeskrivelser
        if (myData.getsummary().equals("clear sky")) {
            return "Klar himmel";
        } else if (myData.getsummary().equals("few clouds")) {
            return "Få skyer på himlen";
        } else if (myData.getsummary().equals("scattered clouds")) {
            return "Spredte skyer på himlen";
        } else if (myData.getsummary().equals("broken clouds")) {
            return "Delvis skydække";
        } else if (myData.getshortsum().equals("Rain")) {
            return "Regnvejr";
        } else if (myData.getshortsum().equals("Thunderstorm")) {
            return "Tordenvejr";
        } else if (myData.getshortsum().equals("Snow")) {
            return "Snevejr";
        } else if (myData.getsummary().equals("mist")) {
            return "Let tåge";
        } else if (myData.getsummary().equals("fog")) {
            return "Tåget";
        } else if (myData.getsummary().equals("overcast clouds")) {
            return "Komplet skydække";
        } else if (myData.getshortsum().equals("Drizzle")) {
            return "Let regn";
        } else {
            return "Kunnne ikke finde passende vejrbeskrivelse";
        }
    }
}
