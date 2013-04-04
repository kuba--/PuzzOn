<!--                                                      -->
<!-- Copyright (c) 2010 PuzzOn.net                        -->
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <%@ include file="/head.jsp" %>
        <link rel="stylesheet" type="text/css" href="/content.css">
        <script language="JavaScript" type="text/javascript">
            
            function loadTweets(){
            
                var timestamp = new Date().getTime();
                
                // [0] The name of the callback function that will get called.
                var callback = "twitter_callback_" + timestamp;
                
                // [1] Create a script element.
                var script = document.createElement("script");
                script.setAttribute("src", "https://api.twitter.com/1/statuses/user_timeline/puzzon.json?count=3&callback=" + callback);
                script.setAttribute("type", "text/javascript");
                
                // [2] Add a callback function on the window object.
                window[callback] = function(jsonObj){
                
                    // [3] Call the handleJsonResponseMethod.
                    handleTweets(jsonObj);
                    window[callback + "done"] = true;
                };
                
                // [4] Wait up to 5 seconds for the call to return.
                setTimeout(function(){
                    if (!window[callback + "done"]) {
                        handle_tweets(null);
                    }
                    
                    // [5] Cleanup. Remove script and callback elements.
                    document.body.removeChild(script);
                    //delete window[callback];
                    //delete window[callback + "done"];
                }, 5000);
                
                // [6] Attach the script element to the document body.
                document.body.appendChild(script);
            }
            
            function handleTweets(jsonObj){
            
                if (null == jsonObj) 
                    return;
                
                for (var i = 0; i < jsonObj.length; ++i) {
                    displayTweet(jsonObj[i]);
                }
            }
            
            function displayTweet(tweet){
                var screen_name = tweet.user.screen_name;
                var created_at = tweet.created_at;
                var txt = tweet.text;
                
                var root = document.getElementById("root");
                
                var widget = document.createElement("div");
                widget.className = "widget";
                
                var header = document.createElement("div");
                header.className = "header";
                
                var footer = document.createElement("div");
                footer.className = "footer";
                
                var content = document.createElement("div");
                content.className = "content";
                content.innerHTML = "<p>" + "<b><a href='http://twitter.com/" + screen_name + "'>" + screen_name + "</a></b> " + txt + "</p>" + "<h5>" + created_at + "</h5>";
                
                widget.appendChild(header);
                widget.appendChild(content)
                widget.appendChild(footer);
                
                root.appendChild(widget);
            }

        </script>
    </head>
    <body onload="loadTweets()">
        <center>
            <div id="root">
                <%@ include file="/menu.jsp.shtml" %>
                <div class="widget">
                    <div class="header" ></div>
                    <%=resources.getSourceContent("/tweets/tweets.shtml")%>
                    <div class="footer" ></div>
                </div>
            </div>
        </center>
    </body>
</html>
