define(function (require, exports, module) {
	
	/* dialog扩展 */
	var $ = require('jquery'),
		$dialog = require('dialog');
	
    /**
     * 警告
     * @param   {String, HTMLElement}   消息内容
     * @param   {Function}              (可选) 回调函数
     */
	$dialog.alert = function (content, type, time, callback) {
    	var time_def = 3000,
    		title;
    	
    	if(typeof content == 'number'){
    		content += '';
    	}
    	if(typeof content != 'string'){
    		seajs.log("调用 $dialog.alert 时类型出错！暂只支持字符型，请检查。");
    		return;
    	}
    	
    	//关闭前一个弹框
    	if(!semi.utils.isEmpty($dialog.get('Alert'))){
    		$dialog.get('Alert').close();
    	}
    	
    	if(typeof type == 'number'){
    		time_def = type;
    		type = 'message';
    	}else if(typeof type == 'function'){
    		callback = type;
    		type = 'message';
    	}
    	if(typeof time == 'function'){
    		callback = time;
    	}
    	if(typeof time == 'number'){
    		time_def = time;
    	}
    	
    	if(type=='success'){
			title = '成功啦';
			content = '<span class="alert-success"></span><span>' + content + '</span>';
		}else if(type=='warning'){
			title = '提醒';
			content = '<span class="alert-warning"></span><span>' + content + '</span>';
		}else if(type=='error'){
			title = '出错啦';
			content = '<span class="alert-error"></span><span>' + content + '</span>';
		}else{
			title = '消息';
		}
    	content = '<div class="dialog-alert">'+content+'</div>';
    	
    	var $d = $dialog({
        	title: title,
            id: 'Alert',
            fixed: true,
            time: time_def,
            padding: '15px 30px',
            content: content,
            okValue: '知道了',
            ok: true,
            onbeforeremove: callback
        }).show();
    	
    	setTimeout(function () {
    		$d.close().remove();
        }, time_def);
    	
        return $d;
    };


    /**
     * 确认选择
     * @param   {String, HTMLElement}   消息内容
     * @param   {Function}              确定按钮回调函数
     * @param   {Function}              取消按钮回调函数
     */
    $dialog.confirm = function (options) {
    	options = $.extend({
    		id: 'Confirm',
    		title: '提醒',
    		content: '亲，你确定要执行本操作？',
    		fixed: true,
            padding: '15px 30px',
    		cancelValue: '取消',
    		cancel: true,
    		okValue: '确定',
    		ok: true
        }, options);
    	
    	options.content = '<span class="d-confirm"></span><span>' + options.content + '</span>';
        return $dialog(options).showModal();
    };
});