define(function (require, exports, module) {

    /**
     * @name semi.utils
     * @class 扩展全局utils方法集。
     * @requires jQuery
     * @author 李博龙
     * @version v1.0.0
     */
	
    /*----- 扩展全局utils方法集 -----*/
	semi.utils = $.extend(semi.utils, {
		initUI: function(_box, callback){
			var $doc = $(_box || document);
			
			//daterangepicker
	    	if($('input.daterangebox-ipt', $doc).size()>0){
    			var _add = function($this){
    				/*var opts = {};
	    			if ($this.attr('dateFmt')) opts.pattern = $this.attr('dateFmt');
	    			if ($this.attr('minDate')) opts.minDate = $this.attr('minDate');
	    			if ($this.attr('maxDate')) opts.maxDate = $this.attr('maxDate');
	    			if ($this.attr('mmStep')) opts.mmStep = $this.attr('mmStep');
	    			if ($this.attr('ssStep')) opts.ssStep = $this.attr('ssStep');
	    			if ($this.attr('changeMinDate')) opts.changeMinDate = $this.attr('changeMinDate');
	    			if ($this.hasClass('dateicon') && $this.siblings('a.dateicon').size()<1){
	    				$this.before('<a class="dateicon" href="javascript:;">选择</a>');
	    			}*/
    				if(semi.utils.isEmpty($this.val()))$this.val(semi.daterange.start+' 至 '+semi.daterange.end);
    				$this.daterangepicker({
	    	            format:'YYYY-MM-DD'
	    	        }, function(start, end, label) {
	    	        	console.log(start.toISOString(), end.toISOString(), label);
	    	        	var callback = $this.data('change');
	    	        	if(!semi.utils.isEmpty(callback) && $.isFunction(callback))callback.call($this);
	    	        });
    			}
    			$('input.daterangebox-ipt', $doc).each(function(){
	    			var $this = $(this);
	    			_add($(this));
	    		});
    			if(semi.utils.isEmpty(semi.daterangepicker)){
    				semi.daterangepicker = function(input){
    					setTimeout(function(){_add(input);},100)
    				}
    			}
	    	}
			
			//validate form
	    	if($('form.required-validate', $doc).size()>0){
	    		require.async('validate',function(){
	    			$('form.required-validate', $doc).each(function(){
	    				var $form = $(this);
	    				$form.validate({
	    					onsubmit: false,
	    					focusInvalid: false,
	    					focusCleanup: true,
	    					errorElement: 'span',
	    					ignore:'.ignore',
	    					invalidHandler: function(form, validator) {
	    						var errors = validator.numberOfInvalids();
	    						if (errors) {
	    							//var message = DWZ.msg('validateFormError',[errors]);
	    							//alertMsg.error(message);
	    						} 
	    					}
	    				});
	    				
	    				$form.find('input[customvalid]').each(function(){
	    					var $input = $(this);
	    					$input.rules('add', {
	    						customvalid: $input.attr('customvalid')
	    					})
	    				});
	    			});
	    		});
	    	}
	    	if($('.form_ctrl').html()=='')$('.form_ctrl').append('&nbsp;');
	    	$('input[type=radio]').addClass('ipt_radio');
	    	$('input[type=checkbox]').addClass('ipt_checkbox');
	    	$('input[disabled=disabled],textarea[disabled=disabled],select[disabled=disabled]').addClass('disabled');
	    	$('input[readonly=readonly],textarea[readonly=readonly],select[readonly=readonly]').addClass('readonly');
	    	$('input,textarea,select', $doc).each(function(){
	    		if(!semi.utils.isEmpty($(this).attr('class')) && $(this).attr('class').indexOf('required')!=-1){
	    			$(this).addClass('required');
	    		}
	    	});
	    	
	    	// 回调
	    	if($.isFunction(callback)){
	    		callback.call();
	    	}
    	},
    	/* 集体加hoverClass */
	    hoverClass: function(str){
			var array = new Array();
			array = str.split(",");
			for(var index in array){
				$(array[index]).hoverClass();
			}
		}
    });
});