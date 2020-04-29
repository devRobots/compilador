package co.edu.uniquindio.app

import co.edu.uniquindio.lexico.AnalizadorLexico
import co.edu.uniquindio.sintaxis.AnalizadorSintactico
import co.edu.uniquindio.sintaxis.bnf.UnidadCompilacion
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.util.Callback
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 1.1
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
    @FXML lateinit var arbolSintactico: TreeView<String>

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
        val errores = analizadorLexico.listaErrores

        val tokensObservables: ObservableList<TokenObservable> = FXCollections.observableArrayList()
        for (token in tokens) {
            val observable = TokenObservable(token)
            tokensObservables.add(observable)
        }

        salidaLexico.items = tokensObservables
        salidaLexico.refresh()

        val erroresObservables: ObservableList<String> = FXCollections.observableArrayList()
        for (error in errores) {
            erroresObservables.add(error.toString())
        }

        erroresLexicos.items = erroresObservables
        erroresLexicos.refresh()

        // Analizador Sintactico
        val analizadorSintactico = AnalizadorSintactico(tokens)
        val unidadCompilacion: UnidadCompilacion? = analizadorSintactico.esUnidadDeCompilacion()
        if (unidadCompilacion != null) {
            contruirTreeView(unidadCompilacion)
        }

        // Salida final
        if(erroresLexicos.items.isNotEmpty()) {
            mensaje.text = "Se encontraron errores lexicos"
        }
        else {
            mensaje.text = ""
        }
    }

    /**
     * Limpia el campo de texto
     */
    @FXML
    fun limpiar() {
        texto.clear()
    }

    /**
     * Construye el arbol
     * en la interfaz
     */
    fun contruirTreeView(unidadCompilacion: UnidadCompilacion) {
        val nodo = TreeItem<String>("Unidad de Compilacion")
        agregarNodosHijo(nodo, unidadCompilacion)
        arbolSintactico.root = nodo
        arbolSintactico.refresh()
    }

    fun agregarNodosHijo(raiz: TreeItem<String>, objeto: Any?) {
        if (objeto != null) {
            val listaAtributos = objeto::class.memberProperties

            for (atributo in listaAtributos) {
                if (atributo.javaClass != ArrayList::class) {
                    val nodo = TreeItem(atributo.name)

                    val valor = objeto.javaClass.getDeclaredField(atributo.name)

                    agregarNodosHijo(nodo, valor)

                    raiz.children.add(nodo)
                } else {
                    val nodo = TreeItem(atributo.name)
                    val lista: ArrayList<Any> = objeto.javaClass.getField(atributo.name) as ArrayList<Any>

                    for (subatributo in lista) {
                        agregarNodosHijo(nodo, subatributo)
                    }

                    raiz.children.add(nodo)
                }
            }
        }
    }
}