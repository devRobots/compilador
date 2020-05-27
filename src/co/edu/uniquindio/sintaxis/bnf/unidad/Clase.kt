package co.edu.uniquindio.sintaxis.bnf.unidad

import co.edu.uniquindio.app.observable.SintaxisObservable
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
class  Clase(
        private val modificadorAcceso: Token?,
        private val identificador: Token,
        private val listaBloquesSentencia: ArrayList<Bloque>
) : Sintaxis("Clase") {

    override fun toString(): String {
        return "${modificadorAcceso?.lexema} cosa ${identificador.lexema}"
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

        val listaObservable = ListaSintactica("Bloques de Sentencia", listaBloquesSentencia)
        treeItem.children.add(listaObservable.getTreeItem())

        return treeItem
    }

    /**
     * Obtiene el panel necesario para mostrar la informacion
     * de la estructura sintactica en la interfaz
     *
     * @return GridPane el panel que se mostrara en pantalla
     */
	override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Modificador de Acceso")
        agregarValor(modificadorAcceso?.lexema)

        agregarAtributo("Identificador")
        agregarValor(identificador.lexema)

        agregarAtributo("Lista de Sentencias")
        agregarValor(ListaSintactica("Sentenicas", listaBloquesSentencia).getPropertiesPanel())

        configurarTabla()
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

    override fun getJavaCode(): String {
        var codigo:String = ""
        if (modificadorAcceso != null){
            codigo += modificadorAcceso.getJavaCode()
        }
        codigo += "class ${identificador!!.lexema.substring(1)} {\n"
        for (bloque in listaBloquesSentencia){
            codigo += bloque.getJavaCode()
        }
        codigo += "}"
        return codigo
    }
}