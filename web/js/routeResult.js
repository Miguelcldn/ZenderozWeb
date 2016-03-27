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
/* jshint browser : true, jquery : true */

$(document).ready(main);
$("#backButton").click(window.goToRoot);

function main() {
    
    queryRoute();
}

function queryRoute() {
    
    $("#instructionsTextArea").val("Calculando ruta, por favor espera...");
    
    var params = getParams();
    
    $.ajax({
        url : "doRoutePlan" + params,
        method: "GET",
        success: drawResult,
        error: function(request, error) {
            $("#instructionsTextArea").val("Error obteniendo datos del servidor: \n" + error);
            $("#map").fadeOut(1000);
        }
    });
}

function getParams() {
    var p = window.location.href;
    return p.slice(p.indexOf('?'));
}

function drawResult(result) {
    
    var map = $("#map");
    
    if(result.url && result.narration) {
        
        map.css("opacity", "0");
        
        setTimeout(function() {
            map.removeClass().addClass("resultMap center-block");
            map.attr("src", result.url);
            map.css("opacity", "1");
        }, 1000);

        $("#instructionsTextArea").val(result.narration);
        
    } else if(result.error) {
        map.fadeOut(1000);
        $("#instructionsTextArea").val("Error: " + result.error);
    } else {
        $("#instructionsTextArea").val("No se puede encontrar la ruta especificada o la información recibida del servidor contiene errores.");
    }
}