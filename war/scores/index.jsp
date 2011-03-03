<!--                                                      -->
<!-- Copyright (c) 2011 PuzzOn.net                        --> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <%@ include file="/head.jsp" %>
        <link rel="stylesheet" type="text/css" href="/content.css">
        <style type="text/css">
            .image-puzzle-item {
                border-style: none;
                width: 50px;
                height: 50px;
            }
            
            .image-sum {
                width: 30px;
                height: 30px;
                border-style: none;
            }
            
            .score-puzzle-item {
                color: #fff;
                font-weight: bold;
                font-style: normal;
                text-shadow: 6px 6px 6px #ccc;
                padding-left: 30px;
            }
            
            .td-puzzle-item {
                align: left;
                valign: center;
            }
            
            #widget-scores {
                display:none;
            }
        </style>
        <script type="text/javascript" language="JavaScript" src="/yui/utilities.js">
        </script>
        <script type="text/javascript" language="JavaScript" src="/arena/stopper-min.js">
        </script>
        <script type="text/javascript" language="JavaScript" src="/arena/json-min.js">
        </script>

        <script type="text/javascript" language="JavaScript">
            
            function loadScores(){
                var callback = {
                    success: handleSuccess,
                    failure: handleFailure
                };
                
                var url = "/api/score";
                YAHOO.util.Connect.asyncRequest("GET", url, callback, null);
            }
            
            function handleSuccess(resp){
                try {
                    var content = document.getElementById("content-scores");
                    var scores = JSON.parse(resp.responseText);
                    var n = scores.length;
                    if (n > 0) {
                    
                        var sum = 0;
                        for (var i = 0; i < n; ++i) {
                            var score = scores[i];
                            
                            content.appendChild(contentTr(score.id, score.ticks));
                            sum += score.ticks;
                        }
                                                
                        setContentSum(sum);
                    }  
                    
                     var ws = document.getElementById("widget-scores");
                     if (ws) ws.style.display = "block";                  
                } 
                catch (e) {
                    window.location.replace('/?locale=' + '<%=resources.getLocale() %>');
                }
            }
            
            function contentTr(id, ticks){
                var img = document.createElement("img");
                img.src = "/images/puzzle-item-" + id + ".png";
                img.className = "image-puzzle-item";
                
                var txt = document.createElement("span");
                txt.className = "score-puzzle-item";
                txt.innerHTML = Stopper.toStringTime(ticks);
                
                var tr = document.createElement("tr");
                
                var td1 = document.createElement("td");
                td1.className = "td-puzzle-item";
                td1.appendChild(img);
                
                var td2 = document.createElement("td");
                td2.className = "td-puzzle-item";
                td2.appendChild(txt);
                
                tr.appendChild(td1);
                tr.appendChild(td2);
                
                return tr;
            }
            
            function setContentSum(sum){
                var txt = document.getElementById("sum");
                txt.innerHTML = Stopper.toStringTime(sum);                
            }
            
            function handleFailure(obj){
                var ws = document.getElementById("widget-scores");
                if (ws) {
                    var wsc = document.getElementById("widget-scores-content");
                    wsc.innerHTML = '<%=resources.getText("scores.noscores") %>';
                    
                    ws.style.display = "block";   
                }                               
            }
        </script>
    </head>
    <body onload="loadScores()">
        <center>
            <div id="root"">
                <%@ include file="/menu.jsp.shtml" %>
                <div class="widget">
                    <div class="header">
                    </div>
                    <%=resources.getSourceContent("/scores/scores.shtml")%>
                    <div class="footer">
                    </div>
                </div>
                <div id="widget-scores" class="widget">
                    <div class="header">
                    </div>
                    <div id="widget-scores-content" class="content">
                        <table cellspacing="10px" cellpadding="5px">
                            <tbody id="content-scores">                                
                            </tbody>
                        </table>
                        <hr>
                        <table cellspacing="10px" cellpadding="5px">
                            <tbody>
                                <tr>
                                    <td align="center" valign="center" width="50">
                                        <img src="/images/sigma-inv.png" class="image-sum">
                                    </td>
                                    <td class="td-puzzle-item">
                                        <span id="sum" class="score-puzzle-item"></span>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="footer">
                    </div>
                </div>
            </div>
        </center>
    </body>
</html>
