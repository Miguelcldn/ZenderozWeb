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

/**
 *
 * @author Miguel Celedon
 */
public class Schema {
    
    public static final String DB = "zenderozwebdb";
    
    public static final String BUS = DB + ".bus",
            BUS_IDBUS = "idBus",
            BUS_ROUTEID = "Route_idRoute",
            BUS_NAME = "Name",
            ROUTE = DB + ".route",
            ROUTE_IDROUTE = "idRoute",
            ROUTE_NAME = "Name",
            ROUTE_RETURNSTOP = "ReturnStop",
            STOP = DB + ".stop",
            STOP_IDSTOP = "idStop",
            STOP_LAT = "Lat",
            STOP_LNG = "Lng",
            STOP_NAME = "Name",
            ROUTESTOP = DB + ".route_stop",
            ROUTESTOP_ROUTE = "Route_idRoute",
            ROUTESTOP_STOP = "Stop_idStop",
            ROUTESTOP_ORDER = "Order",
            ROUTESTOP_DISTANCE = "Distance",
            RS_VIEW = DB + ".route_stops_view",
            RS_IDROUTE = "idRoute",
            RS_IDSTOP = "idStop",
            RS_NAME = "Name",
            RS_ORDER = "Order",
            RS_DISTANCE = "Distance",
            RS_LAT = "Lat",
            RS_LNG = "Lng";
}