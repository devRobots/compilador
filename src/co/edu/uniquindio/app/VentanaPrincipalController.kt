package co.edu.uniquindio.app

import co.edu.uniquindio.app.observable.*
import co.edu.uniquindio.lexico.AnalizadorLexico
import co.edu.uniquindio.lexico.ErrorLexico
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.AnalizadorSemantico
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.simbolo.Funcion
import co.edu.uniquindio.semantica.simbolo.Importacion
import co.edu.uniquindio.semantica.simbolo.Simbolo
import co.edu.uniquindio.semantica.simbolo.Variable
import co.edu.uniquindio.sintaxis.AnalizadorSintactico
import co.edu.uniquindio.sintaxis.ErrorSintactico
import co.edu.uniquindio.sintaxis.bnf.unidad.UnidadCompilacion
import javafx.collections.FXCollections
import javafx.collections.FXCollections.observableArrayList
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.util.Callback
import java.io.File


/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 3.1
 *
 * Controlador de la Ventana Principal
 */
class VentanaPrincipalController {
    /**
     * Codigo fuente
     */
    @FXML lateinit var texto: TextArea
    @FXML lateinit var salida: TextArea
    @FXML lateinit var btnEjecutar: Button

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
     * Elementos del Analizador Semantico
     */
    @FXML lateinit var tablaVariables: TableView<VariableObservable>
    @FXML lateinit var accesoVar: TableColumn<VariableObservable, String?>
    @FXML lateinit var tipoVar: TableColumn<VariableObservable, String?>
    @FXML lateinit var nombreVar: TableColumn<VariableObservable, String?>
    @FXML lateinit var ambVar: TableColumn<VariableObservable, String?>
    @FXML lateinit var filVar: TableColumn<VariableObservable, String?>
    @FXML lateinit var colVar: TableColumn<VariableObservable, String?>

    @FXML lateinit var tablaFunciones: TableView<FuncionObservable>
    @FXML lateinit var accFun: TableColumn<FuncionObservable, String?>
    @FXML lateinit var tipoFun: TableColumn<FuncionObservable, String?>
    @FXML lateinit var nombreFun: TableColumn<FuncionObservable, String?>
    @FXML lateinit var paramsFun: TableColumn<FuncionObservable, String?>

    @FXML lateinit var tablaImportaciones: TableView<ImportacionObservable>
    @FXML lateinit var nombreImport: TableColumn<ImportacionObservable, String?>
    @FXML lateinit var filImport: TableColumn<ImportacionObservable, String?>
    @FXML lateinit var colImport: TableColumn<ImportacionObservable, String?>

    /**
     * Elementos de Rutina de errores
     */
    @FXML lateinit var mensaje: Label
    @FXML lateinit var erroresLexicos: ListView<String>
    @FXML lateinit var erroresSintacticos: ListView<String>
    @FXML lateinit var erroresSemanticos: ListView<String>
    @FXML lateinit var panelErroresLexicos: TitledPane
    @FXML lateinit var panelErroresSintacticos: TitledPane
    @FXML lateinit var panelErroresSemanticos: TitledPane

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

        accesoVar.cellValueFactory = Callback { variable: TableColumn.CellDataFeatures<VariableObservable, String?> -> variable.value.acceso }
        nombreVar.cellValueFactory = Callback { variable: TableColumn.CellDataFeatures<VariableObservable, String?> -> variable.value.nombre }
        filVar.cellValueFactory = Callback { variable: TableColumn.CellDataFeatures<VariableObservable, String?> -> variable.value.fila }
        colVar.cellValueFactory = Callback { variable: TableColumn.CellDataFeatures<VariableObservable, String?> -> variable.value.columna }
        ambVar.cellValueFactory = Callback { variable: TableColumn.CellDataFeatures<VariableObservable, String?> -> variable.value.ambito }
        tipoVar.cellValueFactory = Callback { variable: TableColumn.CellDataFeatures<VariableObservable, String?> -> variable.value.tipo }

        tipoFun.cellValueFactory = Callback { funcion: TableColumn.CellDataFeatures<FuncionObservable, String?> -> funcion.value.tipo }
        nombreFun.cellValueFactory = Callback { funcion: TableColumn.CellDataFeatures<FuncionObservable, String?> -> funcion.value.nombre }
        accFun.cellValueFactory = Callback { funcion: TableColumn.CellDataFeatures<FuncionObservable, String?> -> funcion.value.acceso }
        paramsFun.cellValueFactory = Callback { funcion: TableColumn.CellDataFeatures<FuncionObservable, String?> -> funcion.value.parametros }

        nombreImport.cellValueFactory = Callback { importacion: TableColumn.CellDataFeatures<ImportacionObservable, String?> -> importacion.value.nombre }
        filImport.cellValueFactory = Callback { importacion: TableColumn.CellDataFeatures<ImportacionObservable, String?> -> importacion.value.fila }
        colImport.cellValueFactory = Callback { importacion: TableColumn.CellDataFeatures<ImportacionObservable, String?> -> importacion.value.columna }
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

