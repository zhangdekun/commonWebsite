define(function (require, exports, module) {
	/**
     * @name Validate
     * @class 提供 validate 表单验证
     * @requires jquery，jquery-validate
     * @author 李博龙
     * @version v1.0.0  
     * @param {Object} options
     * @example
     * define(function(require){
     * 	$(function(){
     * 	});
     * });
     */
	var $ = require('jquery');
	require('./jquery-validate');
	require('./jquery-metadata');
	
	/* $.validate扩展 */
	;(function ($) {
		
		/* 手机号或邮箱*/
		$.validator.addMethod("emialorphone", function(value,element,param) {
			return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value) || /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/.test(value);
		}, "请输入邮箱或手机号");
		
		/* select判断 */
		$.validator.addMethod("selected", function(value,element,param) {   
			if($(element).val()==-1)return false;
			return true;
		}, "请选择");
		
		/* 中文 */
		$.validator.addMethod("chinese", function(value,element,param) {   
	   	 	var re = /[^\u4e00-\u9fa5]/; 
			if(re.test(value)) return false; 
			return true;
		}, "请输入中文");
		
		/* 非中文 */
		$.validator.addMethod("unchinese", function(value,element,param) {
			var re = /[\u4e00-\u9fa5]+/; 
			return !re.test(value);
	    }, "请输入非中文字符");
		
		/* 昵称 */
		$.validator.addMethod("nickname", function(value,element,param) {
			if(value.trim().length==0)return true;
			var rx = /[a-z\d]/i, rxcn = /[\u4e00-\u9fa5]/, num = 0, chr;
	        for (var i = 0, j = value.length; i < j; i++) {
	            chr = value.charAt(i);/////////
	            if (rx.test(chr)) num += 1;
	            else if (rxcn.test(chr)) num += 2;
	            else return false;
	        }
	        if (num < 7 || num > 14) return false;/////////
	        return true;
	    }, "请输入中、英文，最长是14个英文或7个汉字");
		
		/* 手机号 */
		$.validator.addMethod("phone", function(value,element,param) {
	        var length = value.length;
	        return value==$(element).attr('placeholder') || this.optional(element) || (length == 11 && /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/.test(value));    
	    }, "请正确填写您的手机号码");
		
		/* 登录名 */
		$.validator.addMethod("chars", function(value,element,param) {
			var re = /^[a-zA-Z][a-zA-Z0-9_]*$/; 
			if(re.test(value)) return true;
	    }, "请输入英文字母开头，只含有英文字母、数字和下划线");
		
		/* 密码 */
		$.validator.addMethod("password", function(value,element,param) {
			var re = /^(\w){6,20}$/; 
			return re.test(value);
	    }, "密码由6-20位半角字符(字母、数字、符号)组成,区分大小写");
		
		/* QQ号 */
	    $.validator.addMethod("qq", function(value,element,param) {
	        var qq = /^[1-9]\d{4,9}$/; 
	        var length = value.length;
	        return qq.test(value) || (length > 4 && length<15)
	    }, "请输入正确的QQ号码"); 
	    
		/*  不等于 */
		$.validator.addMethod("unequalTo", function(value,element,param) {
			var target = $(param).unbind(".validate-equalTo").bind("blur.validate-equalTo", function() {
				$(element).valid();
			});
			return value != target.val();
	    }, "请设置一个新的");
		
		/* 不等于一组 */
		$.validator.addMethod("unequalToArr", function(value,element,arr) {
			for(var i=0; i<arr.length; i++){
				if(arr[i]==value) {
					return false; 
				}
			};
			return true;
	    }, "请设置一个新的");
		
		$.validator.addClassRules({
			selected: {selected: true},
			chinese: {chinese: true},
			unchinese: {unchinese: true},
			nickname: {nickname: true},
			phone: {phone: true},
			chars: {chars: true},
			password: {password: true},
			qq: {qq: true},
			unequalTo: {unequalTo: true},
			unequalToArr: {unequalToArr: true}
		});
	    
		//$.validate提示汉化
		$.extend($.validator.messages, {
			required: "这个必须有",
			remote: "请修正该字段",
			remotep: "请修正该字段",
			email: "请输入正确的邮箱地址",
			url: "请输入合法的网址",
			date: "请输入合法的日期",
			dateISO: "请输入合法的日期 (ISO).",
			number: "请输入合法的数字",
			digits: "只能输入正整数",
			creditcard: "请输入合法的信用卡号",
			equalTo: "请再次输入相同的值",
			accept: "请输入拥有合法后缀名的字符串",
			maxlength: $.validator.format("请输入一个长度最多是 {0} 的字符串"),
			minlength: $.validator.format("请输入一个长度最少是 {0} 的字符串"),
			rangelength: $.validator.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
			range: $.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
			max: $.validator.format("请输入一个最大为 {0} 的值"),
			min: $.validator.format("请输入一个最小为 {0} 的值")
		}); 
	})(jQuery);
	
});