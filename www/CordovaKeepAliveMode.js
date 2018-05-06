var exec = require('cordova/exec');
// var events = require("events");
// var emitter = new events.EventEmitter();

const _option = {
    time: 60000,//默认定时时间为1分钟
    toastTips: null//Toast提示信息，默认为null，为null不执行Toast提示
};

var _eventbus = {};

exports.EVENT_ENABLE = "EVENT_ENABLE";

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'CordovaKeepAliveMode', 'coolMethod', [arg0]);
};

exports.enable = function (option, success, error) {
    /*var promise = new Promise(function (resovle, reject) {
        var success = function (result) {
            // emitter.emit(exports.EVENT_ENABLE, result);
            resovle(result);
        };

        var error = function (error) {
            reject(error);
        };

        if (option) {
            option.time = option.time ? option.time : _option.time;
            option.toastTips = option.toastTips ? option.toastTips : _option.toastTips;
        }

        exec(success, error, "CordovaKeepAliveMode", "enable", [option]);
    });

    return promise;*/

    exec(success, error, "CordovaKeepAliveMode", "enable", [option]);
};

exports.on = function (eventName, callback, scope) {

    if (!_eventbus[eventName]) {
        _eventbus[eventName] = [];
    }

    var item = [callback, scope || window];

    _eventbus[eventName].push(item);
};


/**
 * 事件监听
 */
exports.fireEvent = function (event) {
    var args = Array.apply(null, arguments).slice(1),
        listener = _eventbus[event];

    if (!listener)
        return;

    for (var i = 0; i < listener.length; i++) {
        var fn    = listener[i][0],
            scope = listener[i][1];

        fn.apply(scope, args);
    }
};