package co.edu.uniquindio.semantica

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 3.0
 *
 * Ambito
 */
class Ambito(var nombre: String) {
    private var padre: Ambito? = null

    /**
     * Contructor secundario para ambitos anidados
     */
    constructor(padre: Ambito, nombre: String) : this(nombre) {
        this.padre = padre
    }

    /**
     * Metodo toString() para imprimir ambito de manera legible
     *
     * @return String El nombre del ambito junto con el de sus padres
     */
    override fun toString(): String {
        var salida = nombre

        if (padre != null) {
            salida += "${padre.toString()}.$salida"
        }

        return salida
    }

    /**
     * Metodo para comparar dos ambitos
     *
     * @return Boolean true si son identicos, false si son diferentes
     */
    override fun equals(other: Any?): Boolean {
        var otro = other as Ambito
        if (otro.nombre == nombre) {
            if (otro.padre == padre) {
                return true
            }
        }
        return false
    }
}