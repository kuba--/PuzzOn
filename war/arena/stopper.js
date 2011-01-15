//                                                      
//Copyright (c) 2010 PuzzOn.net
//

var Stopper = {
    // members
    element: null,
    ticks: 0,
    id: null
};

Stopper.start = function(){
    Stopper.element = document.getElementById("stopper");
    Stopper.id = window.setInterval("Stopper.updateInnerHTML()", 1000);
};

Stopper.stop = function(){
    window.clearInterval(Stopper.id);
};

Stopper.updateInnerHTML = function(){
    ++Stopper.ticks;
    var s = Stopper.ticks % 60;
    var m = (Stopper.ticks / 60) % 60;
    var h = Stopper.ticks / 3600;
    
    Stopper.element.innerHTML = Stopper.getDoubleDigit(h) + ":" + Stopper.getDoubleDigit(m) + ":" + Stopper.getDoubleDigit(s);
};

Stopper.getDoubleDigit = function(d){
    intd = Math.floor(d);
    if (intd >= 0 && intd < 10) 
        return "0" + intd.toString();
    return intd.toString();
};
