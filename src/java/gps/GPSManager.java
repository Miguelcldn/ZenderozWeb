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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import estructuras.GPSUnit;
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
    private final HashMap<String, GPSUnit> units;
    private final Timer timer;
    private final int UPDATE_RATE = 1000;
    private final DBManager dbManager;
    
    public GPSManager(String server, int port) {
        this.server = server;
        this.port = port;
        units = query();
        timer = new Timer();
        dbManager = new DBManager(null);
    }
    
    public void Start() {
        timer.schedule(this, 0, UPDATE_RATE);
    }
    
    public HashMap<String, GPSUnit> query() {
        
        HashMap<String, GPSUnit> result;
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
    
    private HashMap<String, GPSUnit> processResponse(String response) {
        
        HashMap<String, GPSUnit> newUnits = new HashMap<>();
        String content = response.replaceAll("\\{|\\}", "");
        String[] unitsData = content.split(",");
        
        for(int i = 0; i < unitsData.length; i += 3) {
            
            newUnits.put(unitsData[i], new GPSUnit(unitsData[i],
                    Double.parseDouble(unitsData[i + 1]),
                    Double.parseDouble(unitsData[i + 2])));
        }
        
        return newUnits;
    }
    
    private void update() {
        HashMap<String, GPSUnit> newPositions = query();
        
        newPositions.keySet().stream().forEach((String id) -> {
            
            GPSUnit newPos = newPositions.get(id);
            if(units.get(id).Move(newPos.lat, newPos.lng, UPDATE_RATE)) {
                //update next stop
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
}
