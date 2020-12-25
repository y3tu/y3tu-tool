import Picture from './Picture'
import VText from './VText'
import VButton from './VButton'

export default {
    install(app) {
        app.component('Picture', Picture)
        app.component('VText', VText)
        app.component('VButton', VButton)
    }
}

