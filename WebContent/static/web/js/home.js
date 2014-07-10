define(function(require){
    $(function(){
    	
    	// 左侧类表:http://192.168.200.49/subs?ptsv_code=V0259&select_type=root
    	$ajax.ajax({
			url : '/subs?ptsv_code='+ptsvcode+'&select_type='+selecttype,
     		success: function(data){
				$.each(data,function(i,j){
    				$('.left-menu').append('<dd><a href="javascript:;" code="'+j.ptsv_code+'">'+j.ptsv_name+'</a></dd>');
    			})
    		}
    	});
    	
    	lastperiodid = lastperiodid.substr(0,4)+'-'+lastperiodid.substr(4,2);
    	$('.dataControl').val(lastperiodid);
    	
    	var $getData = require('./getData');
    	var getData = function(date){
    		$getData.getScatterData({date:date});
			$getData.getBarData({date:date});
			$getData.getColumnData({date:date});
    	}
    	
    	getData(lastperiodid);
    	
    	/* 选择时间 控件 */
    	require('datetimepicker');
        $('.dataControl').datetimepicker({
			format: 'yyyy-mm',
			autoclose: true,
			startView: 3,
			minView:3,
			startDate: moment(lastperiodid).subtract('year',2).format('YYYY-MM'),
			endDate: lastperiodid
		}).change(function(){//时间change监听事件
			getData($(this).val());
		});
		
    	//上传表格
    	var $upxlsx = require('upxlsx');
    	$('.upxlsx_swf').append('<div id="upxlsxcontent"></div>');
    	$upxlsx.init({
    		item: '#upxlsxcontent',
    		vars:{
    			url: '/uploadScatterExcel',
    			filekey: 'scatterFile',
    			ptsv_code: ptsvcode,
    			select_type: selecttype,
    			period_id: $('.dataControl').val().replace('-','')
    		},
    		success: function(data){
    			seajs.log(data);
    			$dialog.alert('导入成功！','success');
    		},
    		error: function(data){
    			$dialog.alert('导入失败，请稍后重试！','warning');
    		}
    	});

    	
    	//编辑二维点阵图弹框
    	var dialogTmpl=['<table class="table table-bordered table-striped pop-table">'+
							'<tr><th>名称</th><th>适用范围</th><th>更新时间</th></tr>'+
							'<tr><td>二维点阵</td><td>平台</td><td>2014-7-01</td></tr>'+
							'<tr><td>二维点阵</td><td>平台</td><td>2014-7-01</td></tr>'+
							'<tr><td>二维点阵</td><td>平台</td><td>2014-7-01</td></tr>'+
						'</table>'].join();

    	var popDot=['<div class="pop-dotMatrix">'+
						'<table cellpadding="0" cellspacing="0"></table>'+
						'<div class="pop-opreate">'+
					       //'<button type="button" class="btn btn-default previewBtn">预览</button>'+
					        '<button type="button" class="btn btn-default saveBtn" style="margin:10px;">保存</button>'+
					        '<button type="button" class="btn btn-default cancelBtn" style="margin:10px;">取消</button>'+
					    '</div><div class="PopDotMatrix"><div id="PopDotMatrix" style="height:300px; width:600px;position:relative;z-index:2;"></div></div>'+
					'</div>'].join();
		var faTmpl=['<tr class="col">'+
			        	'<td class="faObj">'+
			            	'<div class="form-group">'+
			                  '  <span class="tit">分类</span><input type="text" class="form-control faname"><input type="hidden" class="form-control facode">'+
			                   ' <span class="addBtn glyphicon glyphicon-plus"></span>'+
			                   ' <span class="delBtn glyphicon glyphicon-minus"></span>'+
			               ' </div></td>'+
			           '<td class="chObj">'+
			       '</tr>'].join();
		var chTmpl=['<div class="form-group"><span class="tit">子分类</span><input type="text" class="form-control chname"><input type="hidden" class="form-control chcode">'+
						'<select class="form-control chcolor" style="width:55px;margin:0 5px;"><option value="1" checked="checked">红</option><option value="2">黄</option><option value="3">绿</option></select>'+
	                   ' <span class="addBtn glyphicon glyphicon-plus" style="visibility: hidden;"></span>'+
	                    '<span class="delBtn glyphicon glyphicon-minus" style="visibility: hidden;"></span>'+
	                 '</div>'].join();
		var addTmpl=['<div class="form-group"><span class="tit">增幅</span><input type="text" class="form-control addname"><input type="hidden" class="form-control addcode">'+
						'<select class="form-control addcolor" style="width:55px;margin:0 5px;"><option value="1" checked="checked">红</option><option value="2">黄</option><option value="3">绿</option></select>'+
	                   ' <span class="addBtn glyphicon glyphicon-plus" style="visibility: hidden;"></span>'+
	                    '<span class="delBtn glyphicon glyphicon-minus" style="visibility: hidden;"></span>'+
	                 '</div>'].join(); 
        var $dlg;
    	$('.editdotBtn').click(function(){
    		$dlg = $dialog({
				title: '编辑二维点阵模板',
				content: popDot,
				onshow: function(){
					var $dlg = this, $node = $(this.node), data = $('.dotMatrix').data('data');
					
					$.each(data.colAxis, function(i,n){
						$('.pop-dotMatrix table',$node).append(faTmpl);
						$('.faObj:last .faname',$node).val(n.name);
						$('.faObj:last .facode',$node).val(n.axis_code);
						
						$.each(n.children, function(k,m){
							$('.chObj:last',$node).append(chTmpl);
							$('.chObj:last .chname:last',$node).val(m.name);
							$('.chObj:last .chcode:last',$node).val(m.axis_code);
							if(semi.utils.isEmpty(m.area_id)){
								$('.chObj:last .chcolor',$node).val(1);
							}else{
								$('.chObj:last .chcolor',$node).val(m.area_id);						
							}
						});
						
					});
					$('.pop-dotMatrix tr:last',$node).after('<tr class="rol"><td class="addObj" colspan="2"></td></tr>');
					$.each(data.rowAxis, function(i,n){
						$('.addObj',$node).append(addTmpl);
						$('.addname:last',$node).val(n.name);
						$('.addcode:last',$node).val(n.axis_code);
						if(semi.utils.isEmpty(n.area_id)){
							$('.addcolor:last',$node).val(1);
						}else{
							$('.addcolor:last',$node).val(n.area_id);							
						}
					});
					
					if($('.pop-dotMatrix tr',$node).size()<1){
						$('.pop-dotMatrix table',$node).append(faTmpl);
						$('.chObj:last',$node).append(chTmpl);
						$('.pop-dotMatrix tr:last',$node).after('<tr class="rol"><td class="addObj" colspan="2"></td></tr>');
						$('.addObj',$node).append(addTmpl);
					}
					if($('.addObj .form-group').size()<1){
						$('.addObj',$node).append(addTmpl);
					}
					
					var PopDotMatrix = echarts.init($('#PopDotMatrix')[0]);
			
		    		PopDotMatrix.setOption($('.editdotBtn').data('chartOp'));
		    		
		    		//重绘背景
					$('.PopDotMatrix').append('<div class="chartbg2" style="float:left;position:absolute;left:170px;bottom:60px;z-index:1;"></div>');
			    	var w = $('.PopDotMatrix').width()-180, h = $('.PopDotMatrix').height()-120, xspace = w/data.rowAxis.length, yspace = h/data.colAxis.length;
					//var xspace = 402, yspace = 125;
					$.each(data.rowAxis, function(i,n){
						$.each(data.colAxis, function(j,m){
							//$('.chartbg2').append('<div class="chartbg2_unit" style="width:'+xspace+'px;height:'+yspace+'px;position:absolute;left:0'+(i*xspace)+'px;bottom:0'+(j*yspace)+'px;" x_code="'+n.axis_code+'" y_code="'+m.axis_code+'">'+n.name+':'+n.axis_code+'<br>'+m.name+':'+m.axis_code+'</div>');
							$('.chartbg2').append('<div class="chartbg2_unit" style="width:'+xspace+'px;height:'+yspace+'px;position:absolute;left:0'+(i*xspace)+'px;bottom:0'+(j*yspace)+'px;" x_code="'+n.axis_code+'" y_code="'+m.axis_code+'"></div>');	
							if(!semi.utils.isEmpty(m.children)){
								var sublength = m.children.length;
								$.each(m.children, function(k,o){
									//$('.chartbg2 .chartbg2_unit:last').append('<div class="chartbg2_unit_sub" style="width:'+xspace+'px;height:'+(yspace/sublength)+'px;position:absolute;left:0;bottom:0'+(k*yspace/sublength)+'px;" x_code="'+n.axis_code+'" y_code="'+o.axis_code+'">'+n.name+':'+n.axis_code+'<br>'+o.name+':'+o.axis_code+'</div>');
									$('.chartbg2 .chartbg2_unit:last').append('<div class="chartbg2_unit_sub" style="background:'+$getData.getColor(o.area_id, n.area_id)+';width:'+xspace+'px;height:'+(yspace/sublength)+'px;line-height:'+(yspace/sublength)+'px;position:absolute;left:0;bottom:0'+(k*yspace/sublength)+'px;" x_code="'+n.axis_code+'" y_code="'+o.axis_code+'"><span style="position:absolute;left:-90px;font-size:12px;color:#ccc;display:'+(i==0?'inline':'none')+'">'+(o.name.length>6?o.name.substr(0,6)+'...':o.name)+'</span></div>');
								});
							}
						});
					});
				}
			}).showModal();
    	});
    	
    	//数据无关的内容
		$(document).on('mouseenter','.pop-dotMatrix .form-group',function(){
			$(this).find('.glyphicon').css('visibility','visible');
		});
		$(document).on('mouseleave','.pop-dotMatrix .form-group',function(){
			$(this).find('.glyphicon').css('visibility','hidden');
		});
                 
	    $(document).on('click','.pop-dotMatrix .faObj .addBtn',function(){
			$(this).parents('tr').first().after(faTmpl);
			$(this).parents('tr').next('tr').find('.chObj').append(chTmpl);
		});
		
		$(document).on('click','.pop-dotMatrix .chObj .addBtn',function(){
			$(this).parent('.form-group').parent('td').append(chTmpl);
		});
		
		$(document).on('click','.pop-dotMatrix .addObj .addBtn',function(){
			$(this).parent('.form-group').parent('td').append(addTmpl);
		});
		//删除
		$(document).on('click','.pop-dotMatrix .faObj .delBtn',function(){
			$(this).parents('tr').first().remove();
		});
		
		$(document).on('click','.pop-dotMatrix .chObj .delBtn',function(){
			if($(this).parent('.form-group').siblings().size()<1){
				$(this).parents('tr:first').remove();
			}
			$(this).parent('.form-group').remove();
		});
		
		$(document).on('click','.pop-dotMatrix .addObj .delBtn',function(){
			$(this).parent('.form-group').remove();
		});
    	
    	$(document).on('click','.pop-dotMatrix .cancelBtn',function(){
			$dlg.close().remove();
		});
		
		$(document).on('click','.saveBtn',function(){
    		var data = {colAxis:[],rowAxis:[]}, $node = $('.pop-dotMatrix');
    		$('.rol .form-group',$node).each(function(){
    			var $col = $(this), d = {name:$('.addname',$col).val(), axis_code:$('.addcode',$col).val(), area_id:$('.addcolor',$col).val()};
    			data.rowAxis.push(d);
    		});
    		
    		
    		$('.col',$node).each(function(){
    			var $col = $(this), d = {name:$('.faname',$col).val(), axis_code:$('.facode',$col).val(), children:[]};
    			$('.chObj .form-group',$col).each(function(){
    				var $this = $(this);
    				d.children.push({name:$('.chname',$this).val(), axis_code:$('.chcode',$this).val(), area_id:$('.chcolor',$col).val()});
    			});
    			data.colAxis.push(d);
    		});
    		
    		$.extend(data,{
    			ptsv_code: ptsvcode,
    			//select_type: selecttype,
    			period_id: $('.dataControl').val().replace('-','')
    		});
    		
    		seajs.log(data);
    		$dlg.close().remove();
    		$.ajax({
                url: '/saveSM',
                data: JSON.stringify(data),
                contentType: "application/json",
                type: 'post',
                dataType: 'json',
                success: function(data){
                	$dlg.close().remove();
                	$getData.getScatterData({date:$('.dataControl').val()});
					$dialog.alert('保存二维点阵模板成功！','success');
                },
                error: function(data){
                	$dialog.alert('保存二维点阵模板失败，请稍后重试！','warning');
                }
            });
    	})
    });
});