package co.edu.uniquindio.sintaxis.bnf.unidad

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.Sintaxis


import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Unidad de Compilacion
 */
class UnidadCompilacion(
        private var paquete: Paquete?,
        private var listaImportaciones: ArrayList<Importacion>,
        private var clase: Clase?
) : Sintaxis("Unidad de Compilacion") {
    private var listaSintacticaImportacion: ListaSintactica = ListaSintactica("Importaciones", listaImportaciones)

    /**
     * Obtiene el nodo TreeItem necesario para la construccion
     * de la vista del arbol sintactico
     *
     * @return TreeItem<SintaxisObservable> El nodo TreeItem
     */
	override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(paquete?.getTreeItem())

        treeItem.children.add(listaSintacticaImportacion.getTreeItem())

        treeItem.children.add(clase?.getTreeItem())

        return treeItem
    }

    /**
     * Obtiene el panel necesario para mostrar la informacion
     * de la estructura sintactica en la interfaz
     *
     * @return GridPane el panel que se mostrara en pantalla
     */
	override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Paquete")
        agregarValor(paquete.toString())

        agregarAtributo("Lista de Importaciones")
        agregarValor(listaSintacticaImportacion.getPropertiesPanel())

        agregarAtributo("Clase")
        agregarValor(clase.toString())

        configurarTabla()
        return panel
    }

    override fun getJavaCode(): String {
        var unidadCompilacion = ""

        if (paquete != null) {
            unidadCompilacion += paquete!!.getJavaCode()
        }

        unidadCompilacion += "\nimport javax.swing.JOptionPane;\n"

        for (importacion in listaImportaciones) {
            unidadCompilacion += importacion.getJavaCode()
        }

        unidadCompilacion += "\n"

        if (clase != null) {
            unidadCompilacion += clase!!.getJavaCode()
        }

        return unidadCompilacion
    }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>) {
        for (importacion in listaImportaciones) {
            importacion.llenarTablaSimbolos(tablaSimbolos)
        }
        clase?.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, Ambito(nombre))
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>) {
        clase?.analizarSemantica(tablaSimbolos, erroresSemanticos, Ambito(nombre))
    }
}