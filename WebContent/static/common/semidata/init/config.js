define(function (require, exports, module) {

    /**
     * @name config
     * @class 公用常量、变量。
     */
    var config = {};
    config = {
        /**
         * @function
         * @desc 浏览器相关。
         * @example
         * Object{
	     *     w: 1024,
	     *     h: 768,
	     *     msie: false,
	     *     ie6: false,
	     *     iev: undefined
	     * }
         */
        browser: {
            w: 1024,
            h: 768,
            msie: false,
            ie6: false,
            iev: undefined
        },
        /**
         * @function
         * @desc 键盘操作。
         * @example
         * Object{
	     *     ENTER: 13, ESC: 27, END: 35, HOME: 36,
	     *     SHIFT: 16, TAB: 9,
	     *     LEFT: 37, RIGHT: 39, UP: 38, DOWN: 40,
	     *     DELETE: 46, BACKSPACE:8
	     * }
         */
        keyCode: {
            ENTER: 13, ESC: 27, END: 35, HOME: 36,
            SHIFT: 16, TAB: 9,
            LEFT: 37, RIGHT: 39, UP: 38, DOWN: 40,
            DELETE: 46, BACKSPACE:8
        },
        /**
         * @function
         * @desc 记录ajax请求。
         * Array['www.semidata.cn/login','www.semidata.cn/register'...]
         */
        ajax:[],
        grid: {
        	pageTemplate: '<div class="gridTotal">共<b>{totalCount}</b>个广告活动</div>' +
			            '<input type="button" value="首页" class="page-bt first-page" title="首页">' +
			            '<input type="button" value="上一页" class="page-bt pre-page" title="上一页">' +
			            '第{pageNo}页' +
			            '<input type="button" value="下一页" class="page-bt next-page" title="下一页">' +
			            '<input type="button" value="末页" class="page-bt last-page" title="末页">' +
			            '共{totalPage}页&nbsp;&nbsp;跳转至' +
			            '<input type="text" size="4" maxlength="5" class="some-page"/>' +
			            '每页<select class="page-size"><option value="10">10</option><option value="20">20</option></select>条记录'
        },
        daterange:{
        	start: moment().subtract('days', 7).format("YYYY-MM-DD"),
        	end: moment().subtract('days', 1).format("YYYY-MM-DD")
        }
    };

    return config;
});