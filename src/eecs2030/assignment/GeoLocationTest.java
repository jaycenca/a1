package eecs2030.assignment;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jay Cen on 5/29/2017.
 */
public class GeoLocationTest
{
    @Test
    public void test1_generate() throws Exception
    {
        GeoLocation geo1 = GeoLocation.generate(40, -5.7894);
        assertEquals("(+040.0000,-05.7894)", geo1.toString());

        GeoLocation geo2 = GeoLocation.generate(-90.687549, 0);
        assertEquals("(-090.6875,+00.0000)", geo2.toString());

        GeoLocation geo3 = GeoLocation.generate(-180, -80.54325);
        assertEquals("(-180.0000,-80.5433)", geo3.toString());

        GeoLocation geo4 = GeoLocation.generate(180, -83.45231532);
        assertEquals("(+180.0000,-83.4523)", geo4.toString());

        GeoLocation geo5 = GeoLocation.generate(180, -99);
        assertEquals("(+180.0000,-90.0000)", geo5.toString());

        GeoLocation geo6 = GeoLocation.generate(180, 99);
        assertEquals("(+180.0000,+90.0000)", geo6.toString());

        GeoLocation geo7 = GeoLocation.generate(190, 99);
        assertEquals("(-170.0000,+90.0000)", geo7.toString());

        GeoLocation geo8 = GeoLocation.generate(-200, -89.434235);
        assertEquals("(+160.0000,-89.4342)", geo8.toString());

        GeoLocation geo9 = GeoLocation.generate(-360, -90.01100);
        assertEquals("(+000.0000,-90.0000)", geo9.toString());
    }
    @Test
    public void test2_getGMTHourOffset() throws Exception
    {
        GeoLocation geo1 = GeoLocation.generate(-180, 0);
        assertEquals(-12, geo1.getGMTHourOffset());

        GeoLocation geo2 = GeoLocation.generate(-175, 0);
        assertEquals(-12, geo2.getGMTHourOffset());

        GeoLocation geo3 = GeoLocation.generate(-150, 0);
        assertEquals(-10, geo3.getGMTHourOffset());

        GeoLocation geo4 = GeoLocation.generate(-5, 0);
        assertEquals(-1, geo4.getGMTHourOffset());

        GeoLocation geo5 = GeoLocation.generate(5, 0);
        assertEquals(0, geo5.getGMTHourOffset());

        GeoLocation geo6 = GeoLocation.generate(80, 0);
        assertEquals(5, geo6.getGMTHourOffset());

        GeoLocation geo7 = GeoLocation.generate(81.5, 50);
        assertEquals(5, geo7.getGMTHourOffset());

        GeoLocation geo8 = GeoLocation.generate(175, 90);
        assertEquals(11, geo8.getGMTHourOffset());

        GeoLocation geo9 = GeoLocation.generate(180, 180);
        assertEquals(-12, geo9.getGMTHourOffset());
    }
    //following the receipts for equals methods 4 steps
    @Test
    public void test3_equals() throws Exception
    {
        GeoLocation geo1 = GeoLocation.generate(10, 10);
        GeoLocation geo2 = GeoLocation.generate(10, 100);

        //test case for an instance that is equal to itself
        boolean test1 = geo1.equals(geo1);
        assertTrue(test1);

        //test case for an instance that never equals to null
        boolean test2 = geo1.equals(null);
        assertFalse(test2);

        //test case for two difference instances interchangeably
        boolean test3 = geo1.equals(geo2);
        assertFalse(test3);

        boolean test4 = geo2.equals(geo1);
        assertFalse(test4);
    }

    @Test
    public void test4_compareTo() throws Exception
    {
        //test case when hour offset are not the same and hour offset1 > hour offset2
        GeoLocation geo1 = GeoLocation.generate(150, 0);
        GeoLocation geo2 = GeoLocation.generate(10, 0);
        int actual = geo1.compareTo(geo2);
        assertEquals(1, actual);

        geo1 = GeoLocation.generate(170,90);
        geo2 = GeoLocation.generate(80, -90);
        actual = geo1.compareTo(geo2);
        assertEquals(1, actual);

        geo1 = GeoLocation.generate(120, 90);
        geo2 = GeoLocation.generate(80, 20);
        actual = geo1.compareTo(geo2);
        assertEquals(1, actual);

        //test case when hour offset are not the same and hour offset2 > hour offset1
        geo1 = GeoLocation.generate(-120, 90);
        geo2 = GeoLocation.generate(120, 0);
        actual = geo1.compareTo(geo2);
        assertEquals(-1, actual);

        geo1 = GeoLocation.generate(-120, 90);
        geo2 = GeoLocation.generate(120, 20);
        actual = geo1.compareTo(geo2);
        assertEquals(-1, actual);

        geo1 = GeoLocation.generate(-150, 45.9);
        geo2 = GeoLocation.generate(90, 68.4752);
        actual = geo1.compareTo(geo2);
        assertEquals(-1, actual);

        //test case when hour offset and  latitude of the two instances are the same
        geo1 = GeoLocation.generate(90, 20);
        geo2 = GeoLocation.generate(100, 20);
        actual = geo1.compareTo(geo2);
        assertEquals(0, actual);

        //test case when hour offset are the same but latitude 1 > latitude2
        geo1 = GeoLocation.generate(56, 55.0111);
        geo2 = GeoLocation.generate(41, 55);
        actual = geo1.compareTo(geo2);
        assertEquals(1, actual);

        //test case when hour offset are the same but latitude2 > latitude1
        geo1 = GeoLocation.generate(116, 89.9999);
        geo2 = GeoLocation.generate(115, 90.0);
        actual = geo1.compareTo(geo2);
        assertEquals(-1, actual);
    }

    @Test
    public void test5_getCount() throws Exception
    {
        GeoLocation.clear();
        int expected = 0;
        int actual = GeoLocation.getCount();
        assertEquals(expected, actual);

        GeoLocation geo1 = GeoLocation.generate(10, 10);
        expected = 1;
        actual = GeoLocation.getCount();
        assertEquals(expected, actual);

        GeoLocation geo2 = GeoLocation.generate(100, 100);
        expected = 2;
        actual = GeoLocation.getCount();
        assertEquals(expected, actual);

        GeoLocation geo3 = GeoLocation.generate(100, 100);
        expected = 3;
        actual = GeoLocation.getCount();
        assertEquals(2, GeoLocation.getCount());
    }


    @Test
    public void test6_distance() throws Exception
    {
        final double radius = 6371;
        final double delta = 0.000001;

        GeoLocation geo1 = GeoLocation.generate(90, 90);
        GeoLocation geo2;
        double actual = GeoLocation.distance(geo1,geo1);
        assertEquals(0.0, actual, delta);

        geo1 = GeoLocation.generate(0, 0);
        geo2 = GeoLocation.generate(90, 0);
        actual = GeoLocation.distance(geo1, geo2);
        assertEquals(Math.PI*6371/2, actual, delta);

        geo1 = GeoLocation.generate(0, 0);
        geo2 = GeoLocation.generate(45, 0);
        actual = GeoLocation.distance(geo1,geo2);
        assertEquals(Math.PI*6371/4, actual, delta);
    }

}