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
import estructuras.Stop;
import estructuras.BusRoute;
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
    
    private ResultSet makeSelect(String query) {
        ResultSet rs = null;
        Statement state = null;
        
        try {
            state = conn.createStatement();
            
            rs = state.executeQuery(query);
            
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            rs = null;
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore

                rs = null;
            }

            if (state != null) {
                try {
                    state.close();
                } catch (SQLException sqlEx) { } // ignore
            }
        }
        
        return rs;
    }
    
    private ResultSet makeSelect(String fields, String table, String condition) {
        ResultSet rs;
        String query = "";
        fields = (fields == null || fields.isEmpty()) ? "*" : fields;
        boolean conditioned = (condition != null && !condition.isEmpty());
        
        query += "SELECT " + fields + " FROM " + table;
        
        if(conditioned)
            query += " WHERE " + condition;
        
        query += ";";
        
        rs = makeSelect(query);
        
        return rs;
    }
    
    public GPSUnit[] getUnits() {
        ArrayList<GPSUnit> units = new ArrayList();
        ResultSet rs = makeSelect(null, Schema.BUS, null);
        long unitID;
        long routeID;
        
        if(rs != null) {
            try {
                while(rs.next()) {
                    unitID = rs.getLong(Schema.BUS_IDBUS);
                    routeID = rs.getLong(Schema.BUS_ROUTEID);
                    
                    units.add(new GPSUnit(unitID, routeID));
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return units.toArray(new GPSUnit[0]);
    }
    
    public HashMap<Long, BusRoute> getRoutes() {
        HashMap<Long, BusRoute> allRoutes = new HashMap<>();
        Long currentID;
        ArrayList<BusRoute> routes;
        ResultSet rsRoutes;
        ResultSet rsStops;
        
        try {
            
            rsRoutes = makeSelect(null, Schema.ROUTE, null);
            
            while(rsRoutes.next()) {
                ArrayList<BusRoute> stops;
                BusRoute busRoute = new BusRoute(rsRoutes);
                
                
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return allRoutes;
    }
}
