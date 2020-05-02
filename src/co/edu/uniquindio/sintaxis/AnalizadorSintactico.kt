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
    private fun siguienteToken() {
        posicionActual++

        tokenActual = if (posicionActual < tokens.size) {
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
    private fun backtracking(posicion: Int) {
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
            } else {
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

                if (tokenActual?.categoria == Categoria.LLAVE_IZQUIERDO) {
                    siguienteToken()

                    val listaBloqueSentencias = esListaBloqueSentencias()

                    if (tokenActual?.categoria == Categoria.LLAVE_DERECHA) {
                        siguienteToken()
                        return Clase(modificadorAcceso, identificador, listaBloqueSentencias)
                    } else {
                        reportarError("Se esperaba una llave izquierda")
                    }
                } else {
                    reportarError("Se esperaba una llave izquierda")
                }
            } else {
                reportarError("Se esperaba un identificador")
            }
        }

        return null
    }

    private fun esListaBloqueSentencias(): ArrayList<BloqueSentencia> {
        val lista = ArrayList<BloqueSentencia>()

        var bloque: BloqueSentencia? = esBloqueSentencia()
        while (bloque != null) {
            lista.add(bloque)
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

        /*
        val variableGlobal = esVariableGlobal()
        if (variableGlobal != null) {
            return variableGlobal
        } else {
            backtracking(posicionInicial)
        }
        */

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
                            return Metodo(modificadorAcceso, identificador, listaArgumentos, listaBloqueInstrucciones)
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
                                    return Funcion(modificadorAcceso, tipoDato, identificador, listaArgumentos, listaBloqueInstrucciones, retorno)
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
        while (argumento != null) {
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

        /*
        var bloque = esBloqueIntrucciones()
        while (bloque != null) {
            lista.add(bloque)
            bloque = esBloqueIntrucciones()
        }
        */

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
            val expresion = esExpresion()

            if (expresion != null) {
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
    private fun esExpresion(): Expresion? {
        val expAritmetica = esExpresionAritmetica()
        if (expAritmetica != null) {
            return expAritmetica
        }
        val expLogica = esExpresionLogica()
        if (expLogica != null) {
            return expLogica
        }
        val expRelacional = esExpresionRelacional()
        if (expRelacional != null) {
            return expRelacional
        }
        val expCadena = esExpresionCadena()
        if (expCadena != null) {
            return expCadena
        }
        return null
    }

    /**
     * Metodo para Determinar si es una Expresion Aritmetica
     */
    private fun esExpresionAritmetica(): ExpresionAritmetica? {
        val valor: ValorNumerico? = esValorNumerico()
        if (valor != null) {
            if (tokenActual?.categoria == Categoria.OPERADOR_ARITMETICO) {
                val operador = tokenActual!!
                siguienteToken()

                val expresion = esExpresionAritmetica()
                if (expresion != null) {
                    return ExpresionAritmetica(valor, operador, expresion)
                }
            }
            return ExpresionAritmetica(valor)
        }

        if (tokenActual?.categoria == Categoria.PARENTESIS_IZQUIERDO) {
            siguienteToken()
            val eap = esExpresionAritmetica()
            if (eap != null) {
                if (tokenActual?.categoria == Categoria.PARENTESIS_DERECHO) {
                    siguienteToken()
                    if (tokenActual?.categoria == Categoria.OPERADOR_ARITMETICO) {
                        val operador = tokenActual!!
                        siguienteToken()

                        val derecha = esExpresionAritmetica()

                        return ExpresionAritmetica(eap, operador!!, derecha)
                    }
                    return ExpresionAritmetica(eap)
                }
            }
        }

        return null
    }


    /*
    Es valor numerico
     */
    fun esValorNumerico(): ValorNumerico? {
        var signo: Token? = null
        if (tokenActual?.categoria == Categoria.OPERADOR_ARITMETICO && (tokenActual?.lexema == "-" || tokenActual?.lexema == "+")) {
            signo = tokenActual
            siguienteToken()
        }
        var esTipo = when (tokenActual?.categoria) {
            Categoria.ENTERO, Categoria.REAL, Categoria.IDENTIFICADOR -> true
            else -> false
        }
        if (esTipo) {
            val num = tokenActual!!
            siguienteToken()
            return ValorNumerico(signo, num)
        }
        return null
    }

    /**
     * Metodo Expresion Relacional
     */

    fun esExpresionRelacional(): ExpresionRelacional? {
        val izq = esExpresionAritmetica()
        if (izq != null){
            siguienteToken()
            if (tokenActual?.categoria == Categoria.OPERADOR_RELACIONAL){
                siguienteToken()
                val der = esExpresionAritmetica()
                if (der != null){
                    siguienteToken()
                    return ExpresionRelacional(izq,der)
                }else{
                    reportarError("La operacion relacional no esta Correcta")
                }
            }else{
                reportarError("No tiene asignado Operador Relacional")
            }
        }
        return null
    }

    /**
     * Metodo Expresion Logica
     */
    fun esExpresionLogica(): ExpresionLogica? {
        if (tokenActual?.categoria == Categoria.OPERADOR_LOGICO && tokenActual?.lexema == "¬"){
            val negacion = tokenActual
            siguienteToken()
            return ExpresionLogica(esExpresionLogica(),null,negacion,null)
        }
        val valor = esValorLogico()
        if (valor != null){
            siguienteToken()
            if (tokenActual?.categoria == Categoria.OPERADOR_LOGICO ){
                val op = tokenActual
                siguienteToken()
                val der = esExpresionLogica()
                siguienteToken()
                if (der != null){
                    return ExpresionLogica(der,valor,op,null)
                }else{
                    reportarError("Expresion Logica incorrecta, mal asignacion en el operador logico binario")
                }
            }
            return ExpresionLogica(null,valor,null,null)
        }
        return null
    }

    /**
     *  Metodo para ver si es un valor logico
     */
    fun esValorLogico(): ValorLogico? {
        if (tokenActual?.categoria == Categoria.BOOLEANO  || tokenActual?.categoria == Categoria.IDENTIFICADOR  ){
            val valor = tokenActual
            siguienteToken()
            return ValorLogico(valor,null)
        }
        val exp = esExpresionRelacional()
        if (exp != null){
            siguienteToken()
            return ValorLogico(null ,exp)
        }
        return null
    }

    /*
     Es Expresion Relacional
     TODO: hay que agregar siguiente token???
     */
    fun esExpresionCadena(): ExpresionCadena? {
        if (tokenActual?.categoria != Categoria.CADENA_CARACTERES) {
            val cadena = tokenActual
            siguienteToken()
            if (tokenActual?.categoria == Categoria.OPERADOR_ARITMETICO && tokenActual?.lexema == "+") {
                siguienteToken()
                val expresion = esExpresion()
                if (expresion != null) {
                    return ExpresionCadena(cadena, expresion)
                } else {
                    reportarError(" la cadena no esta concatenada con nada")
                    return null
                }
            }
            return ExpresionCadena(cadena, null)
        }
        return null
    }

    fun esExpresionCadena2(): ExpresionCadena? {
        if (tokenActual?.categoria != Categoria.CADENA_CARACTERES) {
            val cadena = tokenActual
            siguienteToken()
            if (tokenActual?.categoria == Categoria.OPERADOR_ARITMETICO && tokenActual?.lexema == "+") {
                siguienteToken()
                val exp = esExpresionAritmetica()
                val exp2 = esExpresionRelacional()
                val exp3 = esExpresionLogica()
                if (exp != null || exp2 != null || exp3 != null) {
                    val expresion = esExpresion()
                    siguienteToken()
                    if (tokenActual?.categoria == Categoria.OPERADOR_ARITMETICO && tokenActual?.lexema == "+") {
                        siguienteToken()
                        val expres = esExpresion()
                        if (expres != null) {
                            return (ExpresionCadena(cadena, expres))
                        } else {
                            reportarError("No hay nada concatenado al + ")
                            return null
                        }
                    }
                    return ExpresionCadena(cadena, expresion)
                }
                reportarError("No hay nada concatenado al + ")
                return null
            }
            return ExpresionCadena(cadena, null)
        }
        return null
    }
}