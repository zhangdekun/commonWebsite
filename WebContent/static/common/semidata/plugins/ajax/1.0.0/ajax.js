define(function(require, exports, module) {

    /**
     * @name Ajax
     * @class 提供 ajax, ajaxP, ajaxSubmit, ajaxSubmitP 处理返回的 json 。
     * @requires jquery, parsejson
     * @author 李博龙
     * @version v1.0.0  
     */
    var $ = require('jquery'),
		$parsejson = require('parsejson'),
		statusCode = {
	        ok: 200,
	        error: 300,
	        timeout: 301
	    }
    /**
     * @name Ajax#ajax
     * @function   
     * @desc 普通 ajax 请求。
     * @param {Object} options
     * @example
     * define(function(require) {
     *     $(function() {
     *         var  $ajax = require('ajax');
     *         $ajax.ajax({
     *     	       url: 'test.html',           //【必填】请求地址
     *     	       type: 'get',                //【选填】请求方式，默认值：get
     *     	       cache: false,               //【选填】是否缓存，默认值：false	
     *     	       data: {                     //【必填】请求参数
     *     		       name: 'dragon',
     *     		       age: 100
     *     	       },
     * 		       success: function(data){    //【选填】请求成功回调
     * 			       seajs.log(data);
     * 		       },
     * 		       error: function(data){      //【选填】请求出错回调
     * 			       seajs.log(data);
     * 		       },
     * 		       complete: function(data){   //【选填】请求完成回调
     * 			       seajs.log(data);
     * 		       }
     *         });
     *     });
     * });
     */
    var ajax = function(options) {
    	var op = $.extend({
        	url: '',
            type: 'get',
            data: {},
            cache: false,
            jsonp: 'callback',
            hidemsg: false
        }, options);
        if(semi.utils.isEmpty(op.url) || _ajaxSended(op)) return;
        $.ajax({
        	url: op.url,
			type: op.type,
			data: op.data,
			dataType: op.dataType,
			cache: op.cache,
			jsonp: op.jsonp,
			success: function(data, textStatus) {
				_success(data, op);
				_ajaxDone(data, op);
			},
			complete: function(XMLHttpRequest, textStatus) {
				_complete(XMLHttpRequest, op);
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				_error(XMLHttpRequest, op);
				_ajaxDone(XMLHttpRequest, op);
			}
        });
    }

    /**
     * @name Ajax#ajaxP
     * @function   
     * @desc 跨域 ajax 请求。
     * @param {Object} options
     * @example
     * define(function(require) {
     *     $(function() {
     *         var  $ajax = require('ajax');
     *         $ajax.ajaxP({
     *     	       url: 'test.html',           //【必填】请求地址
     *     	       type: 'get',                //【选填】请求方式，默认值：get
     *     	       cache: false,               //【选填】是否缓存，默认值：false	
     *     	       data: {                     //【必填】请求参数
     *     		       name: 'dragon',
     *     		       age: 100
     *     	       },
     * 		       success: function(data){    //【选填】请求成功回调
     * 			       seajs.log(data);
     * 		       },
     * 		       error: function(data){      //【选填】请求出错回调
     * 			       seajs.log(data);
     * 		       },
     * 		       complete: function(data){   //【选填】请求完成回调
     * 			       seajs.log(data);
     * 		       }
     *         });
     *     });
     * });
     */
    var ajaxP = function(options) {
        options.dataType = 'jsonp';
        ajax(options);
    }


    /**
     * @name Ajax#ajaxSubmit
     * @function   
     * @desc form 表单 ajax 提交。
     * @param {Object} options
     * @example
     * define(function(require) {
     *     $(function() {
     *         var  $ajax = require('ajax');
     *         $ajax.ajaxSubmit($('#form'),{
     *     	       url: 'test.html',           //【选填】请求地址，默认值：form 的 action
     *     	       type: 'post',               //【选填】请求方式，默认值：post
     *     	       cache: false,               //【选填】是否缓存，默认值：false	
     * 		       success: function(data){    //【选填】请求成功回调
     * 			       seajs.log(data);
     * 		       },
     * 		       error: function(data){      //【选填】请求出错回调
     * 			       seajs.log(data);
     * 		       },
     * 		       complete: function(data){   //【选填】请求完成回调
     * 			       seajs.log(data);
     * 		       }
     *         });
     *     });
     * });
     */
    require('./jquery-form');
    var ajaxSubmit = function(form, options) {
    	var op = $.extend({
            type: 'post',
            cache: false,
            jsonp: 'callback',
            hidemsg: false
        }, {
            type: form.attr('method'),
            url: form.attr('action')
        }, options);
        
        if(semi.utils.isEmpty(op.url) || _ajaxSended(op)) return;
        form.ajaxSubmit({
        	url: op.url,
			type: op.type,
			dataType: op.dataType,
			cache: op.cache,
			jsonp: op.jsonp,
			success: function(data, textStatus) {
				_success(data, op);
				_ajaxDone(data, op);
			},
			complete: function(XMLHttpRequest, textStatus) {
				_complete(XMLHttpRequest, op);
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				_error(XMLHttpRequest, op);
				_ajaxDone(XMLHttpRequest, op);
			}
        });
    };

    /**
     * @name Ajax#ajaxSubmitP
     * @function   
     * @desc form 表单跨域 ajax 提交。
     * @param {Object} options
     * @example
     * define(function(require) {
     *     $(function() {
     *         var  $ajax = require('ajax');
     *         $ajax.ajaxSubmitP($('#form'),{
     *     	       url: 'test.html',           //【选填】请求地址，默认值：form 的 action
     *     	       type: 'post',               //【选填】请求方式，默认值：post
     *     	       cache: false,               //【选填】是否缓存，默认值：false	
     * 		       success: function(data){    //【选填】请求成功回调
     * 			       seajs.log(data);
     * 		       },
     * 		       error: function(data){      //【选填】请求出错回调
     * 			       seajs.log(data);
     * 		       },
     * 		       complete: function(data){   //【选填】请求完成回调
     * 			       seajs.log(data);
     * 		       }
     *         });
     *     });
     * });
     */
    var ajaxSubmitP = function(form, options) {
        options.dataType = 'jsonp';
        ajaxSubmit(form, options);
    }

    /**
     * @name Ajax#_ajaxSended
     * @desc ajax 请求或表单提交 <i style="color:green">前</i> 触发。
     * @event
     * @param {Object} options
     * 
     */
    var _ajaxSended = function(options) {
        if(!semi.utils.isEmpty(semi.ajax) && semi.utils.inArray(options,semi.ajax)!=-1) {
            return true;
        }
        semi.ajax.push(options);
        return false;
    }

    /**
     * @name Ajax#_checkStatusCode
     * @desc 检查返回值status。
     * @event
     * @param {JSON} json
     * 
     */
    var _checkStatusCode = function(json) {
        if(semi.utils.isEmpty(json) || semi.utils.isEmpty(json.statusCode)) {
            return statusCode.ok;
        }
        if(json.statusCode == statusCode.error) {
            return statusCode.error;
        }else if(json.statusCode == statusCode.timeout) {
            return statusCode.timeout;
        }
        return statusCode.ok;
    }
    
    /**
     * @name Ajax#_ajaxDone
     * @desc ajax成功提示。
     * @event
     * @param {JSON} json
     * 
     */
    var _ajaxDone = function(data, options){
    	//TODO
	}


    /**
     * @name Ajax#_success
     * @desc ajax 请求或表单提交 <i style="color:green">成功</i> 后触发</br>根据返回 json 中的 statusCode{ok: 200, error: 300, timeout: 301} 处理。
     * @event
     * @param {JSON} json
     * 
     */
    var _success = function(data, options) {
        var status;
        data = $parsejson(data);
        status = _checkStatusCode(data);
        if(status == statusCode.ok) {
            if(!semi.utils.isEmpty(options.success) && $.isFunction(options.success)){
                options.success.call(this, data);
            }else{
                seajs.log('Ajax Success: ');
                seajs.log(data);
            }
        }else if(status == statusCode.error) {
        	_error.call(this, data, options);
        }else if(status == statusCode.timeout) {
            if(!semi.utils.isEmpty(options.timeout) && $.isFunction(options.timeout)) {
                options.timeout.call(this, data, options);
            }else if(!semi.utils.isEmpty(semi.ajaxTimeout) && $.isFunction(semi.ajaxTimeout)){
            	semi.ajaxTimeout.call(this, data, options);
            }else{
            	_error.call(this, data, options);
            }
        }else{
        	_error.call(this, data, options);
        }
    }

    /**
     * @name Ajax#_complete
     * @desc ajax 请求或表单提交 <i style="color:green">完成</i> 后触发，用于放置ajax重复请求。
     * @event
     */
    var _complete = function(XMLHttpRequest, options) {
    	 if(!semi.utils.isEmpty(options.complete && $.isFunction(options.complete))) {
             options.complete.call(this);
         }
         if(!semi.utils.isEmpty(semi.ajax) && semi.utils.inArray(options,semi.ajax)!=-1) {
         	semi.ajax.splice(semi.utils.inArray(options,semi.ajax), 1);
         }
        //seajs.log('Ajax Complete: ');
        //seajs.log(options);
    }

    /**
     * @name Ajax#_error
     * @desc ajax 请求或表单提交 <i style="color:green">错误</i> 后触发
     * @event
     */
    var _error = function(XMLHttpRequest, options) {
    	if(!semi.utils.isEmpty(options.error) && $.isFunction(options.error)) {
            options.error.call(this, XMLHttpRequest);
        }else{
        	seajs.log('Ajax Error: ');
        	seajs.log(XMLHttpRequest);
        }
    }
    
    return {
        ajax: ajax,
        ajaxP: ajaxP,
        ajaxSubmit: ajaxSubmit,
        ajaxSubmitP: ajaxSubmitP
    }
});