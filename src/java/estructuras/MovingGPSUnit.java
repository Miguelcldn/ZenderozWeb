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

/**
 *
 * @author Miguel Celedon
 */
public class MovingGPSUnit extends GPSUnit {
    
    private double avgSpeed;
    private int samples;
    private double nextTargetDist;
    private static final double MAX_SAMPLES = 10;
    
    public MovingGPSUnit(String ID, double latitude, double longitude) {
        super(ID, latitude, longitude);
        
        avgSpeed = 0;
        samples = 0;
    }
    
    public double getAvgSpeed() {
        return avgSpeed;
    }
    
    public boolean Move(double newLat, double newLng, double msTime) {
        double movedDistance = ComputeArcDistance(Latitude, Longitude, newLat, newLng);
        double frameSpeed = movedDistance / msTime;
        
        avgSpeed = (avgSpeed * samples + frameSpeed) / (samples + 1);
        
        if(samples < MAX_SAMPLES) samples++;
        
        Latitude = newLat;
        Longitude = newLng;
        
        nextTargetDist -= movedDistance;
        
        return nextTargetDist < 10;
    }
    
    private static double ComputeArcDistance(double lat1, double lng1, double lat2, double lng2) {
        double lat = Math.abs(lat1 - lat2) / 2;
        double lng = Math.abs(lng1 - lng2) / 2;
        double varAngle;
        double earthMeanRadius = 6371008.8;
        
        lat = Math.pow(Math.sin(lat), 2);
        lng = Math.pow(Math.sin(lng), 2);
        
        varAngle = 2 * Math.asin(lat + Math.cos(lat1) * Math.cos(lat2) * lng);
        
        return earthMeanRadius * varAngle;
    }

    /**
     * @return the nextTargetDist
     */
    public double getNextTargetDist() {
        return nextTargetDist;
    }

    /**
     * @param nextTargetDist the nextTargetDist to set
     */
    public void setNextTargetDist(double nextTargetDist) {
        this.nextTargetDist = nextTargetDist;
    }
}
