package co.edu.uniquindio.semantica

class Ambito {
    var padre: Ambito? = null
    var nombre: String? = null

    constructor(padre: Ambito, nombre: String) {
        this.padre = padre
        this.nombre = nombre
    }

    constructor(nombre: String) {
        this.nombre = nombre
    }

    override fun equals(other: Any?): Boolean {
        var otro = other as Ambito
        if (otro.nombre == nombre) {
            if (otro.padre == padre) {
                return true
            }
        }

        return false
    }

    override fun toString(): String {
        return "Ambito(nombre=$nombre, nivel=${obtenerNivel()})"
    }

    private fun obtenerNivel(): Int {
        var nivel = 1
        if (padre != null) {
            nivel += padre!!.obtenerNivel()
        }
        return nivel
    }
}