package co.edu.uniquindio.sintaxis.bnf.control

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.bnf.expresion.ExpresionLogica
import co.edu.uniquindio.sintaxis.bnf.sentencia.Sentencia
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Ciclo While
 */
class CicloWhile(
        private val expLogica: ExpresionLogica,
        private val listaSentencia: ArrayList<Sentencia>
) : EstructuraControl("Ciclo While") {
    override fun toString(): String {
        return "durante [ $expLogica ] ¿ ... ?"
    }

    /**
     * Obtiene el nodo TreeItem necesario para la construccion
     * de la vista del arbol sintactico
     *
     * @return TreeItem<SintaxisObservable> El nodo TreeItem
     */
	override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(expLogica.getTreeItem())

        val listaSentenciasObservable = ListaSintactica("Sentencias", listaSentencia)
        treeItem.children.add(listaSentenciasObservable.getTreeItem())

        return treeItem

    }

    /**
     * Obtiene el panel necesario para mostrar la informacion
     * de la estructura sintactica en la interfaz
     *
     * @return GridPane el panel que se mostrara en pantalla
     */
	override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Expresion Logica")
        agregarValor(expLogica.toString())

        agregarAtributo("Bloques de Sentencia")
        agregarValor(ListaSintactica("Sentencias", listaSentencia).getPropertiesPanel())

        configurarTabla()
        return panel
    }

    override fun getJavaCode(): String {
        var codigo = "while( ${expLogica.getJavaCode()}){\n"
        for (sentencia in listaSentencia){
            codigo += "${sentencia.getJavaCode()}\n"
        }
        codigo += "}"
        return codigo
    }
}