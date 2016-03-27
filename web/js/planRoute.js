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

var oStreet = $("#oStreet"),
    dStreet = $("#dStreet"),
    oAv = $("#oAv"),
    dAv = $("#dAv");

$(document).ready(main);
$("#backButton").click(window.goToRoot);

function main() {
    'use strict';
    
    $("#routeForm").submit(validateForm);
    
    clearAllSelects();
    
    loadStreets(oStreet);
    loadStreets(dStreet);
    
    $(oStreet).change(function(){ onStreetChange(oStreet, oAv); });
    $(dStreet).change(function(){ onStreetChange(dStreet, dAv); });
}

function validateForm() {
    'use strict';
    
    if(oStreet.val() === "none" || dStreet.val() === "none" || oAv.val() === "none" || dAv.val() === "none") {
        
        window.alert("Debe seleccionar Calle y Avenida en ambos puntos.");
        return false;
    }
}

function clearAllSelects() {
    'use strict';
    
    clearAv(oAv);
    clearAv(dAv);
    clearStrt(oStreet);
    clearStrt(dStreet);
}

function clearAv(select) {
    'use strict';
    window.clearSelect(select, "--Avenida--");
}

function clearStrt(select) {
    'use strict';
    window.clearSelect(select, "--Calle--");
}

function loadStreets(select) {
    'use strict';
    
    clearStrt(select);
    
    $.ajax({
        url : "streets",
        method: "GET"
    })
    .done(function(list) {
        
        for(var i = 0; i < list.length; i++) {
            select.append('<option value="' + list[i] + '">' + list[i] + '</option>');
        }
    })
    .fail(function(request, error) {
        window.alert("Error: No se pudo obtener la lista de calles. Razón: " + error.message);
    });
}

function loadAvenues(select, street) {
    'use strict';
    
    clearAv(select);
    
    $.ajax({
        url : "avenues" + '?street=' + street + '',
        method: "GET"
    })
    .done(function(list) {
        
        for(var i = 0; i < list.length; i++) {
            select.append('<option value="' + list[i] + '">' + list[i] + '</option>');
        }
    })
    .fail(function(request, error) {
        window.alert("Error: No se pudo obtener la lista de calles. Razón: " + error.message);
    });
}

function onStreetChange(select, dependant) {
    'use strict';
    
    if(select.val() === "none") {
        dependant.prop('disabled', 'disabled');
        window.clearSelect(dependant);
    }
    else {
        dependant.prop('disabled', false);
        loadAvenues(dependant, select.val());
    }
}