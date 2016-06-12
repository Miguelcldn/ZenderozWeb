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
public class BusRoute {
    private final String name;
    private final long ID;
    private final long returnStopID;
    private RouteStop[] stops;
    
    public BusRoute(String name, long ID, long returnID, RouteStop[] stops) {
        this.name = name;
        this.ID = ID;
        this.returnStopID = returnID;
        this.stops = stops;
    }
    
    public BusRoute(String name, long ID, long returnID) {
        this.name = name;
        this.ID = ID;
        this.returnStopID = returnID;
        this.stops = new RouteStop[0];
    }
    
    public BusRoute(ResultSet rs) throws SQLException {
        this.ID = rs.getLong(Schema.ROUTE_IDROUTE);
        this.name = rs.getString(Schema.ROUTE_NAME);
        this.returnStopID = rs.getLong(Schema.ROUTE_RETURNSTOP);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the ID
     */
    public long getID() {
        return ID;
    }

    /**
     * @return the returnStopID
     */
    public long getReturnStopID() {
        return returnStopID;
    }

    /**
     * @return the stops
     */
    public RouteStop[] getStops() {
        return stops;
    }
    
    public RouteStop getStop(long ID) {
        for(RouteStop stop : stops) {
            if(stop.getID() == ID)
                return stop;
        }
        
        return null;
    }
    
    public RouteStop getNextStop(long pastID) {
        for(int i = 0; i < stops.length; i++) {
            long actualID = stops[i].getID();
            
            if(actualID == pastID)
                return stops[(i + 1)];
        }
        
        return null;
    }
    
    public int getStopPosition(long ID) {
        for(int i = 0; i < stops.length; i++) {
            if(stops[i].getID() == ID)
                return i;
        }
        
        return -1;
    }

    /**
     * @param stops the stops to set
     */
    public void setStops(RouteStop[] stops) {
        this.stops = stops;
    }
}
