import log from './util.log.js'
import cookies from './util.cookies.js'

let util = {
    cookies,
    log
};

/**
 * 设置网页title
 * @param title
 */
util.title = function (title) {
    title = title || 'y3tu-tool';
    window.document.title = title;
};

util.inOf = function (arr, targetArr) {
    let res = true;
    arr.forEach(item => {
        if (targetArr.indexOf(item) < 0) {
            res = false;
        }
    });
    return res;
};

util.oneOf = function (ele, targetArr) {
    if (targetArr.indexOf(ele) >= 0) {
        return true;
    } else {
        return false;
    }
};


export default util;
