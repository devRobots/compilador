package co.edu.uniquindio.app

import co.edu.uniquindio.lexico.AnalizadorLexico
import co.edu.uniquindio.lexico.ErrorLexico
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.AnalizadorSintactico
import co.edu.uniquindio.sintaxis.ErrorSintactico
import co.edu.uniquindio.sintaxis.bnf.UnidadCompilacion
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.util.Callback
import java.util.*


/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Controlador de la Ventana Principal
 */
class VentanaPrincipalController {
    /**
     * Codigo fuente
     */
    @FXML lateinit var texto: TextArea

    /**
     * Elementos del Analizador Lexico
     */
    @FXML lateinit var salidaLexico: TableView<TokenObservable>
    @FXML lateinit var palabra: TableColumn<TokenObservable, String?>
    @FXML lateinit var categoria: TableColumn<TokenObservable, String?>
    @FXML lateinit var fila: TableColumn<TokenObservable, String?>
    @FXML lateinit var columna: TableColumn<TokenObservable, String?>

    /**
     * Elementos del Analizador Sintactico
     */
    @FXML lateinit var arbolSintactico: TreeView<SintaxisObservable>
    @FXML lateinit var propertiesPanel: BorderPane

    /**
     * Elementos de Rutina de errores
     */
    @FXML lateinit var mensaje: Label
    @FXML lateinit var erroresLexicos: ListView<String>
    @FXML lateinit var erroresSintacticos: ListView<String>

    /**
     * Metodo initialize de JavaFX
     *
     * Inicializa las configuraciones basicas de los TableView
     */
    @FXML
    fun initialize() {
        palabra.cellValueFactory = Callback { token: TableColumn.CellDataFeatures<TokenObservable, String?> -> token.value.palabra }
        categoria.cellValueFactory = Callback { token: TableColumn.CellDataFeatures<TokenObservable, String?> -> token.value.categoria }
        fila.cellValueFactory = Callback { token: TableColumn.CellDataFeatures<TokenObservable, String?> -> token.value.fila }
        columna.cellValueFactory = Callback { token: TableColumn.CellDataFeatures<TokenObservable, String?> -> token.value.columna }


        arbolSintactico.setCellFactory {
            object : TreeCell<SintaxisObservable?>() {
                override fun updateItem(item: SintaxisObservable?, empty: Boolean) {
                    super.updateItem(item, empty)
                    text = item?.toString()
                }
            }
        }

        arbolSintactico.selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->
            val panel = observable.value.value.sintaxis.getPropertiesPanel()

            val title = GridPane()

            var texto = Label("Objeto:")
            texto.style = "-fx-font-weight: bold"
            texto.padding = Insets(10.0)

            GridPane.setHgrow(texto, Priority.ALWAYS)
            title.add(texto, 0, 0)

            var objeto = Label(observable.value.value.toString())
            objeto.style = "-fx-font-weight: bold"
            objeto.padding = Insets(10.0)

            GridPane.setHgrow(objeto, Priority.ALWAYS)
            title.add(objeto, 1, 0)

            propertiesPanel.top = title
            propertiesPanel.center = panel
        }
    }

    /**
     * Obtiene el codigo fuente y lo envia a los analizadores
     * lexico, sintactio y semantico para imprimir sus respectivas salidas
     */
    @FXML
    fun analizar() {
        val codigoFuente = texto.text

        // Analizador Lexico
        val analizadorLexico = AnalizadorLexico(codigoFuente)
        analizadorLexico.analizar()

        val tokens = analizadorLexico.listaTokens
        val listaErroresLexicos = analizadorLexico.listaErrores

        mostrarTokens(tokens)
        mostrarErroresLexicos(listaErroresLexicos)

        // Analizador Sintactico
        val analizadorSintactico = AnalizadorSintactico(tokens)

        val unidadCompilacion: UnidadCompilacion? = analizadorSintactico.esUnidadDeCompilacion()
        val listaErroresSintacticos = analizadorSintactico.listaErrores

        contruirTreeView(unidadCompilacion)
        mostrarErroresSintacticos(listaErroresSintacticos)

        // Salida final
        resolucionErrores(listaErroresLexicos, listaErroresSintacticos)
    }

    /**
     * Limpia el campo de texto
     */
    @FXML
    fun limpiar() {
        texto.clear()
        mensaje.text = ""
    }

    /**
     * Genera los tokens observables para posteriormente
     * mostrarlos en un TableView
     */
    private fun mostrarTokens(listaTokens: ArrayList<Token>) {
        val tokensObservables: ObservableList<TokenObservable> = FXCollections.observableArrayList()
        for (token in listaTokens) {
            val observable = TokenObservable(token)
            tokensObservables.add(observable)
        }

        salidaLexico.items = tokensObservables
        salidaLexico.refresh()
    }

    /**
     * Extrae el TreeItem de la Unidad de Compilacion
     * para crear el arbol sintactico en la interfaz
     */
    private fun contruirTreeView(unidadCompilacion: UnidadCompilacion?) {
        val treeItem = unidadCompilacion?.getTreeItem()
        treeItem?.expandedProperty()?.set(true)
        arbolSintactico.root = treeItem
        arbolSintactico.refresh()
    }

    /**
     * Genera los errores lexicos observables para posteriormente
     * mostrarlos en un ListView
     */
    private fun mostrarErroresLexicos(listaErrores: ArrayList<ErrorLexico>) {
        val erroresObservables = extraerErroresObservables(listaErrores as ArrayList<Any>)
        erroresLexicos.items = erroresObservables
        erroresLexicos.refresh()
    }

    /**
     * Genera los errores sintacticos observables para posteriormente
     * mostrarlos en un ListView
     */
    private fun mostrarErroresSintacticos(listaErrores: ArrayList<ErrorSintactico>) {
        val erroresObservables = extraerErroresObservables(listaErrores as ArrayList<Any>)
        erroresSintacticos.items = erroresObservables
        erroresSintacticos.refresh()
    }

    /**
     * Extrae el mensaje de cada error para ponerlos en una lista de
     * errores observables
     */
    private fun extraerErroresObservables(listaErrores: ArrayList<Any>): ObservableList<String> {
        val erroresObservables: ObservableList<String> = FXCollections.observableArrayList()
        for (error in listaErrores) {
            erroresObservables.add(error.toString())
        }

        return erroresObservables
    }

    /**
     * Interpreta el flujo de errore
     */
    private fun resolucionErrores(erroresLexicos: ArrayList<ErrorLexico>, erroresSintacticos: ArrayList<ErrorSintactico>) {
        mensaje.text = ""

        if(erroresLexicos.isNotEmpty()) {
            if (erroresLexicos.size == 1) {
                mensaje.text += "Se encontro 1 error lexico"
            } else {
                mensaje.text += "Se encontraron ${erroresLexicos.size} errores lexicos"
            }
            mensaje.text += "\n"
        }
        if (erroresSintacticos.isNotEmpty()) {
            if (erroresSintacticos.size == 1) {
                mensaje.text += "Se encontro 1 error sintactico"
            } else {
                mensaje.text += "Se encontraron ${erroresSintacticos.size} errores sintacticos"
            }
        }
    }
}