class EventBus {
    constructor() {
        this.callbacks = {}
    }

    /**
     * 事件总线中注册事件 singleFlag为true 表示只能注册一次 防止重复执行
     * @param name
     * @param fn
     * @param singleFlag
     */
    $on(name, fn, singleFlag = false) {
        if (singleFlag) {
            this.callbacks[name] = this.callbacks[name] || [];
            this.callbacks[name][0] = fn;
        } else {
            this.callbacks[name] = this.callbacks[name] || [];
            this.callbacks[name].push(fn)
        }
    }

    /**
     * 事件触发
     * @param name
     * @param args
     */
    $emit(name, args) {
        if (this.callbacks[name]) {
            this.callbacks[name].forEach((cb) => cb(args))
        }
    }
}

export default EventBus;