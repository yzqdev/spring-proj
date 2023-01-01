export default class ls {
  static get(name, def = null) {
    const item = localStorage.getItem(name)

    if (item !== null) {
      try {
        const data = JSON.parse(item)

        if (data.expire === null) {
          return data.value
        }

        if (data.expire >= new Date().getTime()) {
          return data.value
        }

        localStorage.removeItem(name)
      } catch (err) {
        return def
      }
    }

    return def
  }

  static set(name, value, expire = null) {
    const stringifyValue = JSON.stringify({
      value,
      expire: expire !== null ? new Date().getTime() + expire : null,
    })

    localStorage.setItem(name, stringifyValue)
  }
  static remove(item) {
    localStorage.removeItem(item)
  }
  static clear(){
    localStorage.clear()
  }
}
