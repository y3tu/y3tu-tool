import log from './util.log.js'
import cookies from './util.cookies.js'

let util = {
    cookies,
    log
};

/**
 * 获取指定cookies
 */
util.getCookies = function (name) {
    return util.cookies.get(name)
}

/**
 * 设置指定cookies
 */
util.setCookies = function (name, value) {
    util.cookies.set(name, value)
}

/**
 * 设置指定cookies
 * @param cookiesSetting 设置过期时间
 */
util.setCookies = function (name, value, cookiesSetting) {
    util.cookies.set(name, value, cookiesSetting)
}


/**
 * 删除指定cookies
 */
util.removeCookies = function (name) {
    util.cookies.remove(name)
}


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
