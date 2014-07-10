<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>login</title>
</head>
<body>
    <form action="/login" method="POST">
    <p>please input your PTSV_CODE...</p>
        PTSV_CODE:<input name="ptsv_code" id="ptsv_code"  type="text"/>
     <p>ROOT PLATFORM : 像1169或者690这种大平台
     	NORM PLATFORM : 1169或者690下的平台（包含小微）
     </p>
     
        <select id="select_type" name="select_type">
        	<option value="root">ROOT PLATFORM</option>
        	<option value="norm">NORM PLATFORM</option>
        </select>
        <input type="submit" value="login">
    </form>
</body>
</html>