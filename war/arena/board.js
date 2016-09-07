//                                                      
//Copyright (c) 2010 PuzzOn.net
//

// Global Board object
var Board = {
    // members
    width: 500,
    height: 500,
    
    element: null,
    
    grid: null,
    board: null,
    
    polyform: null, // { id: null, polys: [ { name: null, points: [] } ] };
    hover_poly: null,
    hover_poly_is_dragging: false,
    
    anchor_xy: null, // { x: null, y: null };
    orig_xy: null, // { x: null, y: null };
    is_snapping_on: true,
    is_puzzling: false,
    
    validateCallback: null,
    
    id: null
};

// initialize
Board.init = function(){
    var element_grid = document.getElementById("grid");
    Board.grid = element_grid.getContext("2d");
    
    Board.element = document.getElementById("board");
    Board.width = Board.element.width;
    Board.height = Board.element.height;
    Board.board = Board.element.getContext("2d");
    
    Board.element.onmousemove = Board.onMouseMove;
    Board.element.onmousedown = Board.onMouseDown;
    Board.element.onmouseup = Board.onMouseUp;
    Board.element.onmouseout = Board.onMouseOut;
    //Board.element.onmouseleave = null;
};

Board.startPuzzling = function(){
    Board.drawGrid();
    Board.id = window.setInterval("Board.drawPolyform()", 30);
    
    Board.is_puzzling = true;
};

Board.stopPuzzling = function(){

    Board.is_puzzling = false;
    
    Board.element.onmousemove = null;
    Board.element.onmousedown = null;
    Board.element.onmouseup = null;
    Board.element.onmouseout = null;
    Board.element.onmouseleave = null;
    
    window.clearInterval(Board.id);
};

Board.drawGrid = function(){
    Board.grid.lineWidth = 5;
    Board.grid.strokeStyle = "rgba(220, 220, 220, 1)";
    Board.grid.strokeRect(0, 0, Board.width, Board.height);
    
    Board.grid.fillStyle = "rgba(220, 220, 220, 0.5)";
    for (var x = 50; x < Board.width; x += 50) {
        Board.grid.fillRect(x - 1, 0, 2, Board.height);
    }
    
    for (var y = 50; y < Board.height; y += 50) {
        Board.grid.fillRect(0, y - 1, Board.width, 2);
    }
};

Board.drawPolyform = function(){
    Board.clear();
    
    var n = Board.polyform.polys.length;
    for (var i = 0; i < n; ++i) {
        var poly = Board.polyform.polys[i];
        Board.drawPoly(poly);
    }
    
    if (null != Board.anchor_xy) {
        Board.drawAnchorXY();
    }
};

Board.clear = function(){
    Board.board.clearRect(0, 0, Board.width, Board.height);
};

Board.drawPoly = function(poly){
    Board.board.beginPath();
    Board.board.fillStyle = (null != Board.hover_poly && poly.name === Board.hover_poly.name) ? "rgba(0, 150, 250, 0.7)" : "rgba(10, 100, 200, 0.7)";
    Board.board.strokeStyle = "rgba(10, 100, 250, 0.9)";
    Board.board.lineCap = "round";
    Board.board.lineJoin = "round";
    Board.board.lineWidth = 2;
    
    var points = poly.points;
    var n = points.length;
    
    Board.board.moveTo(points[n - 1].x, points[n - 1].y);
    for (var i = 0; i < n; ++i) {
        Board.board.lineTo(points[i].x, points[i].y);
    }
    
    Board.board.stroke();
    Board.board.fill();
};

Board.drawAnchorXY = function(){
    var x = Board.anchor_xy.x;
    var y = Board.anchor_xy.y;
    var r = 9;
    
    Board.board.beginPath();
    Board.board.strokeStyle = "rgba(110, 110, 110, 0.7)";
    Board.board.lineWidth = 2;
    Board.board.arc(x, y, r, 0.0, 2.0 * Math.PI, true);
    Board.board.stroke();
    
    // draw arrows            
    var a = r * 0.5;
    var h = a * Math.sqrt(3);
    
    Board.board.beginPath();
    Board.board.fillStyle = "rgba(110, 110, 110, 1)";
    Board.board.lineWidth = 1;
    // arrow - up
    Board.board.moveTo(x + r - a, y);
    Board.board.lineTo(x + r + a, y);
    Board.board.lineTo(x + r, y - h);
    // arrow - down
    Board.board.moveTo(x - r - a, y);
    Board.board.lineTo(x - r + a, y);
    Board.board.lineTo(x - r, y + h);
    
    Board.board.fill();
};

