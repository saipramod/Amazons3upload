<%-- 
    Document   : index
    Created on : Oct 25, 2014, 3:52:16 PM
    Author     : supramo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>S3 Upload</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
    </head>
    <body>
        <div class="container">
            <h1>Welcome to Amazon S3 Interface to upload stuff!</h1>

            <c:forEach items="${requestScope.messages}" var="message">
                <p>
                <ul style='color:blue;font-size: 20px'> - ${message}</ul>
            </p>
        </c:forEach>
    </div>
</div>


<div class="container">
    <form role="form" method="post" action="${pageContext.request.contextPath}/upload" enctype="multipart/form-data">
        <div class="form-group">
            <label for="bucketname">Bucket Name:</label>
            <input type="text" class="form-control" name="bucket">
        </div>
        <!--<div class="form-group">
            <label for="keyname">Key Name:</label>
            <input type="text" class="form-control" name="keyname">
        </div>-->
        <div class="form-group">
            <label for="uploadfile">Please choose a file:</label>
            <input type="file" name="uploadFile"><br>
        </div>   
        <button type="submit" class="btn btn-default">Upload</button>
    </form>
</div>
<div class="container">
    <br>
    <p> Below are a list of your buckets </p>
    <c:forEach items="${requestScope.listofbuckets}" var="message">
        <p>
            <ul style='color:blue;font-size: 20px'> - ${message}</ul>
        </p>
    </c:forEach>
</div>

</body>
</html>

