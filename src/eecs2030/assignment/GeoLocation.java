package eecs2030.assignment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jay Cen on 5/29/2017.
 */
public final class GeoLocation implements Comparable<GeoLocation>
{
    /**
     * <p>
     *     A geo location is defined as a pair of numbers of precision 4 digits
     * after the decimal point: the longitude ( -180 to +180 , where -180
     * equals +180) and the latitude -90 to +90
     * </p>
     *
     * @author Jay Cen
     * @since 1.0
     */

    /**
     *
     */
    final private double longitude ;
    final private double latitude;
    private static int count = 0;

    private static Map<String, GeoLocation> instance = new HashMap<String, GeoLocation>();

    private GeoLocation()
    {
        longitude = 0;
        latitude = 0;
    }

    /**
     * Constructs a new GeoLocation object that contains two double values of longitude and latitude
     * @throws IllegalArgumentException if either longitude or latitude is not in the range
     * @param longitude  a double type value
     * @param lat  a double type value
     */
    private GeoLocation(double longitude, double lat)
    {
        if(longitude < -180 || longitude > 180 || lat < -90 || lat > 90)
            throw new IllegalArgumentException("You Enter an invalid argument!!!");
        this.longitude = round(longitude,  4);
        this.latitude = round(lat, 4);
    }

    /**
     * return longtitude
     * @return the double type longitude
     */
    public double getLongitude()
    {
        return this.longitude;
    }

    /**
     *return latitude
     * @return the double type latitude
     */
    public double getLatitude()
    {
        return this.latitude;
    }
    /**
     * Calculate the GMT hour offset depending on the globe's longitude. The Hour offset is ranging
     * between -12 and 11.
     *
     * <p><br>
     *    <b>For Example</b><br>
     *    Longitude of -180 and 180 will return a value of -12<br>
     *    Longitude of 175 will return a value of 11;<br>
     *    Longitude of 0 will return a value of 0;<br>
     * </p>
     * @return the GMT hour offset
     */
    public int getGMTHourOffset()
    {
        //default case
        int result = (int)(Math.floor(this.longitude/ 15));

        double positive180 = Double.compare(this.longitude, 180);
        double negative180 = Double.compare(this.longitude, -180);

        if( positive180 == 0 || negative180 == 0)
            result = -12;
        return result;
    }

    //helper method
    private static double round(double num, int n)
    {
        return  new BigDecimal(num).setScale(n, RoundingMode.HALF_UP).doubleValue();
    }

    //refer to the powerpoint for the equals method receipt

    /**
     * The method determines if the two locations are the same point;
     * @param obj an object
     * @return true if the two locations are at the same point on the globe; false otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        boolean output = false;
        if(obj == null || this.getClass() != obj.getClass())
        {
//            System.out.println("1");
            return false;
        }

        GeoLocation geo = (GeoLocation)obj;

        if(this == obj)
        {
//            System.out.print("2");
            output = true;
        }

        double thisLon = Double.doubleToLongBits(this.longitude);
        double objLon = Double.doubleToLongBits(geo.longitude);

        double thisLat = Double.doubleToLongBits(this.latitude);
        double objLat = Double.doubleToLongBits(geo.latitude);

        if(thisLon == objLon)
        {
//            System.out.println("3");
            if(thisLat == objLat)
                output = true;
        }
        return output;
    }

    /**
     * Compare two GeoLocation objects numerically by hour offset , then by latitude
     * @param other the GeoLocation to be compared
     * @return the value 0 if the two GeoLocations have the same hour offset and latitude;
     * the value -1 if the two GeoLocations have the same hour offset but this GeoLocation's latitude is
     * less than the other GeoLocation's longitude <b>OR</b> the hour offset of this GeoLocation is less than that
     * of GeoLocation; the value 1 if the two GeoLocations have the same hour offset but this GeoLocation's latitude\
     * is greater than that of GeoLocation.
     */
    //refer to the powerpoint for the slides saying CompareTo method
    @Override
    public int compareTo(GeoLocation other)
    {
        int output = 1;

        int thisOffset = this.getGMTHourOffset();
        int otherOffset = other.getGMTHourOffset();

        double compareLat = Double.compare(this.latitude, other.latitude);

        if(thisOffset == otherOffset)
        {
            if(compareLat == 0)
                output = 0;
            else if(compareLat == -1)
                output = -1;
            else
                output = 1;
        }
        else
        {
            if(thisOffset < otherOffset)
                output = -1;
        }

        return output;
    }

