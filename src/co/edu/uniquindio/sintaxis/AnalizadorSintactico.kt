package co.edu.uniquindio.sintaxis

import kotlin.collections.ArrayList

import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.lexico.Categoria

import co.edu.uniquindio.sintaxis.bnf.*

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Analizador Sintactico
 */
class AnalizadorSintactico(private val tokens: ArrayList<Token>) {
    /**
     * Arbol sintactico y lista de errores por el analizador sintactico
     */
    val listaErrores = ArrayList<ErrorSintactico>()

    /**
     * Elementos necesarios del analizador lexico
     */
    private var posicionActual = 0
    private var tokenActual: Token? = tokens[0]

    /**
     * Metodo que avanza al siguiente token
     *
     * Configura la posicion actual
     * Verifica que no se desborde
     */
    private fun siguienteToken(){
        posicionActual++

        tokenActual = if( posicionActual < tokens.size ) {
            tokens[posicionActual]
        } else {
            null
        }
    }

    /**
     * Metodo que devuelve el token hasta una posicion deseada
     *
     * Configura la posicion actual
     * Verifica que no se desborde
     */
    private fun backtracking(posicion: Int){
        posicionActual = posicion - 1
        siguienteToken()
    }

    /**
     * Metodo que reporta un error y lo agrega
     * a la lista de errores
     */
    private fun reportarError(mensaje: String) {
        val error = ErrorSintactico("$mensaje en ${tokenActual?.fila}:${tokenActual?.columna}")
        listaErrores.add(error)
    }

    /**
     * Metodo que avanza al siguiente token
     *
     * Configura la posicion actual
     * Verifica que no se desborde
     */
    fun esUnidadDeCompilacion(): UnidadCompilacion? {
        val paquete: Paquete? = esPaquete()
        val listaImportaciones: ArrayList<Importacion> = esListaImportaciones()
        val clase = esClase()

        return if (clase != null) {
            UnidadCompilacion(paquete, listaImportaciones, clase)
        } else {
            null
        }
    }

    private fun esPaquete(): Paquete? {
        if (tokenActual?.categoria == Categoria.PALABRA_RESERVADA && tokenActual?.lexema == "caja") {
            siguienteToken()
            if (tokenActual?.categoria == Categoria.IDENTIFICADOR) {
                val paquete = tokenActual!!
                siguienteToken()
                if (tokenActual?.categoria == Categoria.FIN_SENTENCIA) {
                    siguienteToken()
                    return Paquete(paquete)
                } else {
                    reportarError("Se esperaba un terminal")
                }
            } else{
                reportarError("Se esperaba un identificador")
            }
        }

        return null
    }

    private fun esListaImportaciones(): ArrayList<Importacion> {
        val lista: ArrayList<Importacion> = ArrayList<Importacion>()

        var importacion: Importacion? = esImportacion()
        while (importacion != null) {
            lista.add(importacion)
            importacion = esImportacion()
        }

        return lista
    }

    private fun esImportacion(): Importacion? {
        if (tokenActual?.categoria == Categoria.PALABRA_RESERVADA && tokenActual?.lexema == "meter") {
            siguienteToken()
            if (tokenActual?.categoria == Categoria.IDENTIFICADOR) {
                val importacion = tokenActual!!
                siguienteToken()
                if (tokenActual?.categoria == Categoria.FIN_SENTENCIA) {
                    siguienteToken()
                    return Importacion(importacion)
                } else {
                    reportarError("Se esperaba un terminal")
                }
            } else {
                reportarError("Se esperaba un identificador")
            }
        }
        return null
    }

    private fun esClase(): Clase? {
        var modificadorAcceso: Token? = null

        if (tokenActual?.categoria == Categoria.PALABRA_RESERVADA) {
            if (tokenActual?.lexema == "estrato1" || tokenActual?.lexema == "estrato6") {
                modificadorAcceso = tokenActual
                siguienteToken()
            }
        }

        if (tokenActual?.categoria == Categoria.PALABRA_RESERVADA && tokenActual?.lexema == "cosa") {
            siguienteToken()
            if (tokenActual?.categoria == Categoria.IDENTIFICADOR) {
                val identificador = tokenActual!!
                siguienteToken()

                val cuerpoClase: CuerpoClase? = esCuerpoClase()

                return Clase(modificadorAcceso, identificador, cuerpoClase)
            }
            else {
                reportarError("Se esperaba un identificador")
            }
        }

        return null
    }

    private fun esCuerpoClase(): CuerpoClase? {
        if (tokenActual?.categoria == Categoria.LLAVE_IZQUIERDO) {
            siguienteToken()

            val listaBloqueSentencias = esListaBloqueSentencias()

            if (tokenActual?.categoria == Categoria.LLAVE_DERECHA) {
                siguienteToken()
                return CuerpoClase(listaBloqueSentencias)
            } else {
                reportarError("Se esperaba una llave izquierda")
            }
        }

        return null
    }

