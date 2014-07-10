define(function (require) {

    /**
     * @name Init
     * @class 全站公用，页面初始化
     * @requires jQuery
     * @author 李博龙
     * @version v1.0.0
     */

    var $ = require('jquery'),
        $ajax = require('ajax'),
        $dialog = require('dialog'),
        semi = {};

    /* 全局化常用方法 */
    window.semi = semi;
    window.$ajax = $ajax;
    window.$dialog = $dialog;
    require('st-grid');
    require('daterangepicker');
    require('select2');

    /* 扩充semi */
    $.extend(semi,require('./config'));
    semi.utils = require('utils');
    require('./utils');
    
    /* 扩充jQuery方法 */
    require('./fn.extend');
    
    /* 扩充dialog方法 */
    require('./dialog.extend');
   
    /* 异步加载Bootstrap框架 */
    require.async('../../bootstrap/js/bootstrap.min.js');
    
    /* 浏览器resize */
    semi.utils.winResize({
        name: function(){
            $.extend(semi.browser, semi.utils.getBrowserInfo());
        },
        param: null
    });
    
    /* html5兼容 */
    if(semi.browser.msie && semi.browser.iev<9){
        require.async('html5shiv');
        require.async('respond');
    }
    

    /* 页面初始化 */
    $(function () {
        /* ie6背景图片缓存解决闪烁问题 */
        if(semi.browser.msie && semi.browser.ie6){
            try{
                document.execCommand('BackgroundImageCache', false, true);
            }catch(e){}
        }
        /* 清理浏览器内存,只对IE起效 */
        if(semi.browser.msie) {
            try{
                window.setInterval("CollectGarbage();", 10000);
            }catch(e){}
        }

        seajs.log('common init complete!')
    });
});