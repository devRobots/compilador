package co.edu.uniquindio.sintaxis.bnf.sentencia

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.bnf.otro.Argumento

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Invacion de Metodo
 */
class InvocacionMetodo(
        private val identificador: Token,
        private val listaArgumentos: ArrayList<Argumento>
) : Sentencia("Invocacion de Metodo") {

    override fun toString(): String {
        return "${identificador.lexema}[ ... ]"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        val listaObservable = SintaxisObservable(ListaSintactica("Parametros"))
        val treeImportaciones = TreeItem(listaObservable)
        for (parametro in listaArgumentos) {
            treeImportaciones.children.add(parametro.getTreeItem())
        }
        treeItem.children.add(treeImportaciones)

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Nombre metodo")
        agregarValor(identificador.lexema)

        agregarAtributo("Lista de Parametros")
        agregarValor(listaArgumentos.toString())

        return panel
    }

}
