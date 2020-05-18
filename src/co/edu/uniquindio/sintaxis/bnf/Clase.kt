package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.Sintaxis
import co.edu.uniquindio.sintaxis.bnf.bloque.Bloque

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Clase
 */
class Clase(
        private val modificadorAcceso: Token?,
        private val identificador: Token,
        private val listaBloquesSentencia: ArrayList<Bloque>
) : Sintaxis("Clase") {

    override fun toString(): String {
        return "${modificadorAcceso?.lexema} cosa ${identificador.lexema}"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        val listaObservable = SintaxisObservable(ListaSintactica("Bloques de Sentencia"))
        val treeBloquesSentencia = TreeItem(listaObservable)
        for (bloqueSentencia in listaBloquesSentencia) {
            treeBloquesSentencia.children.add(bloqueSentencia.getTreeItem())
        }
        treeItem.children.add(treeBloquesSentencia)

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Modificador de Acceso")
        agregarValor(modificadorAcceso?.lexema)

        agregarAtributo("Identificador")
        agregarValor(identificador.lexema)

        agregarAtributo("Lista de Bloques de Sentencia")
        agregarValor("Lista de Bloques de Sentencia")

        return panel
    }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        for (bloque in listaBloquesSentencia) {
            bloque.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, Ambito(ambito, identificador.lexema))
        }
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        for (bloque in listaBloquesSentencia) {
            bloque.analizarSemantica(tablaSimbolos, erroresSemanticos, Ambito(ambito, identificador.lexema))
        }
    }
}