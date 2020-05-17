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

class Clase(private val modificadorAcceso: Token?,private val identificador: Token, private val listaBloquesSentencia: ArrayList<Bloque>) : Sintaxis() {
    init {
        this.nombre = "Clase"
        this.estructura = "${modificadorAcceso?.lexema} cosa ${identificador.lexema}"
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
        agregarAtributo("Modificador de Acceso", 0)
        agregarValor(modificadorAcceso?.lexema, 0)

        agregarAtributo("Identificador", 1)
        agregarValor(identificador.lexema, 1)

        agregarAtributo("Lista de Bloques de Sentencia", 2)
        agregarValor("Lista de Bloques de Sentencia", 2)

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