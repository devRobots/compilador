package co.edu.uniquindio.sintaxis.bnf.sentencia

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
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
 * Arreglo
 */
class Arreglo(
        private val tipo: Token,
        private val identificador: Token,
        private val listArgumentos: ArrayList<Argumento>
) : Sentencia("Arreglo") {

    override fun toString(): String {
        return "${tipo}{} $identificador = {List}"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        val listaObservable = SintaxisObservable(ListaSintactica("Lista de parametros"))
        val treeListaParametros = TreeItem(listaObservable)
        for (lista in listArgumentos) {
            treeListaParametros.children.add(lista.getTreeItem())
        }
        treeItem.children.add(treeListaParametros)

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Declaración de arreglo")
        agregarValor(identificador.lexema)

        agregarAtributo("Tipo de dato")
        agregarValor(tipo.lexema)

        agregarAtributo("Lista de Parametros")
        agregarValor(listArgumentos.toString())

        return panel
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        tablaSimbolos.agregarVariable(identificador.lexema, tipo.lexema, null, ambito, identificador.fila, identificador.columna)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {

    }
}