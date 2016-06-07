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
package gps;

import db.DBManager;
import estructuras.BusRoute;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import estructuras.GPSUnit;
import estructuras.RouteStop;
import java.util.TimerTask;
import java.util.Timer;
import java.util.HashMap;

/**
 *
 * @author Miguel Celedon
 */
public final class GPSManager extends TimerTask {
    
    private final String server;
    private final int port;
    private final HashMap<Long, GPSUnit> units;
    private final Timer timer;
    private final int UPDATE_RATE = 1000;
    private final DBManager dbManager;
    private final HashMap<Long, BusRoute> routes;
    
    public GPSManager(String server, int port) {
        this.server = server;
        this.port = port;
        timer = new Timer();
        dbManager = new DBManager(null);
        routes = dbManager.getRoutes();
        units = dbManager.getUnits();
    }
    
    public void Start() {
        timer.schedule(this, 0, UPDATE_RATE);
    }
    
    public HashMap<Long, GPSUnit> query() {
        
        HashMap<Long, GPSUnit> result;
        Socket socket;
        BufferedReader input;
        
        try {
            socket = new Socket(server, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            result = processResponse(input.readLine());
            
        } catch (IOException ex) {
            result = null;
            System.out.println("Error: Could not connect to \"" + server + ":" + port + "\"\nReason: " + ex.getMessage());
        }
        
        return result;
    }
    
    private HashMap<Long, GPSUnit> processResponse(String response) {
        
        HashMap<Long, GPSUnit> newUnits = new HashMap<>();
        String content = response.replaceAll("\\{|\\}", "");
        String[] unitsData = content.split(",");
        
        for(int i = 0; i < unitsData.length; i += 3) {
            
            newUnits.put(Long.parseLong(unitsData[i]), new GPSUnit(Long.parseLong(unitsData[i]),
                    Double.parseDouble(unitsData[i + 1]),
                    Double.parseDouble(unitsData[i + 2])));
        }
        
        return newUnits;
    }
    
    private void update() {
        HashMap<Long, GPSUnit> newPositions = query();
        
        newPositions.keySet().stream().forEach((Long id) -> {
            
            GPSUnit newPos = newPositions.get(id),
                    unit = units.get(id);
            
            if(unit.Move(newPos.lat, newPos.lng, UPDATE_RATE)) {
                
                RouteStop stop = unit.getNextTarget();
                stop = routes.get(unit.getRouteID()).getNextStop(stop.getID());
                unit.setNextTarget(stop);
                
            } else if(!unit.isTargetKnown()) {
                
                RouteStop[] stops = routes.get(unit.getRouteID()).getStops();
                
                for(RouteStop stop : stops) {
                    if(unit.isCloseEnough(stop.lat, stop.lng)) {
                        unit.setNextTarget(stop);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void run() {
        update();
    }

    /**
     * @return the units
     */
    public GPSUnit[] getUnits() {
        
        GPSUnit[] result;
        
        try {
            Collection<GPSUnit> c = units.values();
            result = c.toArray(new GPSUnit[0]);
            return Arrays.copyOf(result, result.length);
        }
        catch(Exception e) {
            
        }
        
        return null;
    }
    
    public BusRoute[] getRoutes() {
        return routes.values().toArray(new BusRoute[0]);
    }
}
