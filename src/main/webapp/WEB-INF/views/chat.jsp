<%@ page import="com.netty.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
User user = (User) request.getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>聊天室 - editor:yinq</title>
    <link rel="shortcut icon" href="/static/img/favicon.png">
    <link rel="icon" href="/static/img/favicon.png" type="image/x-icon">
    <link type="text/css" rel="stylesheet" href="/static/css/chatstyle.css">
    <script type="text/javascript" src="/static/js/jquery-3.2.1.min.js"></script>
</head>

<body>
<div class="chatbox">
    <div class="chat_top fn-clear">
        <div class="logo"><img src="/static/img/logo.png" width="190" height="60"  alt=""/></div>
        <div class="uinfo fn-clear">
            <div class="uface"><img src="/static/img/${user.img}" width="40" height="40"  alt=""/></div>
            <div class="uname">
                ${user.name}<i class="fontico down"></i>
                <ul class="managerbox">
                    <li><a href="#"><i class="fontico lock"></i>修改密码</a></li>
                    <li><a href="#"><i class="fontico logout"></i>退出登录</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="chat_message fn-clear">
        <div class="chat_left">
            <div class="message_box" id="message_box">

            </div>
            <div class="write_box">
                <textarea id="message" name="message" class="write_area" placeholder="说点啥吧..."></textarea>
                <input type="hidden" name="fromname" id="fromname" value="${user.name}" />
                <input type="hidden" name="to_uid" id="to_uid" value="0">
                <div class="facebox fn-clear">
                    <div class="expression"></div>
                    <div class="chat_type" id="chat_type">群聊</div>
                    <button name="" class="sub_but">提 交</button>
                </div>
            </div>
        </div>
        <div class="chat_right">
            <ul class="user_list" title="双击用户私聊">
                <li class="fn-clear selected"><em>所有用户</em></li>
                <li class="fn-clear" data-id="1"><span><img src="/static/img/xuwei.jpg" width="30" height="30"  alt=""/></span><em>徐威</em><small class="online" title="在线"></small></li>
                <li class="fn-clear" data-id="2"><span><img src="/static/img/wangliucheng.jpg" width="30" height="30" alt=""/></span><em>王留成</em><small class="online" title="在线"></small></li>
                <li class="fn-clear" data-id="3"><span><img src="/static/img/tongyong.jpg" width="30" height="30"  alt=""/></span><em>向明亨</em><small class="offline" title="离线"></small></li>
                <li class="fn-clear" data-id="3"><span><img src="/static/img/tongyong.jpg" width="30" height="30"  alt=""/></span><em>任</em><small class="offline" title="离线"></small></li>
            </ul>
        </div>
    </div>
</div>

<script type="text/javascript">
    if(window.WebSocket) {
        var socket;
    }else{
        alert("您的浏览器不支持WebSocket协议！");
    }
    $(document).ready(function(e) {
        socket = new WebSocket("ws://127.0.0.1:8012/websocket");
        socket.onmessage = function(event){
            var blob = event.data;
            console.log(blob);
             var json =  eval('(' + blob + ')');
             var htmlData =   '<div class="msg_item fn-clear">'
             + '   <div class="uface"><img src="/static/img/'+ json.img + '"  width="40" height="40"  alt=""/></div>'
             + '   <div class="item_right">'
             + '     <div class="msg own">' + json.msg + '</div>'
             + '     <div class="name_time">' + json.name + '</div>'
             + '   </div>'
             + '</div>';
             $("#message_box").append(htmlData);
             $('#message_box').scrollTop($("#message_box")[0].scrollHeight + 20);

        };
        socket.onopen = function(event){
            console.log("onopen");
        };
        socket.onclose = function(event){
            console.log("onclose");
        };
        socket.onerror = function(event){
            console.log("onerror");
        };


        $('#message_box').scrollTop($("#message_box")[0].scrollHeight + 20);
        $('.uname').hover(
                function(){
                    $('.managerbox').stop(true, true).slideDown(100);
                },
                function(){
                    $('.managerbox').stop(true, true).slideUp(100);
                }
        );

        var fromname = $('#fromname').val();
        var to_uid   = 0; // 默认为0,表示发送给所有用户
        var to_uname = '';
        $('.user_list > li').dblclick(function(){
            alert("未开发");
            return;
            to_uname = $(this).find('em').text();
            to_uid   = $(this).attr('data-id');
            if(to_uname == fromname){
                alert('您不能和自己聊天!');
                return false;
            }
            if(to_uname == '所有用户'){
                $("#toname").val('');
                $('#chat_type').text('群聊');
            }else{
                $("#toname").val(to_uid);
                $('#chat_type').text('您正和 ' + to_uname + ' 聊天');
            }
            $(this).addClass('selected').siblings().removeClass('selected');
            $('#message').focus().attr("placeholder", "您对"+to_uname+"说：");
        });

        $('.sub_but').click(function(event){
            sendMessage(event, fromname, to_uid, to_uname);
        });
        //enter键发送内容
        document.onkeydown=function(){
            if (event.ctrlKey && event.keyCode == 13) {
                $("#message").val($("#message").val() + '\n');
                return;
            }
            if (event.keyCode == 13){
                event.cancelBubble=true;
                event.preventDefault();
                event.stopPropagation();
                if($("#message").val() == ""){
                    $("#message").val() == "";
                    alert("发送内容不可为空！");
                    return;
                }else{
                    sendMessage(event, fromname, to_uid, to_uname);
                }
            }
        }

    });
    function sendMessage(event, from_name, to_uid, to_uname){
        var msg = $("#message").val();
        if(to_uname != ''){
            msg = '您对 ' + to_uname + ' 说： ' + msg;
        }
        //msg.replace("/r/n","'<br/>'");
        var htmlData =   '<div class="msg_item fn-clear">'
                         + '   <div class="uface"><img src="/static/img/${user.img}" width="40" height="40"  alt=""/></div>'
                         + '   <div class="item_right">'
                         + '     <div class="msg own">' + msg + '</div>'
                         + '     <div class="name_time">' + from_name + ' </div>'
                         + '   </div>'
                         + '</div>';
        $("#message_box").append(htmlData);
        $('#message_box').scrollTop($("#message_box")[0].scrollHeight + 20);
        $("#message").val('');
        socket.send("{'msg':'" + msg + "','img':'${user.img}','name':'${user.name}'}");
    }
</script>
</body>
</html>
