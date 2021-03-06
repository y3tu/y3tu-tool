class EventBus {
    constructor () {
        this.callbacks = {}
    }
    $on(name,fn) {
        this.callbacks[name] = this.callbacks[name] || []
        this.callbacks[name].push(fn)
    }
    $emit(name,...args) {
        if(this.callbacks[name]){
            //存在遍历所有callback
            this.callbacks[name].forEach(cb => cb(...args))
        }
    }
}

export default EventBus