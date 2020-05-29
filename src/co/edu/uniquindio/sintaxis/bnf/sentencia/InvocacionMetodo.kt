package co.edu.uniquindio.sintaxis.bnf.sentencia

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.semantica.simbolo.Funcion
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

    /**
     * Obtiene el nodo TreeItem necesario para la construccion
     * de la vista del arbol sintactico
     *
     * @return TreeItem<SintaxisObservable> El nodo TreeItem
     */
	override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        val listaSintacticaArgs = ListaSintactica("Argumentos", listaArgumentos)
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
        agregarAtributo("Nombre metodo")
        agregarValor(identificador.lexema)

        agregarAtributo("Lista de Parametros")
        agregarValor(ListaSintactica("Argumentos", listaArgumentos).getPropertiesPanel())

        configurarTabla()
        return panel
    }

    fun obtenerTipoDato(tablaSimbolos: TablaSimbolos, ambito: Ambito): String {
        return when(identificador.lexema) {
            "&syso" -> "void"
            "&scanner" -> "pal"
            else -> {
                val funcion = tablaSimbolos.buscarFuncion(identificador.lexema, obtenerTiposArgumentos(tablaSimbolos, ambito)) as Funcion?
                funcion?.tipoRetorno ?: "void"
            }
        }
    }

    private fun obtenerTiposArgumentos(tablaSimbolos: TablaSimbolos, ambito: Ambito): ArrayList<String> {
        val tipoArgumentos = ArrayList<String>()

        for (argumento in listaArgumentos) {
            val tipoArg = argumento.obtenerTipoDato(tablaSimbolos, ambito)
            tipoArgumentos.add(tipoArg)
        }

        return tipoArgumentos
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        when(identificador.lexema) {
            "&syso" -> return
            "&scanner" -> return
            else -> {
                val funcion = tablaSimbolos.buscarFuncion(identificador.lexema, obtenerTiposArgumentos(tablaSimbolos, ambito)) as Funcion?
                if (funcion == null) {
                    erroresSemanticos.add(ErrorSemantico("La funcion ${identificador.lexema} no se encuentra definida"))
                } else {
                    for (argumento in listaArgumentos) {
                        argumento.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
                    }
                }
            }
        }
    }

    override fun getJavaCode(): String {
        var codigo = when (identificador.lexema) {
            "&syso" -> "JOptionPane.showMessajeDialog(null, "
            "&scanner" -> "JOptionPane.showInputDialog( "
            else -> "${identificador.getJavaCode()}( "
        }
        for(argumento: Argumento in listaArgumentos){
            codigo += argumento.getJavaCode() +","
        }
        codigo = codigo.substring(0,codigo.length-1) + " );"
        return codigo
    }
}
