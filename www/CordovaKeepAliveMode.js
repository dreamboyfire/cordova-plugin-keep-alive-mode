var exec = require('cordova/exec');
// var events = require("events");
// var emitter = new events.EventEmitter();

const _option = {
    time: 60000,//默认定时时间为1分钟
    toastTips: null//Toast提示信息，默认为null，为null不执行Toast提示
};

exports.EVENT_ENABLE = "EVENT_ENABLE";

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'CordovaKeepAliveMode', 'coolMethod', [arg0]);
};

exports.enable = function (option) {
    var promise = new Promise(function (resovle, reject) {
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

    return promise;
};
/*
exports.on = function (eventName) {
    var promise = new Promise(function (resolve, reject) {
        emitter.on(eventName, function (data) {
            resolve(data);
        });
    });
    return promise;
};*/
