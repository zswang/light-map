var cordova = require('cordova'),
    exec = require('cordova/exec');

var baidumap = baidumap || {};

/**
 * 初始化
 * @param{Object} options 配置项
 * @param{Function} callback 回调
 */
function init(options, callback) {
	exec(callback, function() {
	}, 'BaiduMap', 'init', [options]);
};

baidumap.init = init;

module.exports = baidumap;