<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>海尔</title>
    
    <script src="../../static/common/semidata/seajs/seajs/2.2.1/sea.js" id="seajsnode"></script>
    <script src="../../static/common/semidata/config.js"></script>
    <script src="../../static/common/echarts/echarts-plain-map.js"></script>
    <script href="../../static/common/bootstrap/js/bootstrap.min.js"></script>
    <link href="../../static/common/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="../../static/web/css/home.css" rel="stylesheet">
    <script>
    	var ptsvcode = '${ptsv.ptsv_code}', selecttype = '${select_type}' ,ptsvname = '${ptsv.ptsv_name}';
    	lastperiodid='${last_period_id}'
    </script>
</head>

<body>
	<div id="wrap">
    	<div class="left-side-bg"></div>
    	<div class="left-side">
    		<div class="logo"><img src="../../static/web/images/logo.jpg"/></div>
    		<dl class="left-menu">
    			<dt class="curr"><span class="glyphicon glyphicon-home icon-home"></span>${ptsv.ptsv_name}</dt>
    		</dl>
		</div>
        <div class="header">
            <h3 class="branch">${ptsv.ptsv_name}</h3>
            <!--时间控件 start-->
			<div class="daterangebox">
		        <input type="text" name="reservation" class="daterangebox-ipt dataControl" value="" placeholder="选择月份" readonly/>
		        <a class="daterangebox-btn" href="javascript:;">日期</a>
		    </div>
		    <!--时间控件 end-->
        </div>
        <div class="box">
            <div class="page-container">
                <div class="row">
					<div class="col-lg-6">
						<div class="dataTab">
							<button type="button" class="btn btn-default curr">年累</button>
					        <button type="button" class="btn btn-default">月累</button>
						</div>
						<ul class="progressBar" style="display: block;"></ul>
						<ul class="progressBar"></ul>
					</div>
					<div class="col-lg-6">
						<div class="financeTab"></div>
					    <button type="button" class="btn btn-default back-btn">返回</button>
					    <div style="clear:both;"></div>
					</div>
				</div>
				<div class="row" style="padding-right: 30px; padding-top:30px; border-top: 1px solid #e1e1e1;">
                    <div class="col-lg-10">
                    	<div class="dot-matrix-opreate">
					        <button type="button" class="editdotBtn btn btn-default">编辑二维点阵</button>
					        <a class="btn btn-default downloadBtn" href="/scatterExcel?ptsv_code=${ptsv.ptsv_code}&period_id=201406&select_type=${select_type}">导出二维点阵</a>
					        <div class="btn btn-default upxlsx" style="width: 110px;height: 34px;line-height:34px;padding:0;position:relative;">
					        	导入二维点阵
					        	<div class="upxlsx_swf" style="width: 110px;height: 34px;opacity: 0;position:absolute; top:0; left:0;"></div>
					        </div>
					    </div>
                    	<div class="dotMatrix" style="position:relative;"></div>
                	</div>
                    <div class="col-lg-2 right-pie">
	                    <div class="row">
	                    	<h4 class="expectedPie-tit">预算</h4>
	                        <div id="actualPie" style="height:190px;"></div>
	                    </div>
	                    <div class="row">
	                 		<h4 class="actualPie-tit">实际</h4>
	                        <div id="expectedPie" style="height:190px;"></div>
	                    </div>
                	</div>
                </div>
        	</div>
    	</div>
        <div class="footer">©2014 Haier 版权所有</div>
	</div>	
<script>seajs.use('/static/web/js/home');</script>
</body>
</html>