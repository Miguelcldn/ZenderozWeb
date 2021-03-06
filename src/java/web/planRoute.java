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
package web;

import estructuras.BusRoute;
import estructuras.GPSUnit;
import estructuras.PlanResult;
import estructuras.RouteStop;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import zenderoz.ZenderozApp;
import zenderoz.Utils;

/**
 *
 * @author Miguel Celedon
 */
public class planRoute extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        try (PrintWriter out = response.getWriter()) {
            
            switch(request.getServletPath()) {
                case "/doRoutePlan":
                    doPlanRoute(request, out);
                    break;
                case "/avenues":
                    getAvenues(request, out);
                    break;
                case "/streets":
                    getStreets(request, out);
                    break;
                case "/units":
                    getUnits(request, out);
                    break;
                case "/routes":
                    getRoutes(request, out);
                    break;
                case "/distance":
                    getDistance(request, out);
                    
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    protected void doPlanRoute(HttpServletRequest request, PrintWriter out) {
        
        Integer distance = Integer.parseInt(request.getParameter("maxDistance")),
                routeType = 0;
        String oStreet = request.getParameter("originStreet"),
                oAv = request.getParameter("originAv"),
                dStreet = request.getParameter("destStreet"),
                dAv = request.getParameter("destAv"),
                type = request.getParameter("type");
        
        if(type.equals("change"))
            routeType = 1;
        else if(type.equals("distance"))
            routeType = 2;
        
        ZenderozApp app = ZenderozApp.getInstance();
        PlanResult result = app.planRoute(distance, routeType, oAv, oStreet, dAv, dStreet);
        
        out.write("{\"url\":\"" + result.getURL() + "\",");
        out.write("\"narration\":\"" + result.getNarration() + "\"}");
        
    }
    
    protected void getAvenues(HttpServletRequest request, PrintWriter out) {
        
        ZenderozApp app = ZenderozApp.getInstance();
        String[] avenues = app.getAvenues(request.getParameter("street"));
        
        out.write(Utils.arrayToJSON(avenues));
    }
    
    protected void getStreets(HttpServletRequest request, PrintWriter out) {
        
        ZenderozApp app = ZenderozApp.getInstance();
        String[] streets = app.getStreets();
        
        out.write(Utils.arrayToJSON(streets));
    }
    
    protected void getUnits(HttpServletRequest request, PrintWriter out) {
        
        ZenderozApp app = ZenderozApp.getInstance();
        GPSUnit[] units = app.getUnits();
        boolean first = true;
        
        out.write("[");
        
        for(GPSUnit u : units) {
            if(!first) out.write(",");
                        
            out.write("{");
            out.write("\"id\":" + u.getID() + "");
            out.write(",\"lat\":" + u.lat);
            out.write(",\"lng\":" + u.lng);
            out.write(",\"avgSpeed\":" + u.getAvgSpeed());
            out.write(",\"nextTargetDistance\":" + ((u.isTargetKnown()) ? u.getNextTargetDist() : "null"));
            out.write(",\"routeID\":" + u.getRouteID());
            out.write(",\"nextTargetID\":" + ((u.isTargetKnown()) ? u.getNextTarget().getID() : "null"));
            out.write("}");
            
            first = false;
        }
        
        out.write("]");
    }
    
    protected void getRoutes(HttpServletRequest request, PrintWriter out) {
        
        ZenderozApp app = ZenderozApp.getInstance();
        BusRoute[] routes = app.getRoutes();
        boolean firstRoute = true, firstStop = true;
        
        out.write("[");
        
        for(BusRoute route : routes) {
            if(!firstRoute) out.write(",");
            
            out.write("{");
            out.write("\"routeID\":" + route.getID());
            out.write(",\"returnStop\":" + route.getReturnStopID());
            out.write(",\"routeName\":\"" + route.getName() + "\"");
            out.write(",\"stops\":[");
            for(RouteStop stop : route.getStops()) {
                if(!firstStop) out.write(",");
                
                out.write("{");
                out.write("\"routeID\":" + stop.getRouteID());
                out.write(",\"stopID\":" + stop.getID());
                out.write(",\"stopName\":\"" + stop.getName() + "\"");
                out.write(",\"stopOrder\":" + stop.getOrder());
                out.write(",\"lat\":" + stop.lat);
                out.write(",\"lng\":" + stop.lng);
                out.write("}");
                
                firstStop = false;
            }
            out.write("]");
            out.write("}");
            
            firstRoute = false;
        }
        
        out.write("]");
    }
    
    protected void getDistance(HttpServletRequest request, PrintWriter out) {
        ZenderozApp app = ZenderozApp.getInstance();
        
        try {
            long routeID = Long.parseLong(request.getParameter("routeid"));
            long stopID = Long.parseLong(request.getParameter("stopid"));

            Object[] bestUnit = app.getClosestUnit(routeID, stopID);
            double distance = (double)(bestUnit[1]);
            GPSUnit unit = (GPSUnit)(bestUnit[0]);

            if(distance != Double.MAX_VALUE) {
                out.write("{\"distance\":" + distance);
                out.write(",\"unitID\":" + unit.getID() + "}");
            }
            else {
                out.write("{\"error\":\"Could not find any unit or stop\"");
            }
        } catch(Exception e) {
            out.write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
