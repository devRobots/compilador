package co.edu.uniquindio.sintaxis.bnf.expresion

import co.edu.uniquindio.app.observable.SintaxisObservable
import co.edu.uniquindio.lexico.Categoria
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.semantica.simbolo.Simbolo
import co.edu.uniquindio.sintaxis.bnf.otro.ValorNumerico
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Expresion Aritmetica
 */
class ExpresionAritmetica(
        private val izquierda: ExpresionAritmetica?,
        private val derecho: ExpresionAritmetica?,
        private val operador: Token?,
        private val valor: ValorNumerico?
) : Expresion("Expresion Aritmetica") {

    override fun toString(): String {
        var estructura: String = valor?.toString() ?: izquierda.toString()
        if (operador != null) {
            estructura += "${operador.lexema}$derecho"
        }
        return estructura
    }

    constructor(valor: ValorNumerico?, operador: Token, derecho: ExpresionAritmetica?) : this(null, derecho, operador, valor)
    constructor(izquierda: ExpresionAritmetica?, operador: Token, derecho: ExpresionAritmetica?) : this(izquierda, derecho, operador, null)
    constructor(expresionAritmetica: ExpresionAritmetica) : this(expresionAritmetica, null, null, null)
    constructor(valor: ValorNumerico?) : this(null, null, null, valor)

    /**
     * Obtiene el nodo TreeItem necesario para la construccion
     * de la vista del arbol sintactico
     *
     * @return TreeItem<SintaxisObservable> El nodo TreeItem
     */
    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (valor != null) {
            treeItem.children.add(valor.getTreeItem())
        }
        if (izquierda != null) {
            treeItem.children.add(izquierda.getTreeItem())
        }
        if (derecho != null) {
            treeItem.children.add(derecho.getTreeItem())
        }

        return treeItem
    }

    /**
     * Obtiene el panel necesario para mostrar la informacion
     * de la estructura sintactica en la interfaz
     *
     * @return GridPane el panel que se mostrara en pantalla
     */
    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Valor")
        agregarValor(valor?.toString())

        agregarAtributo("Izquierda")
        agregarValor(izquierda?.toString())

        agregarAtributo("Operador")
        agregarValor(operador?.lexema)

        agregarAtributo("Derecha")
        agregarValor(derecho?.toString())

        configurarTabla()
        return panel
    }

    /**
     * Metodo para Determinar si es una Expresion Aritmetica
     * <ExpAritmetica> ::= “[“ <ExpAritmetica> “]” [ operadorAritmetico <ExpAritmetica>] |
     * <ValorNumerico> [operadorAritmetico <ExpAritmetica>]
     *
     * @return ExpresionAritmetica si existe, null sino existe
     */
    override fun obtenerTipoDato(tablaSimbolos: TablaSimbolos, ambito: Ambito): String {
        if (izquierda != null) {
            var izq:String = izquierda.obtenerTipoDato(tablaSimbolos,ambito)
            if (derecho != null) {
                var der:String = derecho.obtenerTipoDato(tablaSimbolos,ambito)
                if(izq == "dec" || der == "dec"){
                    return "dec"
                }else if(izq != "null" || der != "null"){
                    return "ent"
                }else{
                    return "null"
                }
            }else{
                return izq
            }
        }else{
            var tipo: String = "null"
            if(valor != null){
                if(valor.identificador.categoria == Categoria.ENTERO){
                    tipo = "ent"
                }else if(valor.identificador.categoria == Categoria.REAL){
                    tipo = "dec"
                }else{
                    val simbolo = tablaSimbolos.buscarVariable(valor.identificador.lexema,ambito)
                    if(simbolo != null){
                        tipo = simbolo.tipoDato
                    }
                }
                if(derecho != null){
                    var der: String = derecho.obtenerTipoDato(tablaSimbolos,ambito)
                    if(tipo == "dec" || der == "dec"){
                        tipo = "dec"
                    }else if(tipo != "null" || der != "null"){
                        tipo= "ent"
                    }
                }
            }
            return tipo
        }
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        izquierda?.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        derecho?.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        valor?.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
    }

    override fun getJavaCode(): String {
        var codigo = ""
        if (izquierda != null) {
            codigo += "(${izquierda.getJavaCode()}) "
            if (operador != null) {
                codigo += operador.getJavaCode()
                if (derecho != null) {
                    codigo += derecho.getJavaCode()
                    return codigo
                }
            } else {
                return codigo
            }
            codigo += ")"
        } else {
            if (valor != null) {
                codigo += valor?.getJavaCode()
                if (operador != null) {
                    codigo += operador.getJavaCode()
                    if (derecho != null) {
                        codigo += derecho.getJavaCode()
                        return codigo
                    }
                } else {
                    return codigo
                }
            }
        }
        return codigo
    }
}