    //keeping track of the number of created objects and return that count via getCount()

    /**
     * It keeps track of the number of objects creation
     * @return the number of created objects stored
     */
    public static int getCount()
    {
        return count;
    }

    /**
     * reset the number of created objects to zero, and initiate the instance to an empty MapList
     */
    public static void clear()
    {
        count = 0;
        instance.clear();
    }

    //utility method distance() with 2 Geolocations parameters,
    //calculating the shortest distance between the given locations and the distance of Earth

    /**
     * Compare and determine the shortest distance between the given locations and the radius of Earth,
     * which is 6,371 km
     * @param location1 a Geolocation object
     * @param location2 another Geolocation object
     * @return the shortest distance (in Km)  between the locations and the radius of Earth
     */
    public static double distance(GeoLocation location1, GeoLocation location2)
    {
        double radius  = 6371;
        double deltaLAT = Math.toRadians(Math.abs(location2.latitude - location1.latitude));
        double deltaLON = Math.toRadians(Math.abs(location2.longitude - location1.latitude));
        double lat1 = Math.toRadians(location1.latitude);
        double lat2 = Math.toRadians(location2.latitude);

        double a = Math.pow(Math.sin(deltaLAT / 2 ),2) + (Math.cos(lat1) * Math.cos( lat2 ) * Math.pow(Math.sin(deltaLON / 2),2));

        double c = 2 * Math.atan2(Math.sqrt( a ),Math.sqrt( 1 - a ));

        double distance = radius * c;

        return distance;
    }

    //factory method , generate() with two double type parameters that stored for longitude and latitude
    //creation of a Geolocation
    //check the slides for more details

    /**
     * a factory method that return a GeoLocation object with the specified parameters
     * @param lonitude a double type
     * @param lat a double type
     * @return the creation of a robust object, GeoLocation, that will work for any input
     * passed as arguments
     */
    public static GeoLocation generate(double lonitude, double lat)
    {
        //converting longitude and latitude to its valid range
        if(lonitude > 180 || lonitude < -180)
        {
            if(lonitude % 180 == 0)
                lonitude = 0;
            else
                lonitude =  (lonitude % 180) + (-1 * (lonitude/Math.abs(lonitude)) * 180);
        }

        if(lat > 90 || lat < -90)
        {
            lat =  (lat/Math.abs(lat) * 90);
        }

        //create a new object using the private constructor
        GeoLocation geo = new GeoLocation(lonitude,lat);
        String key = lonitude + " " + lat;

        //if the Location that is generate is not found in the MapList <i>instance</i>
        if(!instance.containsKey(key))
        {
            count++;
            instance.put(key,geo);
        }
        return geo;
    }

    //helper method
    private static String formatLat(double num)
    {
        return new DecimalFormat("+00.0000;-00.0000").format(num);
    }

    //helper method
    private static String formatLon(double num)
    {
        return new DecimalFormat("+000.0000;-000.000").format(num);
    }

    /**
     *
     * @return a String using the format of ( +000.0000, -00.0000)
     */
    @Override
    public String toString()
    {
        String lon = formatLon(this.longitude);
        String lat = formatLat(this.latitude);
        return String.format("(%s,%s)", lon, lat);
    }

    /**public static void main(String[] args)
    {
        GeoLocation geo1 = GeoLocation.generate(-190, 80);
        GeoLocation geo2 = GeoLocation.generate(0, -8);
        System.out.println(geo2);
    }*/

}