    private fun esListaBloqueSentencias(): ArrayList<BloqueSentencia> {
        val lista = ArrayList<BloqueSentencia>()

        var bloque: BloqueSentencia? = esBloqueSentencia()
        while (bloque != null) {
            lista.add(bloque)
            println(tokenActual)
            bloque = esBloqueSentencia()
        }

        return lista
    }

    private fun esBloqueSentencia(): BloqueSentencia? {
        val posicionInicial = posicionActual

        val clase = esClase()
        if (clase != null) {
            return clase
        } else {
            backtracking(posicionInicial)
        }

        val metodo = esMetodo()
        if (metodo != null) {
            return metodo
        } else {
            backtracking(posicionInicial)
        }

        val funcion = esFuncion()
        if (funcion != null) {
            return funcion
        } else {
            backtracking(posicionInicial)
        }

        val variableGlobal = esVariableGlobal()
        if (variableGlobal != null) {
            return variableGlobal
        } else {
            backtracking(posicionInicial)
        }

        return null
    }

    private fun esMetodo(): Metodo? {
        var modificadorAcceso: Token? = null

        if (tokenActual?.categoria == Categoria.PALABRA_RESERVADA) {
            if (tokenActual?.lexema == "estrato1" || tokenActual?.lexema == "estrato6") {
                modificadorAcceso = tokenActual
                siguienteToken()
            }
        }

        if (tokenActual?.categoria == Categoria.IDENTIFICADOR) {
            val identificador = tokenActual!!
            siguienteToken()

            if (tokenActual?.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                siguienteToken()

                val listaArgumentos = esListaArgumentos()

                if (tokenActual?.categoria == Categoria.PARENTESIS_DERECHO) {
                    siguienteToken()
                    if (tokenActual?.categoria == Categoria.LLAVE_IZQUIERDO) {
                        siguienteToken()

                        val listaBloqueInstrucciones = esListaBloqueInstrucciones()

                        if (tokenActual?.categoria == Categoria.LLAVE_DERECHA) {
                            siguienteToken()
                            return Metodo(modificadorAcceso, identificador , listaArgumentos , listaBloqueInstrucciones)
                        } else {
                            reportarError("Se esperaba una llave derecha")
                        }
                    } else {
                        reportarError("Se esperaba una llave izquierda")
                    }
                } else {
                    reportarError("Se esperaba un parentesis derecho")
                }
            } else {
                reportarError("Se esperaba un parentesis izquierdo")
            }
        }

        return null
    }

    private fun esFuncion(): Funcion? {
        var modificadorAcceso: Token? = null

        if (tokenActual?.categoria == Categoria.PALABRA_RESERVADA) {
            if (tokenActual?.lexema == "estrato1" || tokenActual?.lexema == "estrato6") {
                modificadorAcceso = tokenActual
                siguienteToken()
            }
        }

        val tipoDato = esTipoDato()
        if (tipoDato != null) {
            if (tokenActual?.categoria == Categoria.IDENTIFICADOR) {
                val identificador = tokenActual!!
                siguienteToken()

                if (tokenActual?.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                    siguienteToken()

                    val listaArgumentos = esListaArgumentos()

                    if (tokenActual?.categoria == Categoria.PARENTESIS_DERECHO) {
                        siguienteToken()
                        if (tokenActual?.categoria == Categoria.LLAVE_IZQUIERDO) {
                            siguienteToken()

                            val listaBloqueInstrucciones = esListaBloqueInstrucciones()

                            val retorno = esRetorno()

                            if (retorno != null) {
                                if (tokenActual?.categoria == Categoria.LLAVE_DERECHA) {
                                    siguienteToken()
                                    return Funcion(modificadorAcceso, tipoDato, identificador , listaArgumentos , listaBloqueInstrucciones, retorno)
                                } else {
                                    reportarError("Se esperaba una llave derecha")
                                }
                            } else {
                                reportarError("Se esperaba una sentencia de retorno")
                            }
                        } else {
                            reportarError("Se esperaba una llave izquierda")
                        }
                    } else {
                        reportarError("Se esperaba un parentesis derecho")
                    }
                } else {
                    reportarError("Se esperaba un parentesis izquierdo")
                }
            } else {
                reportarError("Se esperaba un identificador")
            }
        }

        return null
    }

    private fun esListaArgumentos(): ArrayList<Argumento> {
        val lista = ArrayList<Argumento>()

        var argumento: Argumento? = esArgumento()
        while (argumento != null ) {
            lista.add(argumento)

            argumento = if (tokenActual?.categoria == Categoria.SEPARADOR) {
                siguienteToken()
                esArgumento()
            } else {
                null
            }
        }

        return lista
    }

    private fun esArgumento(): Argumento? {
        val tipoDato = esTipoDato()
        if (tipoDato != null) {
            if (tokenActual?.categoria == Categoria.IDENTIFICADOR) {
                val identificador = tokenActual!!
                siguienteToken()
                return Argumento(tipoDato, identificador)
            }
        }

        return null
    }

