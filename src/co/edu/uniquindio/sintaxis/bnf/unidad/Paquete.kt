package co.edu.uniquindio.sintaxis.bnf.unidad

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.Sintaxis

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Paquete
 */
class Paquete(private val paquete: Token?) : Sintaxis("Paquete") {

    override fun toString(): String {
        return "caja ${paquete?.lexema}!"
    }

    /**
     * Obtiene el nodo TreeItem necesario para la construccion
     * de la vista del arbol sintactico
     *
     * @return TreeItem<SintaxisObservable> El nodo TreeItem
     */
	override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        return TreeItem(observable)
    }

    /**
     * Obtiene el panel necesario para mostrar la informacion
     * de la estructura sintactica en la interfaz
     *
     * @return GridPane el panel que se mostrara en pantalla
     */
	override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Nombre del paquete")
        agregarValor(paquete?.lexema)

        configurarTabla()
        return panel
    }

    override fun getJavaCode(): String {
        return "package ${paquete!!.lexema.substring(1)};\n"
    }
}