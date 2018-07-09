<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>web socket</title>

    <link href="https://cdn.bootcss.com/bootstrap/4.1.1/css/bootstrap.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/4.1.1/js/bootstrap.js"></script>
    <script src="http://cdn.sockjs.org/sockjs-0.3.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            websocket_client();
        });

        function websocket_client() {
            var hostaddr = window.location.host + "/websocket/p2ptext";
            var url = 'p2ptext';
            var sock = new SockJS(url);

// 以下的 open(), onmessage(), onclose()
// 对应到 ChatTextHandler 的
// afterConnectionEstablished(), handleTextMessage(), afterConnectionClosed();

            sock.onopen = function() {
                alert("open successfully.");
                sayMarco();
            };

            sock.onmessage = function(e) {
                alert("onmessage");
                alert(e.data);
            };

            sock.onclose = function() {
                alert("close");
            };

            function sayMarco() {
                sock.send("this is the websocket client.");
            }
        }
    </script>
</head>

<body>
<div id="websocket">
    websocket div.
</div>
</body>
</html>