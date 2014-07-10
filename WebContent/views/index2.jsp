<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>index</title>
</head>
<body>
${ptsv.org_type}
${ptsv.ptsv_name}
${select_type}
<p>
<a target="_blank" href="/subs?ptsv_code=${ptsv.ptsv_code}&select_type=${select_type}">左侧数据</a>
<p>
<a target="_blank" href="/scatter?ptsv_code=${ptsv.ptsv_code}&period_id=201406&select_type=${select_type}">二维点阵数据</a>

<a target="_blank" href="/pie?ptsv_code=${ptsv.ptsv_code}&period_id=201406&select_type=${select_type}">右侧pie图数据</a>
<a target="_blank" href="/bar?ptsv_code=${ptsv.ptsv_code}&period_id=201406&select_type=${select_type}">bar图数据</a>
<a target="_blank" href="/column?ptsv_code=${ptsv.ptsv_code}&vers_code=V03&period_id=201406&select_type=${select_type}">column图数据</a>
<a target="_blank" href="/column?ptsv_code=${ptsv.ptsv_code}&norm_code=ZB1135&vers_code=V01&period_id=201406&select_type=${select_type}">column图数据钻取日累数据</a>

<a target="_blank" href="/scatterExcel?ptsv_code=${ptsv.ptsv_code}&period_id=201406&select_type=${select_type}">二维点阵excel模板</a>



<p></p>

<form action="/uploadScatterExcel" method="POST" enctype="multipart/form-data">
<input id="scatterFile" type="file" name="scatterFile">
<input id="ptsv_code" name="ptsv_code" value="v1000" />
<input type="submit" value="upload">
</form>
</body>
</html>