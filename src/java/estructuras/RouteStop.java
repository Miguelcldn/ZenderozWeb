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
public class RouteStop extends Stop {
    
    private final long routeID;
    private final int distance;
    private final int order;
    
    public RouteStop(long ID, String name, double lat, double lng, long routeID, int distance, int order) {
        super(ID, name, lat, lng);
        this.routeID = routeID;
        this.distance = distance;
        this.order = order;
    }
    
    public RouteStop(Stop stop, long routeID, int distance, int order) {
        super(stop.getID(), stop.getName(), stop.lat, stop.lng);
        this.routeID = routeID;
        this.distance = distance;
        this.order = order;
    }
    
    public RouteStop(ResultSet rs) throws SQLException {
        super(rs.getLong(Schema.RS_IDSTOP), rs.getString(Schema.RS_NAME), rs.getDouble(Schema.RS_LAT), rs.getDouble(Schema.RS_LNG));
        this.routeID = rs.getLong(Schema.RS_IDROUTE);
        this.distance = rs.getInt(Schema.RS_DISTANCE);
        this.order = rs.getInt(Schema.RS_ORDER);
    }

    /**
     * @return the routeID
     */
    public long getRouteID() {
        return routeID;
    }

    /**
     * @return the distance
     */
    public int getDistance() {
        return distance;
    }
    
    public int getOrder() {
        return order;
    }
}
