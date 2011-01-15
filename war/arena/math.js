//                                                      
//Copyright (c) 2010 PuzzOn.net
//

var Math = Math || {};

Math.detXY = Math.detXY ||
function(x1, y1, x2, y2){
    return x1 * y2 - y1 * x2;
};

Math.dotXY = Math.dotXY ||
function(xy1, xy2){
    return xy1.x * xy2.x + xy1.y * xy2.y;
};

Math.lengthXY = Math.lengthXY ||
function(xy){
    return Math.sqrt(Math.dotXY(xy, xy));
};

Math.distXY = Math.distXY ||
function(xy1, xy2){
    var xy = {
        x: xy2.x - xy1.x,
        y: xy2.y - xy1.y
    };
    
    return Math.lengthXY(xy);
};

Math.unitXY = Math.unitXY ||
function unitXY(xy){
    var l = Math.lengthXY(xy);
    
    if (l == 0.0) 
        l = 1.0;
    
    return {
        x: xy.x / l,
        y: xy.y / l
    };
};

Math.areParallelXY = Math.areParallelXY ||
function(xy1, xy2){
    
    var u1 = Math.unitXY(xy1);
    var u2 = Math.unitXY(xy2);
    
    var d = Math.abs(Math.detXY(u1.x, u1.y, u2.x, u2.y));
    
    return d < 0.5;
};

Math.inRange = Math.inRange ||
function(min, max, val) {
    return (val > min && val < max);
}

Math.liesInXY = Math.liesInXY ||
function(xy11, xy12, xy21, xy22){
    var x11 = Math.min(xy11.x, xy12.x);
    var x12 = Math.max(xy11.x, xy12.x);
    var x21 = Math.min(xy21.x, xy22.x);
    var x22 = Math.max(xy21.x, xy22.x);
    
    if (Math.inRange(x11, x12, x21) || Math.inRange(x11, x12, x22) ||
              Math.inRange(x21, x22, x11) || Math.inRange(x21, x22, x12))
              return true;
                  
    var y11 = Math.min(xy11.y, xy12.y);
    var y12 = Math.max(xy11.y, xy12.y);
    var y21 = Math.min(xy21.y, xy22.y);
    var y22 = Math.max(xy21.y, xy22.y);
                      
    if (Math.inRange(y11, y12, y21) || Math.inRange(y11, y12, y22) ||
              Math.inRange(y21, y22, y11) || Math.inRange(y21, y22, y12))
              return true;
    
    return false;              
};

Math.rotateXY = Math.rotateXY ||
function(xy, center, theta){
    var x = xy.x - center.x;
    var y = xy.y - center.y;
    var cos = Math.cos(theta);
    var sin = Math.sin(theta);
    
    return {
        x: center.x + (x * cos + y * sin),
        y: center.y + (-x * sin + y * cos)
    };
};

Math.translateXY = Math.translateXY ||
function translateXY(xy, t){
    return {
        x: xy.x + t.x,
        y: xy.y + t.y
    };
};