// event handlers
Board.getRelativePosition = function(e){
    var evt = (window.event) ? window.event : e;
    
    if (undefined != evt.layerX && undefined != evt.layerY) 
        return {
            x: evt.layerX,
            y: evt.layerY
        };
    
    if (undefined != evt.x && undefined != evt.y) 
        return {
            x: evt.x,
            y: evt.y
        };
    
    return {
        x: evt.offsetX,
        y: evt.offsetY
    };
};

// mouse handlers
Board.onMouseMove = function(e){
    if (!Board.is_puzzling) 
        return;
    
    var xy = Board.getRelativePosition(e);
    
    if (Board.hover_poly_is_dragging) {
        if (null != Board.anchor_xy) {
            var c = Board.getPolyCenter(Board.hover_poly);
            var theta = Board.calculateTheta(xy, c);
            
            Board.rotatePoly(Board.hover_poly, c, theta);
            
        }
        else {
            // translate clicked poly
            var t = {
                x: xy.x - Board.orig_xy.x,
                y: xy.y - Board.orig_xy.y
            };
            
            if (t.x != 0 || t.y != 0) 
                Board.translatePoly(Board.hover_poly, t);
        }
    }
    else {
        Board.calculateHoverAndAnchor(xy);
    }
    
    Board.orig_xy = xy;
};

Board.onMouseDown = function(e){
    if (!Board.is_puzzling) 
        return;
    
    Board.orig_xy = Board.getRelativePosition(e);
    Board.hover_poly_is_dragging = (null != Board.hover_poly);
};

Board.onMouseUp = function(e){
    if (!Board.is_puzzling) 
        return;
    
    if (Board.hover_poly_is_dragging && Board.is_snapping_on) {
        Board.snapPoly(Board.hover_poly);
    }
    
    Board.validatePolyform();
    
    Board.hover_poly_is_dragging = false;
    Board.orig_xy = Board.getRelativePosition(e);
};

Board.onMouseOut = function(){
    if (!Board.is_puzzling) 
        return;
    
    Board.hover_poly = null;
    Board.hover_poly_is_dragging = false;
    Board.anchor_xy = null;
};

// poly handlers
Board.getPolyCenter = function(poly){
    var c = {
        x: 0,
        y: 0
    };
    var sx = 0;
    var sy = 0;
    var n = poly.points.length;
    
    for (var i = 0; i < n; ++i) {
        sx += poly.points[i].x;
        sy += poly.points[i].y;
    }
    
    if (n > 0) {
        c.x = sx / n;
        c.y = sy / n;
    }
    
    return c;
};

Board.rotatePoly = function(poly, c, theta){
    var n = poly.points.length;
    
    // check if poly after rotation is *not* out of Board range.    
    for (var i = 0; i < n; ++i) {
        var xy = Math.rotateXY(poly.points[i], c, theta);
        if (!Board.contains(xy)) 
            return;
    }
    
    // actual rotation
    for (var i = 0; i < n; ++i) {
        poly.points[i] = Math.rotateXY(poly.points[i], c, theta);
    }
    
    Board.anchor_xy = Math.rotateXY(Board.anchor_xy, c, theta);
};

Board.translatePoly = function(poly, t){
    var n = poly.points.length;
    
    // check if poly after tranlation is *not* out of Board range.    
    for (var i = 0; i < n; ++i) {
        var xy = Math.translateXY(poly.points[i], t);
        if (!Board.contains(xy)) 
            return;
    }
    
    // actual translation
    for (var i = 0; i < n; ++i) {
        poly.points[i] = Math.translateXY(poly.points[i], t);
    }
};

Board.calculateTheta = function(xy, c){
    var ux = Board.orig_xy.x - c.x;
    var uy = Board.orig_xy.y - c.y;
    var vx = xy.x - c.x;
    var vy = xy.y - c.y;
    
    var alpha = Math.atan2(uy, ux);
    var beta = Math.atan2(vy, vx);
    var theta = beta - alpha;
    if (theta < 0.0) {
        theta = 2.0 * Math.PI + theta;
    }
    
    return (beta > alpha) ? -theta : theta;
};

