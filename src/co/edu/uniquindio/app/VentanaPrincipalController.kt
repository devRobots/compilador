package co.edu.uniquindio.app

import co.edu.uniquindio.lexico.AnalizadorLexico
import co.edu.uniquindio.lexico.ErrorLexico
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.AnalizadorSintactico
import co.edu.uniquindio.sintaxis.ErrorSintactico
import co.edu.uniquindio.sintaxis.bnf.unidad.UnidadCompilacion
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.util.Callback


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
    @FXML
    lateinit var texto: TextArea

    /**
     * Elementos del Analizador Lexico
     */
    @FXML
    lateinit var salidaLexico: TableView<TokenObservable>

    @FXML
    lateinit var palabra: TableColumn<TokenObservable, String?>

    @FXML
    lateinit var categoria: TableColumn<TokenObservable, String?>

    @FXML
    lateinit var fila: TableColumn<TokenObservable, String?>

    @FXML
    lateinit var columna: TableColumn<TokenObservable, String?>

    /**
     * Elementos del Analizador Sintactico
     */
    @FXML
    lateinit var arbolSintactico: TreeView<SintaxisObservable>

    @FXML
    lateinit var propertiesPanel: BorderPane

    /**
     * Elementos del Analizador Semantico
     */
    @FXML
    lateinit var tablaVariables: TableView<TokenObservable>

    @FXML
    lateinit var tablaFunciones: TableView<TokenObservable>

    @FXML
    lateinit var tablaImportaciones: TableView<TokenObservable>

    /**
     * Elementos de Rutina de errores
     */
    @FXML
    lateinit var mensaje: Label

    @FXML
    lateinit var erroresLexicos: ListView<String>

    @FXML
    lateinit var erroresSintacticos: ListView<String>

    @FXML
    lateinit var panelErroresLexicos: TitledPane

    @FXML
    lateinit var panelErroresSintacticos: TitledPane

    /**
     * Metodo initialize de JavaFX
     * Inicializa las configuraciones basicas de los TableView, TreeView, etc
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

        arbolSintactico.selectionModel.selectedItemProperty().addListener { observable, _, _ ->
            if (observable.value != null) {
                val sintax = observable.value.value.sintaxis

                val panel = sintax.getPropertiesPanel()

                val title = GridPane()

                val texto = Label("Objeto:")
                texto.style = "-fx-font-weight: bold"
                texto.padding = Insets(10.0)

                GridPane.setHgrow(texto, Priority.ALWAYS)
                title.add(texto, 0, 0)

                val objeto = Label(sintax.nombre)
                objeto.style = "-fx-font-weight: bold"
                objeto.padding = Insets(10.0)

                GridPane.setHgrow(objeto, Priority.ALWAYS)
                title.add(objeto, 1, 0)

                propertiesPanel.top = title
                propertiesPanel.center = panel
            }
        }
    }

    /**
     * Obtiene el codigo fuente y lo envia a los analizadores
     * lexico, sintactio y semantico para imprimir sus respectivas salidas
     */
    @FXML
    fun analizar() {
        limpiarSalidas()
        val codigoFuente = texto.text

        if (codigoFuente.isNotEmpty()) {
            val analizadorLexico = AnalizadorLexico(codigoFuente)
            analizadorLexico.analizar()

            val tokens = analizadorLexico.listaTokens
            val listaErroresLexicos = analizadorLexico.listaErrores

            mostrarTokens(tokens)
            mostrarErroresLexicos(listaErroresLexicos)

            if (listaErroresLexicos.isEmpty()) {
                val analizadorSintactico = AnalizadorSintactico(tokens)

                val unidadCompilacion: UnidadCompilacion? = analizadorSintactico.esUnidadDeCompilacion()
                val listaErroresSintacticos = analizadorSintactico.listaErrores

                if (unidadCompilacion != null) {
                    contruirTreeView(unidadCompilacion)
                }
                mostrarErroresSintacticos(listaErroresSintacticos)

                if (mensaje.text.isBlank()) {
                    mensaje.text = "Se completo el analisis sintactico"
                    mensaje.style = "-fx-text-fill: darkgreen;"
                }
            }
        }
    }

    /**
     * Limpia todos los elementos de la interfaz
     */
    @FXML
    fun limpiar() {
        texto.clear()
        limpiarSalidas()
    }

    /**
     * Limpia las salidas de la interfaz
     */
    private fun limpiarSalidas() {
        mensaje.text = ""

        salidaLexico.items.clear()
        salidaLexico.refresh()

        erroresLexicos.items.clear()
        erroresLexicos.refresh()

        arbolSintactico.root = null
        arbolSintactico.refresh()

        erroresSintacticos.items.clear()
        erroresSintacticos.refresh()

        panelErroresLexicos.style = "-fx-text-fill: black;"
        panelErroresSintacticos.style = "-fx-text-fill: black;"

        panelErroresLexicos.text = "No se encontraron errores lexicos"
        panelErroresSintacticos.text = "No se encontraron errores sintacticos"
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
    private fun contruirTreeView(unidadCompilacion: UnidadCompilacion) {
        val treeItem = unidadCompilacion.getTreeItem()
        treeItem.expandedProperty().set(true)
        arbolSintactico.root = treeItem
        arbolSintactico.refresh()
    }

    /**
     * Genera los errores lexicos observables para posteriormente
     * mostrarlos en un ListView
     */
    private fun mostrarErroresLexicos(listaErrores: ArrayList<ErrorLexico>) {
        val errores: ArrayList<*> = listaErrores
        val erroresObservables = extraerErroresObservables(errores)
        mostrarMensajeErrores(errores, panelErroresLexicos, "lexico")
        erroresLexicos.items = erroresObservables
        erroresLexicos.refresh()
    }

    /**
     * Genera los errores sintacticos observables para posteriormente
     * mostrarlos en un ListView
     */
    private fun mostrarErroresSintacticos(listaErrores: ArrayList<ErrorSintactico>) {
        val errores: ArrayList<*> = listaErrores
        val erroresObservables = extraerErroresObservables(errores)
        mostrarMensajeErrores(errores, panelErroresSintacticos, "sintactico")
        erroresSintacticos.items = erroresObservables
        erroresSintacticos.refresh()
    }

    /**
     * Extrae el mensaje de cada error para ponerlos en una lista de
     * errores observables
     */
    private fun extraerErroresObservables(listaErrores: ArrayList<*>): ObservableList<String> {
        val erroresObservables: ObservableList<String> = FXCollections.observableArrayList()
        for (error in listaErrores) {
            erroresObservables.add(error.toString())
        }

        return erroresObservables
    }

    /**
     * Muestra el flujo de errores correspondiente en el Label mensaje
     */
    private fun mostrarMensajeErrores(errores: ArrayList<*>, panel: TitledPane, tipo: String) {
        if (errores.isNotEmpty()) {
            panel.style = "-fx-text-fill: red;"
            mensaje.style = "-fx-text-fill: red;"

            if (errores.size == 1) {
                val texto = "Se encontro 1 error $tipo"
                mensaje.text = texto
                panel.text = texto
            } else {
                val texto = "Se encontraron ${errores.size} errores ${tipo}s"
                mensaje.text = texto
                panel.text = texto
            }
        }
    }
}