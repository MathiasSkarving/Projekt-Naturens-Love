package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import org.junit.Test;

import classes.*;

public class test extends PApplet{

    String dataurl;
    JSONObject jsondata;
    JSONArray jsonarray;

    @Before
    public void setup() {
        Screentest screen = new Screentest();
        PApplet.runSketch(new String[] { "Screen" }, screen);
        jsonarray = screen.getjsonArray();
    }

    @Test
    public void testvalidtemp(){
        final Cityselector city = new Cityselector("Odense");
        city.cityselect(jsonarray);
        dataurl = city.getUrl();
        jsondata = city.getjJsonObject(dataurl);
        final Datagatherer data = new Datagatherer();
        data.getJSONdata(jsondata);
        assertTrue(data.gettemp() >= -100);
    }

    @Test
    public void testnonvalidtemp(){
        final Cityselector city = new Cityselector("Odense");
        city.cityselect(jsonarray);
        dataurl = city.getUrl();
        jsondata = city.getjJsonObject(dataurl);
        final Datagatherer data = new Datagatherer();
        data.getJSONdata(jsondata);
        assertFalse(data.gettemp() < -100 || data.gettemp() > 100 );
    }

    @Test
    public void testfoundcity(){
        final Cityselector city = new Cityselector("Odense");
        city.cityselect(jsonarray);
        assertTrue(city.getFoundCity() == true);
    }

    @Test
    public void testfalsefoundcity(){
        final Cityselector city = new Cityselector("fnsdjfhndsjoju");
        city.cityselect(jsonarray);
        assertTrue(city.getFoundCity() == false);
    }
}
