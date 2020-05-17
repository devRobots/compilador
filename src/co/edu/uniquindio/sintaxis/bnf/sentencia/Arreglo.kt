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

class Arreglo (private val tipo : Token, private val identificador : Token, private val listArgumentos: ArrayList<Argumento>): Sentencia() {
    init {
        nombre = "Declarar arreglo"
        estructura = "${tipo}{} $identificador = {List}"
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
        agregarAtributo("Declaración de arreglo",0)
        agregarValor(estructura,0)

        agregarAtributo("Lista de Parametros",1)
        agregarValor(listArgumentos.toString(),1)

        agregarAtributo("Tipo de dato",2)
        agregarValor(tipo.lexema,2)

        return panel
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        tablaSimbolos.agregarVariable(identificador.lexema, tipo.lexema, ambito, identificador.fila, identificador.columna)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {

    }
}