Board.calculateHoverAndAnchor = function(xy){
    Board.hover_poly = null;
    Board.anchor_xy = null;
    
    var n = Board.polyform.polys.length;
    
    for (var i = 0; i < n; ++i) {
        var poly = Board.polyform.polys[i];
        
        if (Board.polyContains(poly, xy)) {
            Board.hover_poly = poly;
        }
        
        var nn = poly.points.length;
        for (var ii = 0; ii < nn; ++ii) {
            if (Math.distXY(poly.points[ii], xy) < 9) {
                Board.anchor_xy = poly.points[ii];
                break;
            }
        }
        
        if ((null != Board.anchor_xy) && (null == Board.hover_poly)) {
            Board.hover_poly = poly;
        }
        
        if (null != Board.hover_poly) {
            return;
        }
    }
};

Board.contains = function(xy){
    return (xy.x > 0) && (xy.x < Board.width) && (xy.y > 0) && (xy.y < Board.height);
};

Board.polyContains = function(poly, xy){
    var n = poly.points.length;
    var b = 0;
    
    for (var i = 0, j = n - 1; i < n; j = i++) {
        var b1 = (poly.points[i].y <= xy.y) && (poly.points[j].y > xy.y);
        var b2 = (poly.points[j].y <= xy.y) && (poly.points[i].y > xy.y);
        var d = (((poly.points[j].x - poly.points[i].x) * (xy.y - poly.points[i].y)) / (poly.points[j].y - poly.points[i].y)) + poly.points[i].x;
        
        if ((b1 || b2) && (xy.x < d)) 
            b = 1 - b;
    }
    
    return (1 === b);
};

Board.snapPoly = function(poly){
    var t = Board.getPolySnapXY(poly);
    
    if (null != t) {
        Board.translatePoly(poly, t);
    }
};

Board.getPolySnapXY = function(poly){
    var xy;
    var d = Math.max(Board.width, Board.height);
    
    var max_i = poly.points.length;
    for (var i = max_i - 1, ii = 0; ii < max_i; i = ii, ++ii) {
        var xy11 = poly.points[i], xy12 = poly.points[ii];
        
        var max_j = Board.polyform.polys.length;
        for (var j = 0; j < max_j; ++j) {
            var poly_j = Board.polyform.polys[j];
            if (poly.name == poly_j.name) 
                continue;
            
            var max_k = poly_j.points.length;
            for (var k = max_k - 1, kk = 0; kk < max_k; k = kk, ++kk) {
                var xy21 = poly_j.points[k], xy22 = poly_j.points[kk];
                
                
                var tmp_xy = Board.getSnapXY(xy11, xy12, xy21, xy22);
                if (null != tmp_xy) {
                    var tmp_d = Math.lengthXY(tmp_xy);
                    if (tmp_d < d) {
                        d = tmp_d;
                        xy = tmp_xy;
                    }
                }
            }
        }
    }
    
    return (d < 9) ? xy : null;
};

Board.getSnapXY = function(xy11, xy12, xy21, xy22){
    var xy1 = {
        x: xy12.x - xy11.x,
        y: xy12.y - xy11.y
    };
    var xy2 = {
        x: xy22.x - xy21.x,
        y: xy22.y - xy21.y
    };
           
    if (Math.liesInXY(xy11, xy12, xy21, xy22) && Math.areParallelXY(xy1, xy2)) {
        var v = {
            x: xy21.x - xy11.x,
            y: xy21.y - xy11.y
        };
        var k = Math.dotXY(v, xy1) / Math.dotXY(xy1, xy1)
        var u = {
            x: k * xy1.x,
            y: k * xy1.y
        };
        
        var xy = {
            x: xy11.x + u.x,
            y: xy11.y + u.y
        };
        
        var t = {
            x: xy21.x - xy.x,
            y: xy21.y - xy.y
        };
        return t;
    }
    return null;
};

Board.validatePolyform = function(){

    var callback = {
        success: function(resp){
            Board.stopPuzzling();
            if (Board.validateCallback) Board.validateCallback(resp);
            
        },
        failure: function(obj){
        
        },
        argument: [Board.polyform.id]
    };
    
    YAHOO.util.Connect.asyncRequest("POST", "/api/valid", callback, "polyform=" + JSON.stringify(Board.polyform));
};

