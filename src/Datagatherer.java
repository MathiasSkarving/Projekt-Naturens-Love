import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class Datagatherer extends PApplet {
    private int temp;
    private int sunset, sunrise;
    private String summary;
    private String shortsummary;
    private JSONObject jsonData;

    public Datagatherer() {
    }

    // indlæs data
    public void getJSONdata(JSONObject jsonobjectin)
    {
        jsonData = jsonobjectin;
    }

    public int gettemp() {
        try {
            int tempread = jsonData.getJSONObject("main").getInt("temp");
            this.temp = tempread;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Kunne ikke hente temperaturdata");
        }

        return temp;
    }

    public int getsunrise() {
        try {
            int sunriseread = jsonData.getJSONObject("sys").getInt("sunrise");
            sunrise = sunriseread;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Kunne ikke hente solopgangsdata");
        }

        return sunrise;
    }

    public int getsunset() {
        try {
            int sunsetread = jsonData.getJSONObject("sys").getInt("sunset");
            sunset = sunsetread;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Kunne ikke hente solnedgangsdata");

        }

        return sunset;
    }

    public String getsummary() {
        try {
            JSONArray weatherArray = jsonData.getJSONArray("weather");
            String summaryread = weatherArray.getJSONObject(0).getString("description");
            this.summary = summaryread;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Kunne ikke hente summarydata");
        }

        return summary;
    }

    public String getshortsum() {
        try {
            JSONArray weatherArray = jsonData.getJSONArray("weather");
            String ssummaryread = weatherArray.getJSONObject(0).getString("main");
            this.shortsummary = ssummaryread;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Kunne ikke hente shortsummarydata");
        }

        return shortsummary;
    }
}
