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
    <script src="https://cdn.bootcss.com/sockjs-client/1.1.4/sockjs.min.js"></script>
    <script src="https://cdn.bootcss.com/stomp.js/2.3.3/stomp.js"></script>
    <script type="text/javascript">
         var stompClient = null;

         function setConnected(connected) {
             document.getElementById('connect').disabled = connected;
             document.getElementById('disconnect').disabled = !connected;
             document.getElementById('conversationDiv').style.visibility =  connected ? 'visible' : 'hidden';
             document.getElementById('response').innerHTML = '';
         }

         function connect() {
             var socket = new SockJS('/hello');
             stompClient = Stomp.over(socket);
             stompClient.connect({login: "guest"}, function(frame) {
                 setConnected(true);
//                 console.log('Connected: ' + frame);
                 stompClient.subscribe('/topic/greetings', function(greeting){
                     showGreeting(JSON.parse(greeting.body).content);
                 });
                 // starting line.
                 stompClient.subscribe('/app/macro',function(shout){
                     showGreeting(JSON.parse(shout.body).message);
                 }); // ending line. attention for addr '/app/macro' in client.
                 stompClient.subscribe('/topic/spittlefeed',function(shout){
                     showGreeting(JSON.parse(shout.body).message);
                 });
                 stompClient.subscribe('/user/queue/notifications',function(greeting){
                     showGreeting(JSON.parse(greeting.body).content);
                 });
             });
         }

         function connectAny() {
             var socket = new SockJS('/hello');
             stompClient = Stomp.over(socket);
             stompClient.connect({login: "guest2"}, function(frame) {
                 setConnected(true);
                 stompClient.subscribe('/user/user/queue/notifications',function(greeting){
                     showGreeting(JSON.parse(greeting.body).content);
                 });
             });
         }

         function disconnect() {
             if (stompClient != null) {
                 stompClient.disconnect();
             }
             setConnected(false);
             console.log("Disconnected");
         }

         function sendName() {
             var name = document.getElementById('name').value;
             stompClient.send("/app/hello", {}, JSON.stringify({ 'name': name }));
         }

         function sendToSelf() {
             var name = document.getElementById('name').value;
             stompClient.send("/app/spittle/self", {}, JSON.stringify({ 'name': name }));
         }

         function sendToOther() {
             var name = document.getElementById('name').value;
             stompClient.send("/app/spittle/other", {}, JSON.stringify({ 'name': name }));
         }

         function showGreeting(message) {
             var response = document.getElementById('response');
             var p = document.createElement('p');
             p.style.wordWrap = 'break-word';
             p.appendChild(document.createTextNode(message));
             response.appendChild(p);
         }

    </script>
</head>

<body>
    <noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being enabled. Please enable
        Javascript and reload this page!</h2></noscript>
    <div>
        <div>
            <button id="connect" onclick="connect();">Connect</button>
            <button id="connectAny" onclick="connectAny();">ConnectAny</button>
            <button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button>
        </div>
        <div id="conversationDiv">
            <label>What is your name?</label><input type="text" id="name" />
            <button id="sendName" onclick="sendName();">Send</button>
            <button id="sendToSelf" onclick="sendToSelf();">SendToSelf</button>
            <button id="sendToOther" onclick="sendToOther();">SendToOther</button>
            <p id="response"></p>
        </div>
    </div>
</body>
</html>