                if (listaErroresSintacticos.isEmpty() && unidadCompilacion != null) {
                    val analizadorSemantico = AnalizadorSemantico(unidadCompilacion)
                    analizadorSemantico.analizar()

                    val simbolos = analizadorSemantico.tablaSimbolos.listaSimbolos
                    mostrarSimbolos(simbolos)

                    val listaErroresSemanticos = analizadorSemantico.erroresSemanticos
                    mostrarErroresSemantico(listaErroresSemanticos)

                    if (listaErroresSemanticos.isEmpty()) {
                        salida.text = unidadCompilacion.getJavaCode()

                        if (mensaje.text.isBlank()) {
                            mensaje.text = "¡Se completo la compilacion!"
                            mensaje.style = "-fx-text-fill: darkgreen;"
                        }
                    }
                }
            }
        }
    }

    @FXML
    fun ejecutar() {
        btnEjecutar.disableProperty().set(true)
        btnEjecutar.text = "Exportando..."

        val alerta = Alert(Alert.AlertType.INFORMATION)
        alerta.headerText = "Verdad que te engañe?"
        alerta.contentText = "xdxdxd"
        alerta.show()

        if (salida.text.isNotBlank()) {
            File("src/Principal.java").writeText( salida.text )
            try {
                val p1 = Runtime.getRuntime().exec("javac src/Principal.java")
                p1.waitFor()
                btnEjecutar.text = "Ejecutando..."
                val p2 = Runtime.getRuntime().exec("java Principal", null, File("src"))
                p2.waitFor()
            } catch (ea: Exception) {
                ea.printStackTrace()
            }
        }

        btnEjecutar.text = "Exportar y Ejecutar"
        btnEjecutar.disableProperty().set(false)
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
        salida.clear()

        salidaLexico.items.clear()
        salidaLexico.refresh()

        erroresLexicos.items.clear()
        erroresLexicos.refresh()

        arbolSintactico.root = null
        arbolSintactico.refresh()

        erroresSintacticos.items.clear()
        erroresSintacticos.refresh()

        erroresSemanticos.items.clear()
        erroresSemanticos.refresh()

        panelErroresLexicos.style = "-fx-text-fill: black;"
        panelErroresSintacticos.style = "-fx-text-fill: black;"
        panelErroresSemanticos.style = "-fx-text-fill: black;"

        panelErroresLexicos.text = "No se encontraron errores lexicos"
        panelErroresSintacticos.text = "No se encontraron errores sintacticos"
        panelErroresSemanticos.text = "No se encontraron errores semanticos"
    }

    /**
     * Genera los tokens observables para posteriormente
     * mostrarlos en un TableView
     */
    private fun mostrarTokens(listaTokens: ArrayList<Token>) {
        val tokensObservables: ObservableList<TokenObservable> = observableArrayList()
        for (token in listaTokens) {
            val observable = TokenObservable(token)
            tokensObservables.add(observable)
        }

        salidaLexico.items = tokensObservables
        salidaLexico.refresh()
    }

    /**
     * Genera las variables, funciones e importaciones observables para
     * posteriormente mostrarlos cada uno en un TableView
     */
    private fun mostrarSimbolos(lista: ArrayList<Simbolo>) {
        val variablesObservables: ObservableList<VariableObservable> = observableArrayList()
        val funcionesObservables: ObservableList<FuncionObservable> = observableArrayList()
        val importacionesObservables: ObservableList<ImportacionObservable> = observableArrayList()

        for (simbolo in lista) {
            when(simbolo) {
                is Variable     -> variablesObservables.add(VariableObservable(simbolo))
                is Funcion      -> funcionesObservables.add(FuncionObservable(simbolo))
                is Importacion  -> importacionesObservables.add(ImportacionObservable(simbolo))
            }
        }

        tablaVariables.items = variablesObservables
        tablaFunciones.items = funcionesObservables
        tablaImportaciones.items = importacionesObservables

        tablaVariables.refresh()
        tablaFunciones.refresh()
        tablaImportaciones.refresh()
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
     * Genera los errores sintacticos observables para posteriormente
     * mostrarlos en un ListView
     */
    private fun mostrarErroresSemantico(listaErrores: ArrayList<ErrorSemantico>) {
        val errores: ArrayList<*> = listaErrores
        val erroresObservables = extraerErroresObservables(errores)
        mostrarMensajeErrores(errores, panelErroresSemanticos, "semantico")
        erroresSemanticos.items = erroresObservables
        erroresSemanticos.refresh()
    }

    /**
     * Extrae el mensaje de cada error para ponerlos en una lista de
     * errores observables
     */
    private fun extraerErroresObservables(listaErrores: ArrayList<*>): ObservableList<String> {
        val erroresObservables: ObservableList<String> = observableArrayList()
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