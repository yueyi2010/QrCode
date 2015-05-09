<%--
  Created by IntelliJ IDEA.
  User: chenpeng07
  Date: 2015/4/30
  Time: 17:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<p>
    <strong>
        1.批量生成二维码
    </strong>
</p>

<form action="generate_qrs" method="get">
    <p>
        生成个数(num): <input type="text" name="num"/>
    </p>
    <p>
        激活次数(allow): <input type="text" name="allow" value="5"/>
    </p>
    <!--
    <p>
        二维码/激活码(type):
        <input type="radio" name="type" value="0" checked="checked"/>二维码(0)
        <input type="radio" name="type" value="1" />激活码(1)
    </p>
    -->
    <p>
        二维码生成路径(path): <input type="text" name="path"/>
    </p>
    <input type="submit" value="提交"/>
</form>
<p>
    <strong>
        2.主动激活接口
    </strong>
</p>
<form action="qr" method="get">
    <p>
        用户(id): <input type="text" name="id"/>
    </p>

    <p>
        二维码(qr): <input type="text" name="qr"/>
    </p>
    <input type="submit" value="提交"/>
</form>
<strong>
    3.被动激活检测
</strong>

<form action="qr" method="get">
    <p>
        用户(id): <input type="text" name="id"/>
    </p>
    <input type="submit" value="提交"/>
</form>
<p>
    <strong>
        4.获取激活用户ID
    </strong>
</p>
<a href="ids">获取</a>

<p>
    <strong>
        5.客户反馈
    </strong>
</p>

<form action="fb" method="get">
    <p>
        用户(id): <input type="text" name="id"/>
    </p>

    <p>
        反馈(fb): <input type="text" name="fb"/>
    </p>
    <input type="submit" value="提交"/>
</form>
<strong>
    6.客户反馈获取
</strong>

<form action="fb" method="get">
    <p>
        用户(id): <input type="text" name="id"/>
    </p>
    <input type="submit" value="提交"/>
</form>
</body>
</html>