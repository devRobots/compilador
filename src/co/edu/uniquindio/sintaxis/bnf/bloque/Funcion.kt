package co.edu.uniquindio.sintaxis.bnf.bloque

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.AmbitoTipo
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
 * Funcion
 */
class Funcion(
        private val modificadorAcceso: Token?,
        private val tipo: Token?,
        private val identificador: Token?,
        private val listaParametros: ArrayList<Parametro>,
        private val listaSentencias: ArrayList<Sentencia>
) : Bloque("Funcion") {

    override fun toString(): String {
        return "$modificadorAcceso ${tipo?.lexema ?: "void"} $identificador [ ... ] ¿ ... ?"
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

        val listaSintacticaParams = ListaSintactica("Parametros", listaParametros)
        treeItem.children.add(listaSintacticaParams.getTreeItem())

        val listaSintacticaSentencias = ListaSintactica("Sentencias", listaSentencias)
        treeItem.children.add(listaSintacticaSentencias.getTreeItem())

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

        agregarAtributo("Tipo de Dato de Retorno")
        agregarValor(tipo?.lexema)

        agregarAtributo("Identificador")
        agregarValor(identificador?.lexema)

        agregarAtributo("Parametros")
        agregarValor(ListaSintactica("Parametros", listaParametros).getPropertiesPanel())

        agregarAtributo("Bloques de Sentencia")
        agregarValor(ListaSintactica("Sentencias", listaSentencias).getPropertiesPanel())

        configurarTabla()
        return panel
    }

    private fun obtenerTiposParametros(): ArrayList<String> {
        val tipoParametros = ArrayList<String>()

        for (parametro in listaParametros) {
            tipoParametros.add(parametro.tipo.lexema)
        }

        return tipoParametros
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        tablaSimbolos.agregarFuncion(identificador!!.lexema, tipo?.lexema, modificadorAcceso?.lexema, obtenerTiposParametros())

        for (parametro in listaParametros) {
            tablaSimbolos.agregarVariable(parametro.identificador.lexema, parametro.tipo.lexema, null, Ambito(ambito, identificador.lexema), parametro.identificador.fila, parametro.identificador.columna)
        }

        for (sentencia in listaSentencias) {
            sentencia.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, Ambito(ambito, identificador.lexema))
        }
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        for (sentencia in listaSentencias) {
            sentencia.analizarSemantica(tablaSimbolos, erroresSemanticos, AmbitoTipo(ambito, identificador!!.lexema, tipo?.lexema ?: "void"))
        }
    }

    override fun getJavaCode(): String {
        var codigo = ""
        if (modificadorAcceso != null){
            codigo += "${modificadorAcceso.getJavaCode()} "
        }
        codigo += tipo?.getJavaCode() ?: "void"
        codigo += if(identificador?.lexema == "&principal") " main ( " else " ${identificador?.getJavaCode()}( "
        for(parametro in listaParametros){
            codigo += parametro.getJavaCode() + ","
        }
        codigo = codigo.substring(0,codigo.length-1) + " ) {\n"
        for(sentencia in listaSentencias){
            codigo += "\t${sentencia.getJavaCode()}\n"
        }
        codigo += "}\n"
        return codigo
    }
}