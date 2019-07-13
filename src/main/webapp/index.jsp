<%@ page import="com.netty.websocket.WebSocketServer" %>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html lang="en">
<head>
    <meta http-equiv=”Content-Type” content=”text/html; charset=utf-8″>
    <title>登录</title>
    <link rel="stylesheet" href="/static/css/style.css" />
    <link href='http://fonts.googleapis.com/css?family=Oleo+Script' rel='stylesheet' type='text/css'>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
</head>
<body>
<div class="lg-container">
    <h1>门户入口</h1>
    <form id="lg-form" name="lg-form">
        <div>
            <label for="username">Usernamehehe:</label>
            <input type="text" name="username" id="username" placeholder="用户名hahahehe"/>
        </div>
        <div>
            <label for="password">Passwordhehe:</label>
            <input type="password" name="password" id="password" placeholder="密码190713hehe" />
        </div>
        <div>
            <button type="button" id="login">登录190713</button>
        </div>
    </form>
    <div id="message"></div>
</div>

<script type="text/javascript">
    $(document).ready(function(){
        $("#login").click(function(){
            var form_data = {
                username: $("#username").val(),
                password: $("#password").val(),
            };
            $.ajax({
                type: "POST",
                url: "/user/login",
                data: form_data,
                success: function(response){
                    if(response.code == 200){
                        window.location.href = "/user/chat/" + response.result;
                    } else{
                        $("#message").html('<p class="error">账号或密码错误!</p>');
                    }
                }
           });
        });
    });
</script>

</body>
</html>