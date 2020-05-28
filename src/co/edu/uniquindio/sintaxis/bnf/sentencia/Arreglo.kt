package co.edu.uniquindio.sintaxis.bnf.sentencia

import co.edu.uniquindio.app.observable.SintaxisObservable
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

    /**
     * Obtiene el nodo TreeItem necesario para la construccion
     * de la vista del arbol sintactico
     *
     * @return TreeItem<SintaxisObservable> El nodo TreeItem
     */
	override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        val listaSintacticaArgs = ListaSintactica("Lista de parametros", listArgumentos)
        treeItem.children.add(listaSintacticaArgs.getTreeItem())

        return treeItem
    }

    /**
     * Obtiene el panel necesario para mostrar la informacion
     * de la estructura sintactica en la interfaz
     *
     * @return GridPane el panel que se mostrara en pantalla
     */
	override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Declaración de arreglo")
        agregarValor(identificador.lexema)

        agregarAtributo("Tipo de dato")
        agregarValor(tipo.lexema)

        agregarAtributo("Lista de Argumentos")
        agregarValor(ListaSintactica("Argumentos", listArgumentos).getPropertiesPanel())

        configurarTabla()
        return panel
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        tablaSimbolos.agregarVariable(identificador.lexema, tipo.lexema, null, ambito, identificador.fila, identificador.columna)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {

    }

    override fun getJavaCode(): String {
        var codigo = ""
        if (listArgumentos!= null){
            codigo += "${tipo.getJavaCode()}[] ${identificador.lexema.substring(1)} = ["
            for (argumento in listArgumentos){
                codigo += argumento.getJavaCode()+","
            }
            codigo += "];"
            return codigo
        }else{
            codigo +=  "${tipo.getJavaCode()}[] ${identificador.lexema.substring(1)};"
            return codigo
        }
    }
}