    private fun esListaBloqueInstrucciones(): ArrayList<BloqueInstrucciones> {
        val lista = ArrayList<BloqueInstrucciones>()

        var bloque = esBloqueIntrucciones()
        while (bloque != null) {
            lista.add(bloque)
            bloque = esBloqueIntrucciones()
        }

        return lista
    }

    private fun esBloqueIntrucciones(): BloqueInstrucciones? {
        TODO("Falta")
        return null
    }

    private fun esTipoDato(): TipoDato? {
        if (tokenActual?.categoria == Categoria.PALABRA_RESERVADA) {
            var esTipo = when (tokenActual!!.lexema) {
                "ent", "dec", "pal", "bip", "bit" -> true
                else -> false
            }

            if (esTipo) {
                val tipo = TipoDato(tokenActual!!)
                siguienteToken()
                return tipo
            } else {
                reportarError("Se esperaba un tipo de dato")
            }
        }

        return null
    }

    private fun esRetorno(): Retorno? {
        if (tokenActual?.categoria == Categoria.PALABRA_RESERVADA && tokenActual?.lexema == "devolver") {
            siguienteToken()
            TODO("Corregir")
            val expresion = ExpresionAritmetica(null, null, null, ValorNumerico(Token("", Categoria.OPERADOR_ARITMETICO, 0, 0), null, null, Token("", Categoria.IDENTIFICADOR, 0, 0)))

            if (expresion != null) {
                siguienteToken()
                if (tokenActual?.categoria == Categoria.FIN_SENTENCIA) {
                    siguienteToken()
                    return Retorno(expresion)
                } else {
                    reportarError("Se esperaba un final de sentencia")
                }
            } else {
                reportarError("Se esperaba una expresion, valor o indentificador")
            }
        }

        return null
    }

    private fun esVariableGlobal(): BloqueSentencia? {
        TODO("Falta")
        return null
    }

    /*
    Metodo para Determinar si es una Expresion
     */
    fun esExpresion(): Expresion? {
        val expAritmetica = esExpresionAritmetica()
        if (expAritmetica != null){
            return expAritmetica
        }
        val expRelacional = esExpresionRelacional()
        if (expRelacional != null){
            return expRelacional
        }
        val expLogica = esExpresionLogica()
        if (expLogica != null){
            return expLogica
        }
        val expCadena = esExpresionCadena()
        if (expCadena != null){
            return expCadena
        }
        return null
    }
    /*
    metodo para Determinar si es una Expresion Aritmetica
     */
    fun esExpresionAritmetica(): ExpresionAritmetica? {
        val izq: ExpresionAritmetica? = esExpresionAritmetica()
        if( izq != null ){
            if (tokenActual?.categoria == Categoria.OPERADOR_ARITMETICO) {
                val operador: Token? = tokenActual
                siguienteToken()
                val der: ExpresionAritmetica? = esExpresionAritmetica()
                if (der != null) {
                    return ExpresionAritmetica(izq, operador, der)
                }
            }
        }else if (tokenActual?.categoria == Categoria.PARENTESIS_IZQUIERDO) {
            siguienteToken()
            val eap: ExpresionAritmetica? = esExpresionAritmetica()
            if (eap != null) {
                if (tokenActual?.categoria == Categoria.PARENTESIS_DERECHO) {
                    siguienteToken()
                    return ExpresionAritmetica(eap)
                }
            }
        } else {
            val valor: ValorNumerico? = esValorNumerico()
            if (valor != null) {
                siguienteToken()
                return ExpresionAritmetica(valor)
            }
        }
        return null
    }

    /*
    Es valor numerico
     */
    fun esValorNumerico(): ValorNumerico?{
        var signo : Token? = null
        if (tokenActual?.categoria == Categoria.OPERADOR_ARITMETICO && (tokenActual?.lexema == "-" ||  tokenActual?.lexema == "+")){
            signo = tokenActual
            siguienteToken()
        }
        var esTipo = when(tokenActual?.categoria) {
            Categoria.ENTERO, Categoria.REAL, Categoria.IDENTIFICADOR -> true
            else -> false
        }
        if (esTipo){
            val num = tokenActual
            return ValorNumerico(signo,num)
        }
        return null
    }

    /**
     * Metodo Expresion Relacional
     */

    fun esExpresionRelacional(): ExpresionRelacional? {
        return null
    }

    /**
     * Metodo Expresion Logica
     */
    fun esExpresionLogica(): ExpresionLogica? {
        return null
    }

    /*
     Es Expresion Relacional
     */
    fun esExpresionCadena(): ExpresionCadena? {
        if (tokenActual != null){

        }
        if (tokenActual?.categoria != Categoria.CADENA_CARACTERES){

        }
        return null
    }
}