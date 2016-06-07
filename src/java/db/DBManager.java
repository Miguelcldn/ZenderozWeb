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
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import estructuras.GPSUnit;
import estructuras.BusRoute;
import estructuras.RouteStop;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Miguel Celedon
 */
public class DBManager {
    
    private Connection conn;
    private final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/zenderozwebdb?user=root&password=300592";
    
    public DBManager(String connString) {
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            if(connString == null) connString = CONNECTION_STRING;

            conn = DriverManager.getConnection(connString);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            System.out.println("Could not connect to DB: " + ex.getMessage());
        }
    }
    
    private Statement makeSelect(String query) {
        Statement state;
        
        try {
            state = conn.createStatement();
            
            state.executeQuery(query);
            
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            state = null;
        }
        
        return state;
    }
    
    private Statement makeSelect(String fields, String table, String condition) {
        Statement state;
        String query = "";
        fields = (fields == null || fields.isEmpty()) ? "*" : fields;
        boolean conditioned = (condition != null && !condition.isEmpty());
        
        query += "SELECT " + fields + " FROM " + table;
        
        if(conditioned)
            query += " WHERE " + condition;
        
        query += ";";
        
        state = makeSelect(query);
        
        return state;
    }
    
    public HashMap<Long, GPSUnit> getUnits() {
        HashMap<Long, GPSUnit> units = new HashMap<>();
        Statement state = makeSelect(null, Schema.BUS, null);
        ResultSet rs = null;
        long unitID;
        long routeID;
        
        if(state != null) {
            try {
                rs = state.getResultSet();
                
                while(rs != null && rs.next()) {
                    unitID = rs.getLong(Schema.BUS_IDBUS);
                    routeID = rs.getLong(Schema.BUS_ROUTEID);
                    
                    units.put(unitID, new GPSUnit(unitID, routeID));
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if(rs != null) rs.close();
                    state.close();
                } catch (SQLException ex) {}
            }
        }
        
        return units;
    }
    
    public HashMap<Long, BusRoute> getRoutes() {
        HashMap<Long, BusRoute> allRoutes = new HashMap<>();
        Long currentID;
        Statement stateRoutes = null, stateStops = null;
        ResultSet rsRoutes = null, rsStops = null;
        
        try {
            
            stateRoutes = makeSelect(null, Schema.ROUTE, null);
            rsRoutes = stateRoutes.getResultSet();
            
            while(rsRoutes.next()) {
                ArrayList<RouteStop> stops = new ArrayList<>();
                BusRoute busRoute = new BusRoute(rsRoutes);
                currentID = busRoute.getID();
                
                stateStops = makeSelect(null, Schema.RS_VIEW, Schema.RS_IDROUTE + "=" + currentID.toString());
                rsStops = stateStops.getResultSet();
                
                while(rsStops.next()) {
                    stops.add(new RouteStop(rsStops));
                }
                
                rsStops.close();
                stateStops.close();
                
                busRoute.setStops(stops.toArray(new RouteStop[0]));
                allRoutes.put(currentID, busRoute);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(rsRoutes != null) rsRoutes.close();
                if(stateRoutes != null) stateRoutes.close();
                if(rsStops != null) rsStops.close();
                if(stateStops != null) stateStops.close();
            } catch (SQLException ex) {}
        }
        
        return allRoutes;
    }
}
