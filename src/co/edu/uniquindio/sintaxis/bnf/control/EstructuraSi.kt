package co.edu.uniquindio.sintaxis.bnf.control

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.Sintaxis
import co.edu.uniquindio.sintaxis.bnf.bloque.Funcion
import co.edu.uniquindio.sintaxis.bnf.bloque.VariableGlobal
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
 * Estructura Si
 */
class EstructuraSi(
        private val expLogica: ExpresionLogica,
        private val bloqueInstrucciones: ArrayList<Sentencia>
) : Sintaxis("Estructura Si") {

    override fun toString(): String {
        return "wi [ $expLogica ] ¿ ... ?"
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

        val listaObservable = ListaSintactica("Lista de Sentencias", bloqueInstrucciones)
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
        agregarAtributo("Expresion Logica")
        agregarValor(expLogica.toString())

        agregarAtributo("Lista de Sentencias")
        agregarValor(ListaSintactica("Sentencias", bloqueInstrucciones).getPropertiesPanel())

        configurarTabla()
        return panel
    }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        for (sentencia in bloqueInstrucciones) {
            sentencia.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, Ambito(ambito, "CondicionalSi"))
        }
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        expLogica.analizarSemantica(tablaSimbolos, erroresSemanticos, Ambito(ambito, "CondicionalSi"))

        for (sentencia in bloqueInstrucciones) {
            sentencia.analizarSemantica(tablaSimbolos, erroresSemanticos, Ambito(ambito, "CondicionalSi"))
        }
    }

    override fun getJavaCode(): String {
        var codigo = "if( ${expLogica.getJavaCode()} ){\n"
        for(sentencia: Sentencia in bloqueInstrucciones){
            codigo += "\t${sentencia.getJavaCode()}\n"
        }
        codigo += "}\n"
        return codigo
    }

}