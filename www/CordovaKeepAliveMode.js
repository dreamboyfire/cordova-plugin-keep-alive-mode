var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'CordovaKeepAliveMode', 'coolMethod', [arg0]);
};

exports.enable = function (arg0) {
    var promise = new Promise(function (resovle, reject) {
        var success = function (result) {
            resovle(result);
        };

        var error = function (error) {
            reject(error);
        };

        exec(success, error, "CordovaKeepAliveMode", "enable", [arg0]);
    });

    return promise;
};
