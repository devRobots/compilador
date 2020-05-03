package co.edu.uniquindio.sintaxis

import kotlin.collections.ArrayList

import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.lexico.Categoria

import co.edu.uniquindio.sintaxis.bnf.*
import kotlin.math.exp

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

    /**
     * Metodo para Determinar si es una parquete
     * <DeclaracionPaquete> ::= caja identificador “!”
     */
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

    /**
     * Metodo para Determinar si es una Lista de Importaciones
     * <ListaImportaciones> ::= <Importacion> [<ListaImportaciones>]
     */
    private fun esListaImportaciones(): ArrayList<Importacion> {
        val lista = ArrayList<Importacion>()

        var importacion: Importacion? = esImportacion()
        while (importacion != null) {
            lista.add(importacion)
            importacion = esImportacion()
        }

        return lista
    }

    /**
     * Metodo para Determinar si es una Importacion
     * <Importacion> ::= meter identificador “!”
     */
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

    /**
     * Metodo para Determinar si es una clase
     * <DeclaracionClase> ::=  [ <ModificadorAcceso> ] cosa identificador “¿” [<ListaBloque>] “?”
     */
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

                    val listaBloque = esListaBloque()

                    if (tokenActual?.categoria == Categoria.LLAVE_DERECHA) {
                        siguienteToken()
                        return Clase(modificadorAcceso, identificador, listaBloque)
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

    /**
     * Metodo para Determinar si es una Lista de Bloques
     * <ListaSentenciaBloque> ::= <SentenciaBloque> [<ListaBloque>]
     */
    private fun esListaBloque(): ArrayList<Bloque> {
        val lista = ArrayList<Bloque>()

        var bloque: Bloque? = esBloque()
        while (bloque != null) {
            lista.add(bloque)
            bloque = esBloque()
        }

        return lista
    }

    /**
     * Metodo para Determinar si es un bloque
     * <Bloque>::= <ListaClases> | <ListaFunciones> | <ListaVariablesGlobales>
     *     <DeclaracionVariableGlobal>
     */
    private fun esBloque(): Bloque? {
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

    /**
     * Metodo Para Determinar si es un Metodo
     * <Metodo>::= [<ModificarAcceso>] identificador “[“ [<ListaParametros>] ”]” <BloqueSentencia>
     * <BloqueSentencia> es equivalente a “¿” [<ListaSentencia>] “?”
     */
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

                        val listaSentencias = esListaSentencia()

                        if (tokenActual?.categoria == Categoria.LLAVE_DERECHA) {
                            siguienteToken()
                            return Metodo(modificadorAcceso, identificador, listaArgumentos, listaSentencias)
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

    /**
     * Metodo Para Determinar si es una Funcion
     * <Funcion> ::= [<ModificarAcceso>] <TipoRetorno> Identificador “[“ [<ListaParametros>] ”]” <BloqueSentencia>
     * <BloqueSentencia> es equivalente a “¿” [<ListaSentencia>] “?”
     *  <Funcion> ::= [<ModificarAcceso>] <TipoRetorno> Identificador “[“ [<ListaParametros>] ”]” “¿” [<ListaSentencia>] “?”
     */
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

                            val listaSentencias = esListaSentencia()

                            val retorno = esRetorno()

                            if (retorno != null) {
                                if (tokenActual?.categoria == Categoria.LLAVE_DERECHA) {
                                    siguienteToken()
                                    return Funcion(modificadorAcceso, tipoDato, identificador, listaArgumentos, listaSentencias, retorno)
                                } else {
                                    reportarError("Se esperaba una llave derecha")
                                }
                            } else {
                                reportarError("Se esperaba una sentencia de retorno")
                            }
                        } else {
                            reportarError("Se esperaba una llave izquierda")
                        }
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

    /**
     * Metodo para Determinar si es una Lista de Argumentos
     */
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

    /**
     * Metodo para Determinar si es un Argumento
     * <Argumento> ::= <TipoDato><Identificador>
     */
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

    /**
     * Metodo para Determinar si es una Lista de Sentencias
     * <ListaSentencia> ::= <Sentencia> [<ListaSentencia>]
     */
    private fun esListaSentencia(): ArrayList<Sentencia> {
        val lista = ArrayList<Sentencia>()

        var sentencia = esSentencia()
        while (sentencia != null) {
            lista.add(sentencia)
            sentencia = esSentencia()
        }

        return lista
    }



    /**
     * Metodo para Determinar si es una Sentencia de Incremento
     * <Incremento> ::= identificador operadorIncremento
     */
    fun esSentenciaIncremento():SentenciaIncremento?{
        if (tokenActual?.categoria == Categoria.IDENTIFICADOR){
            val identificador = tokenActual
            siguienteToken()
            if (tokenActual?.categoria == Categoria.OPERADOR_INCREMENTO){
                val incremento = tokenActual
                siguienteToken()
                return SentenciaIncremento(identificador,incremento)
            }
        }
        return null
    }

    /**
     * Metodo para Determinar si es una Sentencia de Decremento
     * <Decremento> ::= identificador operadorDecremento
     */
    fun esSentenciaDecremento():SentenciaDecremento?{
        if (tokenActual?.categoria == Categoria.IDENTIFICADOR){
            val identificador = tokenActual
            siguienteToken()
            if (tokenActual?.categoria == Categoria.OPERADOR_INCREMENTO){
                val decremento = tokenActual
                siguienteToken()
                return SentenciaDecremento(identificador,decremento)
            }
        }
        return null
    }
    /**
     * Metodo para Determinar si es una Impresion
     * <Imprimir> ::= @Syso ”[“ [<ExpCadena>]  “]” “!”
     */
    fun esImprimir():Imprimir{
        if (tokenActual?.categoria == Categoria.IDENTIFICADOR && tokenActual?.lexema == "@Syso"){
            val syso = tokenActual
            siguienteToken()
            if (tokenActual?.categoria == Categoria.PARENTESIS_IZQUIERDO){
                val expCadena = esExpresionCadena()
                if (tokenActual?.categoria == Categoria.PARENTESIS_DERECHO){
                    siguienteToken()
                    if (tokenActual?.categoria == Categoria.FIN_SENTENCIA){
                        val terminal = tokenActual
                        siguienteToken()
                        return Imprimir(syso,expCadena,terminal)
                    }
                }
            }
        }
        return null
    }

    /**
     * Metodo para Determinar si es un Tipo de Dato
     * <TipoDato> ::= ent | dec | pal | bip | bit
     */
    private fun esTipoDato(): TipoDato? {
        if (tokenActual?.categoria == Categoria.PALABRA_RESERVADA) {
            val esTipo = when (tokenActual!!.lexema) {
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

    /**
     * Metodo Para Determinar si es una SentenciaRetorno
     * <SentenciaRetorno> ::= devolver identificador “!”
     * Como el identificador es una Expresion tambien es equivalente a
     * <SentenciaRetorno> ::= devolver <Expresion> “!”
     */
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

    /**
     * Metodo Para Determinar si es una Variable Global
     * TODO: Inserte BNF asignacion o declaracion de variable Global
     */
    private fun esVariableGlobal(): Bloque? {
        TODO("Falta")
        return null
    }

    /**
     * Metodo para Determinar si es una Expresion
     * <Expresion> ::= <ExpAritmetica> | <ExpRelacional> | <ExpLogica> | <ExpoCadena>
     */
    private fun esExpresion(): Expresion? {
        val init = posicionActual
        val expAritmetica = esExpresionAritmetica()
        if (expAritmetica != null) {
            return expAritmetica
        }
        backtracking(init)
        val expLogica = esExpresionLogica()
        if (expLogica != null) {
            return expLogica
        }
        backtracking(init)
        val expRelacional = esExpresionRelacional()
        if (expRelacional != null) {
            return expRelacional
        }
        backtracking(init)
        val expCadena = esExpresionCadena()
        if (expCadena != null) {
            return expCadena
        }
        backtracking(init)
        return null
    }

    /**
     * Metodo para Determinar si es una Expresion Aritmetica
     */
    private fun esExpresionAritmetica(): ExpresionAritmetica? {
        if (tokenActual?.categoria == Categoria.PARENTESIS_IZQUIERDO) {
            siguienteToken()
            val izq: ExpresionAritmetica? = esExpresionAritmetica()
            if (izq != null) {
                if (tokenActual?.categoria == Categoria.PARENTESIS_DERECHO) {
                    siguienteToken()
                    if (tokenActual?.categoria == Categoria.OPERADOR_ARITMETICO) {
                        val op = tokenActual
                        siguienteToken()
                        val der = esExpresionAritmetica()
                        if (der != null) {
                            return ExpresionAritmetica(izq, op!!, der)
                        }
                    } else {
                        return ExpresionAritmetica(izq)
                    }
                }
            }
        } else {
            val valor = esValorNumerico()
            if (valor != null) {
                siguienteToken()
                if (tokenActual?.categoria == Categoria.OPERADOR_ARITMETICO) {
                    val op = tokenActual
                    siguienteToken()
                    val der = esExpresionAritmetica()
                    if (der != null) {
                        return ExpresionAritmetica(valor, op!!, der)
                    }
                } else {
                    return ExpresionAritmetica(valor)
                }
            }
        }
        return null
    }

    /**
     * Metodo para Determinar si es una Expresion Relacional
     * <ExpRelacional> ::= <ExpAritmetica> OpRelacional <ExpAritmetica>
     */

    private fun esExpresionRelacional(): ExpresionRelacional? {
        val izq = esExpresionAritmetica()
        if (izq != null) {
            siguienteToken()
            if (tokenActual?.categoria == Categoria.OPERADOR_RELACIONAL) {
                siguienteToken()
                val der = esExpresionAritmetica()
                if (der != null) {
                    siguienteToken()
                    return ExpresionRelacional(izq, der)
                } else {
                    reportarError("La operacion relacional no esta Correcta")
                }
            } else {
                reportarError("No tiene asignado Operador Relacional")
            }
        }
        return null
    }

    /**
     * Metodo para Determinar si es una Expresion Logica
     * <ExpLogica> ::= “[“ <ExpLogica> “]” [ OpLogicoBinario <ExpLogica>] | ValorLogico |  Negacion<ExpLogica>
     */
    private fun esExpresionLogica(): ExpresionLogica? {
        if (tokenActual?.categoria == Categoria.OPERADOR_LOGICO && tokenActual?.lexema == "¬") {
            val negacion = tokenActual
            siguienteToken()
            return ExpresionLogica(esExpresionLogica(), null, negacion, null)
        }
        val valor = esValorLogico()
        if (valor != null) {
            siguienteToken()
            if (tokenActual?.categoria == Categoria.OPERADOR_LOGICO) {
                val op = tokenActual
                siguienteToken()
                val der = esExpresionLogica()
                siguienteToken()
                if (der != null) {
                    return ExpresionLogica(der, valor, op, null)
                } else {
                    reportarError("Expresion Logica incorrecta, mal asignacion en el operador logico binario")
                }
            }
            return ExpresionLogica(null, valor, null, null)
        }
        return null
    }

    /**
     * Metodo para Determinar si es una Expresion Cadena
     * <Exp Cadena> ::=  cadenaDeCaracteres [ "+" <Expresion> ]
     */
    private fun esExpresionCadena(): ExpresionCadena? {
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
                }
            }
            else {
                return ExpresionCadena(cadena, null)
            }
        }
        return null
    }

    /**
     *  Metodo para Determinar si es un valor logico
     *  <ValorLogico> ::= .true | .false | <ExpRelacional> | identificador
     */
    private fun esValorLogico(): ValorLogico? {
        if (tokenActual?.categoria == Categoria.BOOLEANO || tokenActual?.categoria == Categoria.IDENTIFICADOR) {
            val valor = tokenActual
            siguienteToken()
            return ValorLogico(valor, null)
        }
        val exp = esExpresionRelacional()
        if (exp != null) {
            siguienteToken()
            return ValorLogico(null, exp)
        }
        return null
    }

    /**
     * Metodo para Determinar si es un Valor Numerico
     * <ValorNumerico> ::= [<Signo>] real | [<Signo>] entero | [<Signo>] identificador
     */
    private fun esValorNumerico(): ValorNumerico? {
        var signo: Token? = null
        if (tokenActual?.categoria == Categoria.OPERADOR_ARITMETICO && (tokenActual?.lexema == "-" || tokenActual?.lexema == "+")) {
            signo = tokenActual
            siguienteToken()
        }
        val esTipo = when (tokenActual?.categoria) {
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
     * Terminar todas las Sentencias
     */

    /**
     * Metodo Para Determinar si es una Sentencia
     * <Sentencia> ::=<SentenciaCondicional> | <SentenciaWhile> | <SentenciaFor> | <SentenciaSwitch> |
     * <SentenciaRetorno> | <Incremento> | <Decremento> | <DeclaracionVariableLocal> | <Asignacion> |
     * <InvocacionMetodo> | <Imprimir> | <Leer>
     */
    private fun esSentencia(): Sentencia? {
        val init = posicionActual
        val sentenciaSi = esSentenciaCondicional()
        if (sentenciaSi!= null){
            return sentenciaSi
        }
        backtracking(init)
        val sentenciaWhile= esSentenciaWhile()
        if (sentenciaWhile != null){
            return sentenciaWhile
        }
        backtracking(init)
        val sentenciaFor= esSentenciaFor()
        if (sentenciaFor != null){
            return sentenciaFor
        }
        backtracking(init)
        val sentenciaRetorno= esRetorno()
        if (sentenciaRetorno != null){
            return sentenciaRetorno
        }
        backtracking(init)
        val incremento = esSentenciaIncremento()
        if (incremento != null){
            return incremento
        }
        backtracking(init)
        val decremento = esSentenciaDecremento()
        if (decremento != null){
            return decremento
        }
        backtracking(init)
        val declaracionVariable = esDeclaracionVariableLocal()
        if (declaracionVariable != null){
            return declaracionVariable
        }
        backtracking(init)
        val asignacion = esAsignacion()
        if (asignacion != null){
            return asignacion
        }
        backtracking(init)
        val invocacionMetodo = esInvocacionMetodo()
        if (invocacionMetodo != null){
            return invocacionMetodo
        }
        backtracking(init)
        val imprimir = esImprimir()
        if (imprimir != null){
            return imprimir
        }
        backtracking(init)
        val leer = esLectura()
        if (leer != null){
            return leer
        }
        backtracking(init)
        return null
    }

    /**
     * Metodo de la sentencia condicional
     */
    private fun esSentenciaCondicional(): SentenciaCondicional?{
        val sentenciasi = esSentenciaSi()
        if (sentenciasi != null){
            val sentenciaSino = esSentenciaSino()
            return SentenciaCondicional(sentenciasi, sentenciaSino)
        }
        return null
    }

    /**
     * Metodo de la Sentencia condicional si
     */
    private fun esSentenciaSi(): SentenciaSi?{
        if (tokenActual?.categoria == Categoria.PALABRA_RESERVADA && tokenActual?.lexema== "wi"){
            siguienteToken()
            if (tokenActual?.categoria== Categoria.PARENTESIS_IZQUIERDO){
                siguienteToken()
                val expLogica = esExpresionLogica()
                if (expLogica != null) {
                    if (tokenActual?.categoria == Categoria.PARENTESIS_DERECHO) {
                        siguienteToken()
                        if (tokenActual?.categoria == Categoria.LLAVE_IZQUIERDO) {
                            siguienteToken()

                            val listaSentencia = esListaSentencia()

                            if (tokenActual?.categoria == Categoria.LLAVE_DERECHA) {
                                return SentenciaSi(expLogica, listaSentencia)
                            } else {
                                reportarError("Se esperaba una llave derecha")
                            }
                        } else {
                            reportarError("Se esperaba una llave izquierdo")
                        }
                    } else {
                        reportarError("Se esperaba un parentesis derecho")
                    }
                } else {
                    reportarError("Se esperaba una expresion logica")
                }
            } else {
                reportarError("Se esperaba un parentesis izquierdo")
            }
        }
        return null
    }

    /**
     * Metodo de la sentencia sino
     */
    private fun esSentenciaSino(): SentenciaSiNo?{
        if (tokenActual?.categoria == Categoria.PALABRA_RESERVADA && tokenActual?.lexema == "wo"){
            siguienteToken()
            val sentenciaSi = esSentenciaSi()
            if (sentenciaSi != null) {
                val sentenciaSino = esSentenciaSino()
                return SentenciaSiNo(ArrayList<Sentencia>(), sentenciaSi,sentenciaSino)
            }else{
                if (tokenActual?.categoria == Categoria.LLAVE_IZQUIERDO){
                    siguienteToken()
                    val listaSentencia = esListaSentencia()
                        if (tokenActual?.categoria == Categoria.LLAVE_DERECHA) {
                            return SentenciaSiNo(listaSentencia, null, null)
                        }else{
                            reportarError("Se esperaba una llave derecha")
                        }
                    }else{
                    reportarError("Se esperaba una llave iquierdo")
                    }
                }
            }
        return null
    }

    /**
     * Metodo de la sentencia while
     */
    private fun esSentenciaWhile(): SentenciaWhile? {
        if (tokenActual?.categoria == Categoria.PALABRA_RESERVADA && tokenActual?.lexema == "durante") {
            siguienteToken()
            if (tokenActual?.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                siguienteToken()
                val expLogico = esExpresionLogica()
                if (expLogico != null) {
                    if (tokenActual?.categoria == Categoria.PARENTESIS_DERECHO) {
                        siguienteToken()
                        if (tokenActual?.categoria == Categoria.LLAVE_IZQUIERDO) {
                            siguienteToken()
                            val listaSentencia = esListaSentencia()
                            if (tokenActual?.categoria == Categoria.LLAVE_DERECHA) {
                                return SentenciaWhile(expLogico, listaSentencia)
                            }else{
                                reportarError("Se esperaba una llave derecha")
                            }
                        }else{
                            reportarError("Se esperaba una llave izquierda")
                        }
                    }else{
                        reportarError("Se esperaba un parentesis derecho")
                    }
                }else{
                    reportarError("Se esperaba una expresión lógica")
                }
            }else{
                reportarError("Se esperaba un parentesis izquierdo")
            }
        }
        return null
    }


    /**
     * Metodo para Determinar si es una Lista de Parametros
     */

    /**
     * Metodo para Determinar si es un Parametro
     */

    /**
     * Metodo para Determinar si es una Lista De Variables
     */

    /**
     * Metodo para Determinar si es una Variable
     */

    /**
     * Metodo para Determinar si es una Asignacion
     */

    /**
     * Metodo para Determinar si es un Arreglo
     */

    /**
     * Metodo para Determinar si es una Impresion
     * Impresion(Expresion)
     */

    /**
     * Metodo para Determinar si es una Lectura
     * lectura(nombreVariable:Token)
     */

}