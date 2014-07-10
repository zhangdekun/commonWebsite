define(function (require, exports, module) {

    /**
     * @name Upxlsx
     * @class 提供 init, getSWF 方法。
     * @requires jquery, swfobject
     * @author 李博龙
     * @version v1.0.0  
     * 最大支持5M
     */

    var $ = require('jquery'),
    	$parsejson = require('parsejson'),
        swfobject = require('swfobject'),
        statusCode = {
            ok: 200,
            error: 300,
            timeout: 301
        };
    
    var defaultInitParams = {
    		url: '../static/common/semidata/plugins/upxlsx/1.0.0/upxlsx.swf',
    		width: '100%',
    		height: '100%',
    		version: '10.0.0'
    	},
		defaultVars = {
	    	url: ''
	    },
		defaultParams = {
	        menu: 'false',
	        quality: 'high',
	        wmode: 'transparent',
	        allowFullScreen: 'true',
	        allowScriptAccess: 'always'
	    },
	    defaultAttrs = {
	        id: 'upxlsx',
	        name: 'upxlsx'
	    }

    /**
     * @name Upxlsx#initUpxlsx
     * @function   
     * @desc 播放器初始化方法。
     * @param {Object} options
     */
    var initUpxlsx = function (options) {
        var op = $.extend({
                item: '',
                initParams: null,
                vars: {
                	 url: ''
                },
                params: null,
                attrs: null,
                success: null,
                error: null,
                start: null
            }, options), item = op.item;
        
        if(semi.utils.isEmpty(op.vars.url)){
        	seajs.log('upxlsx: url不能为空！');
        	return;
        }
        

        if (semi.utils.isEmpty(op.item)) item = 'upxlsxcontent';
        item = item.replaceAll('#','');
        
        op.initParams = $.extend(defaultInitParams, op.initParams);
        op.vars = $.extend(defaultVars, op.vars);
        op.params = $.extend(defaultParams, op.params);
        op.attrs = $.extend(defaultAttrs, op.attrs);
        
        window.upxlsxStart = function(data){
        	if(!semi.utils.isEmpty(op.start) && $.isFunction(op.start)){
        		op.start.call(this, data);
        	}else{
        		seajs.log('Upxlsx Start:')
        		seajs.log(data);
        	}
        }
        window.upxlsxSuccess = function(data){
        	var status;
            data = $parsejson(data);
            status = _checkStatusCode(data);
            if(status == statusCode.error || status == statusCode.timeout) {
            	upxlsxError(data);
            }else{
                if(!semi.utils.isEmpty(options.success) && $.isFunction(options.success)){
                    options.success.call(this, data);
                }else{
                    seajs.log('Upxlsx Success:');
                    seajs.log(data);
                }
            }
        }
        window.upxlsxError = function(data){
        	if(!semi.utils.isEmpty(op.error) && $.isFunction(op.error)){
        		op.error.call(this, data);
        	}else{
        		seajs.log('Upxlsx Error:')
        		seajs.log(data);
        	}
        }
        
        swfobject.embedSWF(op.initParams.url, item, op.initParams.width, op.initParams.height, op.initParams.version, op.initParams.installUrl, op.vars, op.params, op.attrs);
    }

    window.console_log = function(value){
		seajs.log(value);
	}

    /**
     * @name Upxlsx#getSWF
     * @function   
     * @desc 获取页面Flash。
     * @param {String} movieName
     */
    var getSWF = function (movieName) {
        if (window.document[movieName]) {
            return window.document[movieName];
        }
        if (navigator.appName.indexOf('Microsoft Internet') == -1) {
            if (document.embeds && document.embeds[movieName]) {
                return document.embeds[movieName];
            }
        } else {
            // if (navigator.appName.indexOf('Microsoft Internet')!=-1)
            return document.getElementById(movieName);
        }
    }
    
    /**
     * @name Upxlsx#_checkStatusCode
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
    
    return {
        init: initUpxlsx
    }
});