import mitt from 'mitt'

const emitter = mitt()

/**
 * // listen to an event
 emitter.on('foo', e => console.log('foo', e) )

 // listen to all events
 emitter.on('*', (type, e) => console.log(type, e) )

 // fire an event
 emitter.emit('foo', { a: 'b' })

 // clearing all events
 emitter.all.clear()
 */
const bus = {};

bus.$on = emitter.on;
bus.$off = emitter.off;
bus.$emit = emitter.emit;
export default bus;