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

import java.sql.ResultSet;
import db.Schema;
import java.sql.SQLException;

/**
 *
 * @author Miguel Celedon
 */
public class Stop extends GPSSpot {
    
    private final String name;
    
    public Stop(long ID, String name, double lat, double lng) {
        super(ID, lat, lng);
        this.name = name;
    }
    
    public Stop(ResultSet rs) throws SQLException {
        super(rs.getLong(Schema.STOP_IDSTOP), rs.getLong(Schema.STOP_LAT), rs.getLong(Schema.STOP_LNG));
        this.name = rs.getString(Schema.STOP_NAME);
    }
    
    public String getName() {
        return this.name;
    }
}
