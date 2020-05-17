package co.edu.uniquindio.sintaxis.bnf.sentencia

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.bnf.otro.Argumento
import co.edu.uniquindio.sintaxis.bnf.otro.TipoDato
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class Arreglo (private val tipoDato : TipoDato, private val identificador : Token, private val listArgumentos: ArrayList<Argumento>): Sentencia() {
    init {
        nombre = "Declarar arreglo"
        estructura = "${tipoDato}{} $identificador = {List}"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(tipoDato.getTreeItem())

        val listaObservable = SintaxisObservable(ListaSintactica("Lista de parametros"))
        val treeListaParametros = TreeItem(listaObservable)
        for (lista in listArgumentos) {
            treeListaParametros.children.add(lista.getTreeItem())
        }
        treeItem.children.add(treeListaParametros)

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Declaración de arreglo",0)
        agregarValor(estructura,0)

        agregarAtributo("Lista de Parametros",1)
        agregarValor(listArgumentos.toString(),1)

        return panel
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        tablaSimbolos.agregarVariable(identificador.lexema, tipoDato.tipo.lexema, ambito, identificador.fila, identificador.columna)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {

    }
}