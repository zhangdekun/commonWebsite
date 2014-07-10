define(function(require){
	/* jQuery 方法扩展 */
    (function($){
    	/* 自定义元素方法 */
    	$.extend($.fn,{
    		initUI: function(callback){
    			return this.each(function(){
    				semi.utils.initUI(this, callback);
    			});
    		},
    		/* 鼠标经过className替换 */
        	hoverClass:function(className){
    			var _className = className || 'hover';
    			return this.each(function(){
    				$(this).hover(function(){
    					$(this).addClass(_className);
    				},function(){
    					$(this).removeClass(_className);
    				});
    			});
    		}
    	});
	})(jQuery);
});