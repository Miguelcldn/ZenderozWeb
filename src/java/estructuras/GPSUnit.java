/*
 * The MIT License
 *
 * Copyright 2016 Miguel Celedon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package estructuras;

import db.Schema;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Miguel Celedon
 */
public class GPSUnit extends GPSSpot {
    
    private double avgSpeed = 0;
    private int samples = 0;
    private double nextTargetDist;
    private RouteStop nextTarget = null;
    private long routeID = 0;
    public static final double DEFAULT_PRECISION = 10;
    private static final double MAX_SAMPLES = 10;
    private static final double EARTHMEANRADIUS = 6371008.8;
    
    
    public GPSUnit(long ID, long routeID) {
        super(ID);
        this.routeID = routeID;
    }
    
    public GPSUnit(long ID, double latitude, double longitude) {
        super(ID, latitude, longitude);
    }
    
    public GPSUnit(long ID) {
        super(ID);
    }
    
    public GPSUnit(ResultSet rs) throws SQLException {
        super(rs.getLong(Schema.BUS_IDBUS));
        this.routeID = rs.getLong(Schema.BUS_ROUTEID);
    }
    
    public double getAvgSpeed() {
        return avgSpeed;
    }
    
    public boolean Move(double newLat, double newLng, double msTime) {
        double movedDistance = ComputeArcDistance(lat, lng, newLat, newLng);
        double frameSpeed = movedDistance / msTime;
        
        avgSpeed = (avgSpeed * samples + frameSpeed) / (samples + 1);
        
        if(samples < MAX_SAMPLES) samples++;
        
        lat = newLat;
        lng = newLng;
        
        if(this.isTargetKnown()) {
            nextTargetDist -= movedDistance;
            return nextTargetDist < 10;
        }
        else {
            return false;
        }
    }
    
    private static double ComputeArcDistance(double lat1, double lng1, double lat2, double lng2) {
        double lat = Math.abs(lat1 - lat2) / 2;
        double lng = Math.abs(lng1 - lng2) / 2;
        double varAngle;
        
        lat = Math.pow(Math.sin(lat), 2);
        lng = Math.pow(Math.sin(lng), 2);
        
        varAngle = 2 * Math.asin(lat + Math.cos(lat1) * Math.cos(lat2) * lng);
        
        return EARTHMEANRADIUS * varAngle;
    }
    
    public void setCoordinates(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    /**
     * @return the nextTargetDist
     */
    public double getNextTargetDist() {
        return nextTargetDist;
    }

    /**
     * @param target
     */
    public void setNextTarget(RouteStop target) {
        this.nextTarget = target;
        this.nextTargetDist = target.getDistance();
    }
    
    public RouteStop getNextTarget() {
        return nextTarget;
    }
    
    public boolean isCloseEnough(double latitude, double longitude, double precision) {
        double result = Math.hypot(this.lat - latitude, this.lng - longitude) * EARTHMEANRADIUS;
        return (result <= precision);
    }
    
    public boolean isCloseEnough(double latitude, double longitude) {
        return isCloseEnough(latitude, longitude, DEFAULT_PRECISION);
    }
    
    public boolean isTargetKnown() {
        return nextTarget != null;
    }
    
    @Override
    public String toString() {
        return "GPSUnit: {ID: "+this.getID()+", Lat: "+lat+", Lng: "+lng+"}";
    }
    
    public String JSONSerialize() {
        return this.getID() + ":{lat:" + lat + ",lng:" + lng + "}";
    }

    /**
     * @return the routeID
     */
    public long getRouteID() {
        return routeID;
    }

    /**
     * @param routeID the routeID to set
     */
    public void setRouteID(long routeID) {
        this.routeID = routeID;
    }
}
