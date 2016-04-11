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
public class GPSUnit {
    
    public double Longitude;
    public double Latitude;
    public final String ID;
    public static final double DEFAULT_PRECISION = 0.0001;
    
    public GPSUnit(String ID, double latitude, double longitude) {
        this.ID = ID;
        this.Longitude = longitude;
        this.Latitude = latitude;
    }
    
    public GPSUnit(String ID) {
        this.ID = ID;
        this.Longitude = 0;
        this.Latitude = 0;
    }
    
    public boolean isCloseEnough(double latitude, double longitude, double precision) {
        return (Math.hypot(this.Latitude - latitude, this.Longitude - longitude) <= precision);
    }
    
    public boolean isCloseEnough(double latitude, double longitude) {
        return isCloseEnough(latitude, longitude, DEFAULT_PRECISION);
    }
    
    @Override
    public String toString() {
        return "GPSUnit: {ID: "+ID+", Lat: "+Latitude+", Lng: "+Longitude+"}";
    }
}
