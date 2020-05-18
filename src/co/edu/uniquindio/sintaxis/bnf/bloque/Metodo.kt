package co.edu.uniquindio.sintaxis.bnf.bloque

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.bnf.otro.Parametro
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
 * Metodo
 */
class Metodo(
        private val modificadorAcceso: Token?,
        private val identificador: Token,
        private val listaParametros: ArrayList<Parametro>,
        private val listaSentencias: ArrayList<Sentencia>
) : Bloque("Metodo") {

    override fun toString(): String {
        return "$modificadorAcceso $identificador [ ... ] ¿ ... ?"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        val listaParametrosObservable = SintaxisObservable(ListaSintactica("Parametros"))
        val treeParametros = TreeItem(listaParametrosObservable)
        for (parametro in listaParametros) {
            treeParametros.children.add(parametro.getTreeItem())
        }
        treeItem.children.add(treeParametros)

        val listaSentenciasObservable = SintaxisObservable(ListaSintactica("Sentencias"))
        val treeSentencias = TreeItem(listaSentenciasObservable)
        for (sentencia in listaSentencias) {
            treeSentencias.children.add(sentencia.getTreeItem())
        }
        treeItem.children.add(treeSentencias)

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Modificador de Acceso")
        agregarValor(modificadorAcceso?.lexema)

        agregarAtributo("Identificador")
        agregarValor(identificador.lexema)

        agregarAtributo("Argumentos")
        agregarValor(listaParametros.toString())

        agregarAtributo("Bloques de Sentencia")
        agregarValor(listaSentencias.toString())

        return panel
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        val tiposParametros = ArrayList<String>()

        for (argumento in listaParametros) {
            tiposParametros.add(argumento.tipo.lexema)
            argumento.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, Ambito(ambito, identificador.lexema))
        }

        tablaSimbolos.agregarFuncion(identificador.lexema, "void", modificadorAcceso?.lexema, tiposParametros)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        TODO("Not yet implemented")
    }
}