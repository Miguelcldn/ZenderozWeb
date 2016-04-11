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
package simulator;

import estructuras.GPSUnit;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import zenderoz.IGPSProvider;
import simulator.structures.SimulatorData;

/**
 *
 * @author Miguel Celedon
 */
public final class GPSProvider extends TimerTask implements IGPSProvider {

    private final ArrayList<GPSUnit> units;
    private final Timer timer;
    private final SimulatorData data;
    private final int[] nextPoints;
    private final int STEPS_BETWEEN_CHECKPOINTS;
    
    public GPSProvider() {
        this.STEPS_BETWEEN_CHECKPOINTS = 10;
        timer = new Timer();
        data = new SimulatorData();
        this.units = new ArrayList<>();
        nextPoints = new int[data.units.length];
        Start();
    }
    
    public void Start() {
        
        double[] position;
        int i = 0;
        
        for(String id : data.units) {
            int p = (int) Math.floor(Math.random() * data.checkpoints.length);
            position = data.checkpoints[p];
            
            //Set the next checkpoint for each unit
            nextPoints[i] = p + 1;
            i++;
            
            units.add(new GPSUnit(id, position[0], position[1]));
        }
        
        timer.schedule(this, 0, 1000);
    }

    @Override
    //Updates the units positions
    public void run() {
        
        GPSUnit unit;
        double[] point;
        
        for(int i = 0; i < units.size(); i++) {
            
            unit = units.get(i);
            point = data.checkpoints[nextPoints[i]];
            unit.Latitude += (point[0] - unit.Latitude) / STEPS_BETWEEN_CHECKPOINTS;
            unit.Longitude += (point[1] - unit.Longitude) / STEPS_BETWEEN_CHECKPOINTS;
            
            if(unit.isCloseEnough(point[0], point[1])) {
                nextPoints[i] = (nextPoints[i] + 1) % data.checkpoints.length;
                unit.Latitude = point[0];
                unit.Longitude = point[1];
            }
        }
    }
    
    @Override
    public GPSUnit[] getAllUnits() {
        return units.toArray(new GPSUnit[0]);
    }

    @Override
    public GPSUnit getUnit(String ID) {
        for(GPSUnit unit : units) {
            if(unit.ID.equals(ID))
                return unit;
        }
        
        return null;
    }
}
