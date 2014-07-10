define(function(require, exports, module){
	
	/* 颜色计算 */
	var getColor = function(colorid1, colorid2, isId){
		//id 1:红#ffd5d0   2:黄#fff1c1   3:绿#e4f2ce
		var _color = '', _id = '';
		if(semi.utils.isEmpty(isId))isId = false;
		if(semi.utils.isEmpty(colorid1) || semi.utils.isEmpty(colorid2)){
			_color = '#fff';
			_id = 0;
		}else{
			if(colorid1=='1' || colorid2=='1'){
				_color = '#ffd5d0';
				_id = 1;
			}else if(colorid1=='2' || colorid2=='2'){
				_color = '#fff1c1';
				_id = 2;
			}else{
				_color = '#e4f2ce';
				_id = 3;
			}
		}
		return isId?_id:_color;
	}
	
	/* 获取点阵图数据 */
	var getScatterData = function(option){
		var op = $.extend({
			date: ''
		},option);
		
		if(semi.utils.isEmpty(op.date))return;
		var date = op.date;
		if(op.date.indexOf('-')!=-1){
			date = date.replaceAll('-','');
		}
		
		//下载点阵图
		$('.downloadBtn').attr('href','/scatterExcel?ptsv_code='+ptsvcode+'&period_id='+date+'&select_type='+selecttype);
		
		//显示点阵图：http://gome.report.mzhen.com:8000/Poci/poci/v1/tdd
    	var arr2=[], xDataCa=[],yDataCa=[], xDataVal=[], yDataVal=[], redPointData=[],bluePointData=[];
    	$ajax.ajax({
			url : '/scatter?ptsv_code='+ptsvcode+'&period_id='+date+'&select_type='+selecttype,
     		success: function(data){
     			getPieData(data);
     			$('.dotMatrix').empty().append('<div id="dotMatrix" style="height:500px;position:relative;z-index:2;"></div>');
    			$('.dotMatrix').append('<div class="chartbg" style="float:left;position:absolute;left:170px;bottom:60px;z-index:1;"></div>').data('data',data);
		    	var w = $('.dotMatrix').width()-180, h = $('.dotMatrix').height()-120, xspace = w/data.rowAxis.length, yspace = h/data.colAxis.length;
				$.each(data.rowAxis, function(i,n){
					$.each(data.colAxis, function(j,m){
						$('.chartbg').append('<div class="chartbg_unit" style="width:'+xspace+'px;height:'+yspace+'px;position:absolute;left:0'+(i*xspace)+'px;bottom:0'+(j*yspace)+'px;" x_code="'+n.axis_code+'" y_code="'+m.axis_code+'"></div>');	
						if(!semi.utils.isEmpty(m.children)){
							var sublength = m.children.length;
							$.each(m.children, function(k,o){
								$('.chartbg .chartbg_unit:last').append('<div class="chartbg_unit_sub" style="background:'+getColor(o.area_id, n.area_id)+';width:'+xspace+'px;height:'+(yspace/sublength)+'px;line-height:'+(yspace/sublength)+'px;position:absolute;left:0;bottom:0'+(k*yspace/sublength)+'px;" x_code="'+n.axis_code+'" y_code="'+o.axis_code+'"><span style="position:absolute;left:-90px;font-size:12px;color:#ccc;display:'+(i==0?'inline':'none')+'">'+(o.name.length>6?o.name.substr(0,6)+'...':o.name)+'</span></div>');
							});
						}
					});
				});
				
				//遍历横坐标--类目
		    	$.each(data.rowAxis,function(i,j){
    				xDataCa.push(j.name);
	    		});
				//遍历纵坐标--类目
		    	$.each(data.colAxis,function(i,j){
    				yDataCa.push(j.name.length>6?j.name.substr(0,6)+'...':j.name);
	    		});
				//遍历横坐标--数值
		    	$.each(data.rowAxis,function(i,j){
    				xDataVal.push(j.axis_code);
					if(!semi.utils.isEmpty(this.children)){
						$.each(this.children,function(k,m){
							xDataVal.push(m.axis_code);
						})
					}
	    		});
				//遍历纵坐标--数值
		    	$.each(data.colAxis,function(i,j){
    				yDataVal.push(j.axis_code);
					if(!semi.utils.isEmpty(this.children)){
						$.each(this.children,function(k,m){
							yDataVal.push(m.axis_code);
						})
					}
	    		});
		    	//遍历点坐标
		    	$.each(data.points,function(i,j){
		    		if(xDataVal.indexOf(j.x_code)==-1 || yDataVal.indexOf(j.y_code)==-1)return true;
					if(j.p_type=='S03'){
						var xVal=parseFloat(((xDataVal.indexOf(j.x_code)+1)-Math.random()).toFixed(2));
						var yVal=parseFloat(((yDataVal.indexOf(j.y_code)+1)-Math.random()).toFixed(2));
						var cc=[xVal,yVal];
						redPointData.push(cc);
					}else if(j.p_type=='S01'){
						var xVal=parseFloat(((xDataVal.indexOf(j.x_code)+1)-Math.random()).toFixed(2));
						var yVal=parseFloat(((yDataVal.indexOf(j.y_code)+1)-Math.random()).toFixed(2));
						var aa=[xVal,yVal];
						bluePointData.push(aa);
					}
	    		});
	    		showDotMatrix();
    		}
    	});
    	   		
		//点阵图初始化
		
    	function showDotMatrix(){
    		var dotMatrix = echarts.init($('#dotMatrix')[0]);
    		var op = {
			    legend: {
					data:['预算','实际'],
					x:'right',
					y:22
				},
				grid:{
					x:170,
					x2:10
					},
			    xAxis : [
			        {
			            type : 'value',
			            position: 'top',
						splitNumber: xDataVal.length,
			            axisLine : {
			                show: false
			            },
			            axisLabel:{
			            	show:false
			            }
			        },
			        {
			            type : 'category',
			            position: 'bottom',
			            data : xDataCa
			        }
			    ],
			    yAxis : [
			    	{
			            type : 'value',
			            position: 'right',
						splitNumber: yDataVal.length,
			            axisLine : {
			                show: false
			            },
			            axisLabel:{
			            	show:false
			            },
			            splitLine:{
			            	show:false
			            }
			        },
			        {
			            type : 'category',
			            position: 'left',
			            axisLabel:{
			            	margin: 100
			            },
			            data : yDataCa
			        }
			    ],
			    animation: false,
			    series : [
			        {
			            name:'预算',
			            type:'scatter',
			            symbol: 'circle',
			            itemStyle:{
	    	            	normal: {
	    	            		color:'#fa705c'
						    }
    	           		},
    			        data: redPointData
			        },
			        {
			            name:'实际',
			            type:'scatter',
			            symbol: 'circle',
			            itemStyle:{
	    	            	normal: {
	    	            		color:'#61c2de'
						    }
    	           		},
   			            data: bluePointData
			        }
			    ]
     		}
    		dotMatrix.setOption(op);
     		$('.editdotBtn').data('chartOp',op);
    	};
	}
	
	/* 获取实际图与预算图数据 */
	var getPieData = function(data){
		
		if(semi.utils.isEmpty(data) || semi.utils.isEmpty(data.points))return;
		
		seajs.log(data)
		
		//显示实际图与预算图：http://192.168.200.49/pie?ptsv_code='+ptsvcode+'&period_id=201406&select_type=root
    	var actualData=[],expectedData=[],
			redArr = []; yellowArr = []; greenArr = [],
			redArr2 = []; yellowArr2 = []; greenArr2 = [];
			
		var getAreaId = function(x_code, y_code){
			var colorid1 = '', colorid2 = '';
			$.each(data.rowAxis, function(k,m){
				if(m.axis_code == x_code)colorid1 = m.area_id;
			});
			$.each(data.colAxis, function(k,m){
				$.each(m.children, function(j,o){
					if(o.axis_code == y_code)colorid2 = o.area_id;
				});
			});
			return getColor(colorid1, colorid2, true);
		}
		
		$.each(data.points, function(i,n){
			var _id = getAreaId(n.x_code, n.y_code);
			if(n.p_type=='S03'){//实际
				if(_id=='1'){
					redArr.push(n);
				}else if(_id=='2'){
					yellowArr.push(n);
				}else if(_id=='3'){
					greenArr.push(n);
				}
			}else{//预算
				if(_id=='1'){
					redArr2.push(n);
				}else if(_id=='2'){
					yellowArr2.push(n);
				}else if(_id=='3'){
					greenArr2.push(n);
				}
			}
		});
		actualData.push({ value:redArr.length, name:'红'},{ value:yellowArr.length, name:'黄'},{ value:greenArr.length, name:'绿'});
		expectedData.push({ value:redArr2.length, name:'红'},{ value:yellowArr2.length, name:'黄'},{ value:greenArr2.length, name:'绿'});
		
		// 实际图初始化
    	var actualPie = echarts.init($('#actualPie')[0]);
    	actualPie.setOption({
    	    legend: {
		        orient : 'horizontal',
		        x : 'center',
		        y : 'bottom',
		        data: ['红','黄','绿']
		    },
    	    series : [
    	        {
    	            type:'pie',
    	            center : ['50%', '40%'],
					itemStyle : {
						normal : {
							label : {
								show : false
							},
							labelLine : {
								show : false
							}
						}
					},
    	            data:actualData
    	        }
    	    ],
    	    color:['#ffd5d0','#fff1c1','#e4f2ce']//id 1:红#ffd5d0   2:黄#fff1c1   3:绿#e4f2ce
    	});
    	
    	// 预算图初始化
    	var expectedPie = echarts.init($('#expectedPie')[0]);
    	expectedPie.setOption({
    	    legend: {
		        orient : 'horizontal',
		        x : 'center',
		        y : 'bottom',
		        data: ['红','黄','绿']
		    },
    	    series : [
    	        {
    	            type :'pie',
    	            center : ['50%', '40%'],
					itemStyle : {
						normal : {
							label : {
								show : false
							},
							labelLine : {
								show : false
							}
						}
					},
    	            data:expectedData
    	        }
    	    ],
    	    color:['#ffd5d0','#fff1c1','#e4f2ce']
    	});
	}
	
	/* 获取年累、月累数据 */
	var getBarData = function(option){
		var op = $.extend({
			date: ''
		},option);
		
		if(semi.utils.isEmpty(op.date))return;
		var date = op.date;
		if(op.date.indexOf('-')!=-1){
			date = date.replaceAll('-','');
		}
		
		
		//切换年累、月累 ：http://gome.report.mzhen.com:8000/Poci/poci/v1/tdd
    	$('.dataTab').children('button').click(function(){
    		$(this).addClass('curr').siblings().removeClass('curr');
    		var index=$(this).index();
    		$('.progressBar').eq(index).show().siblings('.progressBar').hide();
    	});
    	
        $ajax.ajax({
        	url:'/bar?ptsv_code='+ptsvcode+'&period_id='+date+'&select_type='+selecttype,
        	success:function(data){
        			$('.progressBar').empty();
						$.each(data.barList,function(b,c){
							if(c.title=='V04'){//年累
								$.each(c.barItems, function(i,j) {
									var val1;
									if((j.dim_value).toFixed(0).length>4 && (j.dim_value).toFixed(0).length<=8){
										val1=(j.dim_value).toFixed(0).substring(0,(j.dim_value).toFixed(0).length-4)+'万'
									}else if((j.dim_value).toFixed(0).length>=9){
										val1=(j.dim_value).toFixed(0).substring(0,(j.dim_value).toFixed(0).length-8)+'亿'
									}else if((j.dim_value).toFixed(0).length<=4){
										val1=(j.dim_value).toFixed(0);
									}
									       $('.progressBar').eq(0).append('<li class="row">'+
										       								'<div class="col-lg-3">'+
										       									'<h3 class="title-total">'+val1+'</h3><span class="name">'+j.dim_name+'</span></div>'+
																				'<div class="col-lg-9">'+
																				'<div class="progress"><span class="sr-only">'+((j.amplitude)*100).toFixed(2)+'%</span><div class="progress-con"><div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow='+(j.amplitude).toFixed(2)*100+' aria-valuemin="0" aria-valuemax="100" style="width: '+((j.amplitude)*100).toFixed(2)+'%"></div></div></div>'+
																				'<div class="progress"><span class="sr-only">'+((j.pc)*100).toFixed(2)+'%</span><div class="progress-con"><div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow='+(j.pc).toFixed(2)*100+' aria-valuemin="0" aria-valuemax="100" style="width: '+((j.pc)*100).toFixed(2)+'%"></div></div></div>'+
																			'</div>'+
																			'</li>');                                                
								});
							}else if(c.title=='V03'){
									$.each(c.barItems, function(i,j) {
										var val;
									if((j.dim_value).toFixed(0).length>4 && (j.dim_value).toFixed(0).length<=8){
										val=(j.dim_value).toFixed(0).substring(0,(j.dim_value).toFixed(0).length-4)+'万'
									}else if((j.dim_value).toFixed(0).length>=9){
										val=(j.dim_value).toFixed(0).substring(0,(j.dim_value).toFixed(0).length-8)+'亿'
									}else if((j.dim_value).toFixed(0).length<=4){
										val=(j.dim_value).toFixed(0);
									}
									       $('.progressBar').eq(1).append('<li class="row">'+
										       								'<div class="col-lg-3">'+
										       									'<h3 class="title-total">'+val+'</h3><span class="name">'+j.dim_name+'</span></div>'+
																				'<div class="col-lg-9">'+
																				'<div class="progress"><span class="sr-only">'+((j.amplitude)*100).toFixed(2)+'%</span><div class="progress-con"><div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow='+(j.amplitude).toFixed(2)*100+' aria-valuemin="0" aria-valuemax="100" style="width: '+((j.amplitude)*100).toFixed(2)+'%"></div></div></div>'+
																				'<div class="progress"><span class="sr-only">'+((j.pc)*100).toFixed(2)+'%</span><div class="progress-con"><div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow='+(j.pc).toFixed(2)*100+' aria-valuemin="0" aria-valuemax="100" style="width: '+((j.pc)*100).toFixed(2)+'%"></div></div></div>'+
																			'</div>'+
																		'</li>');                                              
									});
								}
		        });
        	}
        });
	}
	
	/* 获取收入、利润、交易额数据 */
	var getColumnData = function(option){
		var op = $.extend({
			date: ''
		},option);
		
		if(semi.utils.isEmpty(op.date))return;
		var date = op.date;
		if(op.date.indexOf('-')!=-1){
			date = date.replaceAll('-','');
		}
		
		$('.back-btn').click(function(){
        	_getColumnData(date,false);
        	$(this).hide();
        }).hide();
        
      	//切换收入、利润、交易额 ：http://gome.report.mzhen.com:8000/Poci/poci/v1/tdd
        $ajax.ajax({//button
        	url:'/dims',
        	data:{ptsv_code:ptsvcode},
        	type:'post',
        	success: function(data){
	        		$('.financeTab').empty();
			        	$.each(data, function(a,b){//遍历button内容
				        	//$('.financeTab').append('<button type="button" class="btn btn-default finBtn">'+b.norm_name+'</button>');
				        	$('.financeTab').append('<button type="button" class="btn btn-default finBtn" code="'+b.norm_code+'">'+b.norm_name+'</button>');
			        	});
		        	$('.financeTab').children().eq(0).addClass('curr');
	        	}
	        });
        
    	//切换收入、利润、交易额 ：http://gome.report.mzhen.com:8000/Poci/poci/v1/tdd
    	var financexAxis=[], financeSeriesred=[],financeSeriesblue=[];
    	$(document).off('click','.finBtn').on('click','.finBtn',function(){
    		var codeVal=$(this).attr('code');
    		if($('.financeItem.'+codeVal).size()==0){
    			$dialog.alert('暂无数据！','warning');
    			return false;
    		}
    		$(this).addClass('curr').siblings().removeClass('curr');
    		$('.financeItem.'+codeVal).show().siblings('.financeItem').hide();
    	});
    	var _getColumnData = function(_date,isDay){
    		if(semi.utils.isEmpty(isDay))isDay = false;
    		$ajax.ajax({//图标
        	url:'/column?ptsv_code='+ptsvcode+'&vers_code='+(isDay?'V01':'V03')+'&period_id='+_date+'&select_type='+selecttype,
        	success: function(data){
        			$('.editdotBtn').data('data',data);
	        		$('.financeItem').remove();
	        		financexAxis=[]
	        		if(isDay){
	        			for(var i=1; i<=moment(_date).daysInMonth(); i++){
	        				financexAxis.push(i+'号');
	        			}
	        		}else{
	        			var _day = parseInt(_date.substr(4,2));
	        			for(var i=1; i<=_day; i++){
	        				financexAxis.push(getMonth(i));
	        			}
	        		}
	        		
		        	$.each(data.columnList, function(a,b){//遍历button内容
		        		//$('.financeTab').append('<button type="button" class="btn btn-default finBtn" code="'+b.code+'">'+b.title+'</button>');
			        	financeSeriesred=[],financeSeriesblue=[]
			        	if(!semi.utils.isEmpty(b.snors.S01)){
				        	$.each(b.snors.S01,function(i,j){
				        		if(isDay){
					        		financeSeriesred.push(''+j.c_vaule+'');
				        		}else{
					        		financeSeriesred.push(''+j.c_vaule+'');
				        		}
				        	});
			        	};
			        	if(!semi.utils.isEmpty(b.snors.S03)){
				        	$.each(b.snors.S03,function(i,j){
					        	financeSeriesblue.push(''+j.c_vaule+'');
				        	});
			        	}
			        	//createFinanceBar(a, isDay);
			        	
		        	   	if(a==0){
				        	$('.financeTab').parent('.col-lg-6').append('<div class="financeItem '+b.code+'" id="financeBar'+a+'" style="height:330px; width:550px"></div>');        		
			        	}else{
				        	$('.financeTab').parent('.col-lg-6').append('<div class="financeItem '+b.code+'" id="financeBar'+a+'" style="height:330px; width:550px; display:none"></div>');
			        	}
			        	var financeBar= 'financeBar'+a;
			    		var financeBar = echarts.init($('#financeBar'+a)[0]);
			    		initFinanceChart(!isDay,financeBar,financexAxis, financeSeriesred,financeSeriesblue);
		        	});
		        	$('.financeTab').children().eq(0).addClass('curr').siblings().removeClass('curr');
	    			$('.financeItem').eq(0).show();
	        	}
	        });
    	}
    	
    	_getColumnData(date);
        
        
        function getMonth(num){
        	var month;
        	switch(num){
				case 1:month='一月';break;
				case 2:month='二月';break;
				case 3:month='三月';break;
				case 4:month='四月';break;
				case 5:month='五月';break;
				case 6:month='六月';break;
				case 7:month='七月';break;
				case 8:month='八月';break;
				case 9:month='九月';break;
				case 10:month='十月';break;
				case 11:month='十一月';break;
				case 12:month='十二月';break;
			}
        	return month;
        }
        
        function getMonth2(num){
        	var month;
        	switch(num){
				case "一月":month='01';break;
				case "二月":month='02';break;
				case "三月":month='03';break;
				case "四月":month='04';break;
				case "五月":month='05';break;
				case "六月":month='06';break;
				case "七月":month='07';break;
				case "八月":month='08';break;
				case "九月":month='09';break;
				case "十月":month='10';break;
				case "十一月":month='11';break;
				case "十二月":month='12';break;
			}
        	return month;
        }
        
/*        function createFinanceBar(num, isDay){
        	if(num==0){
	        	$('.financeTab').parent('.col-lg-6').append('<div class="financeItem'+b.code+'" id="financeBar'+num+'" style="height:330px; width:550px"></div>');        		
        	}else{
	        	$('.financeTab').parent('.col-lg-6').append('<div class="financeItem" id="financeBar'+num+'" style="height:330px; width:550px; display:none"></div>');
        	}
        	var financeBar= 'financeBar'+num;
    		var financeBar = echarts.init($('#financeBar'+num)[0]);
    		initFinanceChart(!isDay,financeBar,financexAxis, financeSeriesred,financeSeriesblue);
        }*/
        
        function initFinanceChart(isMonth,_financeBar,_financexAxis,_financeSeriesred,_financeSeriesblue){
        	
        	/* 财务柱表*/
	    	_financeBar.clear().setOption({
	    	    tooltip : {
	    	        trigger: 'item'
	    	    },
	    	    grid:{
					x:90,
					y:10,
					x2:20,
					y2:50
					},
	    	    xAxis : [
	    	        {	
	    	            type : 'category',
	    	            data : _financexAxis
	    	        }
	    	    ],
	    	    yAxis : [
	    	        {
	    	            type : 'value',
	                    splitLine : {//横轴标记
			                show:true,
			                lineStyle: {
			                    color: '#bcc4d1',
			                    type: 'dashed',
			                    width: 1
			                }
	    	       		},
						splitArea : {//背景条
						    show: true,
						    areaStyle:{
						        color:['rgba(241,241,241,1)','rgba(255,255,255,1)']
						    }
						}
	               }
	    	    ],
	    	    series : [
	    	        {
	    	            name:'预算',
	    	            type:'bar',
	    	            itemStyle:{
	    	            	normal: {
	    	            		color:'#ff7365'
						    },
	    	            },
	    	            data:_financeSeriesred,
	    	        },
	    	        {
	    	            name:'实际',
	    	            type:'bar',
	    	            itemStyle:{
	    	            	normal: {
	    	            		color:'#39b6e5'
						    },
	    	            },
	    	            data:_financeSeriesblue,
	    	        }
	    	    ]
	    	});
	    	if(isMonth){
	    		_financeBar.on('click', function(event){
	    			_getColumnData($('.dataControl').val().substr(0,4)+getMonth2(event.name), true);
	    		});
	    	}else{
	    		$('.back-btn').show();
	    	}
        }
	}
	
	
	return{
		getScatterData: getScatterData,
		getBarData: getBarData,
		getColumnData: getColumnData,
		getColor: getColor
	}
});
