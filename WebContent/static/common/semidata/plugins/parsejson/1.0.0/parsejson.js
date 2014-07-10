define(function (require, exports, module) {
	
	/**
     * @name parseJson
     * @class 提供字符串转json方法。
     * @author 李博龙
     * @version v1.0.0  
     * @example
     * define(function(require) {
     *     $(function() {
     *         var  $parsejson = require('parsejson');
     *         seajs.log($parsejson('{name:"dragon"}'));
     *     });
     * });
     */
    var parseJson = function (str) {
        var data;
        try {
            data = require('./json_parse')(str);
        } catch (e) {
            data = str;
        }
        return data;
    }

    return parseJson;
});