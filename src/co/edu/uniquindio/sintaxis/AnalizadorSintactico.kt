package co.edu.uniquindio.sintaxis

import co.edu.uniquindio.lexico.Categoria.*
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.bnf.unidad.Clase
import co.edu.uniquindio.sintaxis.bnf.unidad.Importacion
import co.edu.uniquindio.sintaxis.bnf.unidad.Paquete
import co.edu.uniquindio.sintaxis.bnf.bloque.Bloque
import co.edu.uniquindio.sintaxis.bnf.bloque.Funcion
import co.edu.uniquindio.sintaxis.bnf.bloque.VariableGlobal
import co.edu.uniquindio.sintaxis.bnf.control.*
import co.edu.uniquindio.sintaxis.bnf.expresion.*
import co.edu.uniquindio.sintaxis.bnf.otro.*
import co.edu.uniquindio.sintaxis.bnf.sentencia.*
import co.edu.uniquindio.sintaxis.bnf.unidad.UnidadCompilacion

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
    init {
        val copia: ArrayList<*> = tokens.clone() as ArrayList<*>
        for (token in copia!!) {
            token as Token
            if (token.categoria == COMENTARIO_LINEA || token.categoria == COMENTARIO_BLOQUE) {
                tokens.remove(token)
            }
        }
    }

    /**
     * Arbol sintactico y lista de errores por el analizador sintactico
     */
    var listaErrores: ArrayList<ErrorSintactico> = ArrayList()

    /**
     * Elementos necesarios del analizador lexico
     */
    private var posicionActual: Int = 0
    private var tokenActual: Token? = tokens[0]

    private var fila: Int? = 0
    private var columna: Int? = 0

    /**
     * Metodo que avanza al siguiente token
     *
     * Configura la posicion actual
     * Verifica que no se desborde
     */
    private fun siguienteToken() {
        fila = tokenActual?.fila
        columna = tokenActual?.columna!! + tokenActual?.lexema!!.length

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
     * @param posicion Posicion a la que se va a devolver el token actual
     * @param cantidadErrores La cantidad de errores a la que se va a devolver la lista de errores
     */
    private fun backtracking(posicion: Int, cantidadErrores: Int) {
        posicionActual = posicion - 2
        siguienteToken()
        siguienteToken()

        if (cantidadErrores < listaErrores.size) {
            val cant = cantidadErrores - listaErrores.size
            for (i in 0..cant) {
                listaErrores.removeAt(listaErrores.lastIndex)
            }
        }
    }

    /**
     * Metodo que reporta un error y lo agrega
     * a la lista de errores
     *
     * @param mensaje El mensaje del error que se va agregar a la lista de errores
     */
    private fun reportarError(token: String) {
        val fil = fila ?: -1
        val col = columna ?: -1

        var centinela = true

        for (error in listaErrores) {
            if (error.fila == fil && error.columna == col) {
                centinela = false
                break
            }
        }

        val error = ErrorSintactico(token, fil, col)
        if (centinela) listaErrores.add(error)
    }

    /**
     *  Metodo para avanzar hasta el proximo token seguro
     *
     *  @param seleccion El token seguro al que desea avanzar (Indice de la lista)
     */
    private fun buscarTokenSeguro(seleccion: Int) {
        val categorias = arrayOf(CORCHETE_DERECHO, PARENTESIS_DERECHO, FIN_SENTENCIA, LLAVE_DERECHA)

        var centinela = true

        while (centinela) {
            siguienteToken()
            for (i in seleccion until categorias.size) if (tokenActual?.categoria == categorias[i]) {
                centinela = false
                break
            }
        }
        siguienteToken()
    }

    /**
     * Metodo que avanza al siguiente token
     *
     * Configura la posicion actual
     * Verifica que no se desborde
     *
     * @return UnidadCompilacion La unidad minima y raiz del arbol sintactico
     */
    fun esUnidadDeCompilacion(): UnidadCompilacion {
        val paquete: Paquete? = esPaquete()
        val listaImportaciones: ArrayList<Importacion> = esListaImportaciones()
        val clase: Clase? = esClase()

        if (clase == null) {
            reportarError("una clase")
        }

        return UnidadCompilacion(paquete, listaImportaciones, clase)
    }

    /**
     * Metodo para Determinar si es una parquete
     * <DeclaracionPaquete> ::= caja identificador “!”
     *
     * @return Paquete si existe, null sino existe
     */
    private fun esPaquete(): Paquete? {
        if (tokenActual?.categoria == PALABRA_RESERVADA && tokenActual?.lexema == "caja") {
            siguienteToken()
            var paquete: Token? = null
            if (tokenActual?.categoria == IDENTIFICADOR) {
                paquete = tokenActual!!
                siguienteToken()
            } else {
                reportarError("un identificador")
            }
            if (tokenActual?.categoria == FIN_SENTENCIA) {
                siguienteToken()
            } else {
                reportarError("un terminal")
            }
            return Paquete(paquete)
        }
        return null
    }

    /**
     * Metodo para Determinar si es una Lista de Importaciones
     * <ListaImportaciones> ::= <Importacion> [<ListaImportaciones>]
     *
     * @return ArrayList<Importacion> La lista de importaciones
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
     *
     * @return Importacion si existe, null sino existe
     */
    private fun esImportacion(): Importacion? {
        if (tokenActual?.categoria == PALABRA_RESERVADA && tokenActual?.lexema == "meter") {
            siguienteToken()
            var importacion: Token? = null
            if (tokenActual?.categoria == IDENTIFICADOR) {
                importacion = tokenActual!!
                siguienteToken()
            } else {
                reportarError("un identificador")
            }
            if (tokenActual?.categoria == FIN_SENTENCIA) {
                siguienteToken()
            } else {
                reportarError("un terminal")
            }
            return Importacion(importacion)
        }
        return null
    }

    /**
     * Metodo para Determinar si es una clase
     * <DeclaracionClase> ::=  [ <ModificadorAcceso> ] cosa identificador “¿” [<ListaBloque>] “?”
     *
     * @return Clase si existe, null sino existe
     */
    private fun esClase(): Clase? {
        var modificadorAcceso: Token? = null

        if (tokenActual?.categoria == PALABRA_RESERVADA) {
            if (tokenActual?.lexema == "estrato1" || tokenActual?.lexema == "estrato6") {
                modificadorAcceso = tokenActual
                siguienteToken()
            }
        }

        if (tokenActual?.categoria == PALABRA_RESERVADA && tokenActual?.lexema == "cosa") {
            siguienteToken()
            val identificador = tokenActual!!
            if (tokenActual?.categoria == IDENTIFICADOR) {
                siguienteToken()
            } else {
                reportarError("un identificador")
            }

            if (tokenActual?.categoria == LLAVE_IZQUIERDO) {
                siguienteToken()
            } else {
                reportarError("una llave izquierda")
            }
            val listaBloque = esListaBloque()

            if (tokenActual?.categoria == LLAVE_DERECHA) {
                siguienteToken()
            } else {
                reportarError("una llave derecha")
            }
            return Clase(modificadorAcceso, identificador, listaBloque)
        }
        return null
    }

    /**
     * Metodo para Determinar si es una Lista de Bloques
     * <ListaBloque> ::= <Bloque> [<ListaBloque>]
     *
     * @return ArrayList<Bloque> La lista de bloques de codigo
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
     *
     * @return Bloque si existe, null sino existe
     */
    private fun esBloque(): Bloque? {
        val posicionInicial = posicionActual
        val cantidadErrores = listaErrores.size

        val variableGlobal = esVariableGlobal()
        if (variableGlobal != null) {
            return variableGlobal
        } else {
            backtracking(posicionInicial, cantidadErrores)
        }

        val funcion = esFuncion()
        if (funcion != null) {
            return funcion
        } else {
            backtracking(posicionInicial, cantidadErrores)
        }

        return null
    }

    /**
     * Metodo Para Determinar si es una Funcion
     * <Funcion> ::= [<ModificarAcceso>] <TipoRetorno> Identificador “[“ [<ListaParametros>] ”]” “¿” [<ListaSentencia>] “?”
     *
     * @return Funcion si existe, null sino existe
     */
    private fun esFuncion(): Funcion? {
        var modificadorAcceso: Token? = null

        if (tokenActual?.categoria == PALABRA_RESERVADA) {
            if (tokenActual?.lexema == "estrato1" || tokenActual?.lexema == "estrato6") {
                modificadorAcceso = tokenActual
                siguienteToken()
            }
        }
        val tipo = tokenActual
        if (tokenActual?.categoria == TIPO_DATO) {
            siguienteToken()
        }
        if (tokenActual?.categoria == IDENTIFICADOR_METODO) {
            val identificador = tokenActual!!
            siguienteToken()
            if (tokenActual?.categoria == PARENTESIS_IZQUIERDO) {
                siguienteToken()
            } else {
                reportarError("una llave izquierda")
            }
            val listaArgumentos = esListaParametros()

            if (tokenActual?.categoria == PARENTESIS_DERECHO) {
                siguienteToken()
            } else {
                reportarError("una llave derecho")
            }
            if (tokenActual?.categoria == LLAVE_IZQUIERDO) {
                siguienteToken()
            } else {
                reportarError("un parentesis izquierdo")
            }
            val listaSentencias = esListaSentencia()

            if (tokenActual?.categoria == LLAVE_DERECHA) {
                siguienteToken()
            } else {
                reportarError("una llave derecha")
            }
            return Funcion(modificadorAcceso, tipo, identificador, listaArgumentos, listaSentencias)
        }
        return null
    }

    /**
     * Metodo para Determinar si es una Lista de Parametros
     * <ListaParametros> ::= <Parametro> [ "," <ListaParametros>]
     *
     * @return ArrayList<Parametro> La lista de parametros
     */
    private fun esListaParametros(): ArrayList<Parametro> {
        val lista = ArrayList<Parametro>()

        var parametro: Parametro? = esParametro()
        while (parametro != null) {
            lista.add(parametro)
            parametro = if (tokenActual?.categoria == SEPARADOR) {
                siguienteToken()
                esParametro()
            } else {
                null
            }
        }
        return lista
    }

    /**
     * Metodo para Determinar si es un Parametro
     * <Parametro> ::= <TipoDato> identificador
     *
     * @return Parametro si existe, null sino existe
     */
    private fun esParametro(): Parametro? {
        if (tokenActual?.categoria == TIPO_DATO) {
            val tipo = tokenActual!!
            siguienteToken()

            if (tokenActual?.categoria == IDENTIFICADOR) {
                val identificador = tokenActual!!
                siguienteToken()
                return Parametro(tipo, identificador)
            }
        }
        return null
    }

    /**
     * Metodo para Determinar si es una Lista de Argumentos
     * <ListaArgumentos> ::= <Argumento> [ "," <ListaArgumentos> ]
     *
     * @return ArrayList<Argumento> La lista de argumentos
     */
    private fun esListaArgumentos(): ArrayList<Argumento> {
        val lista = ArrayList<Argumento>()
        var parametro = esArgumento()
        while (parametro != null) {
            lista.add(parametro)
            parametro = if (tokenActual?.categoria == SEPARADOR) {
                siguienteToken()
                esArgumento()
            } else {
                null
            }
        }

        return lista
    }

    /**
     * Metodo para determinar si es un Argumento
     * <Argumento> ::= <Expresion>
     *
     * @return Argumento si existe, null sino existe
     */
    private fun esArgumento(): Argumento? {
        val expresion = esExpresion()
        if (expresion != null) {
            return Argumento(expresion)
        }
        return null
    }

    /**
     * Metodo para Determinar si es una Lista de Sentencias
     * <ListaSentencia> ::= <Sentencia> [<ListaSentencia>]
     *
     * @return ArrayList<Sentencia> La lista de sentencias
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
     * Metodo Para Determinar si es una Sentencia
     * <Sentencia> ::= <SentenciaRetorno> | <Incremento> | <Decremento> | <DeclaracionVariableLocal> |
     * <Asignacion> | <InvocacionMetodo> | <EstructuraControl>
     *
     * @return Sentencia si existe, null sino existe
     */
    private fun esSentencia(): Sentencia? {
        val posicionInicial = posicionActual
        val cantidadErrores = listaErrores.size

        val declaracionVariable = esVariableLocal()
        if (declaracionVariable != null) {
            return declaracionVariable
        } else {
            backtracking(posicionInicial, cantidadErrores)
        }

        val incremento = esIncrementoDecremento()
        if (incremento != null) {
            return incremento
        } else {
            backtracking(posicionInicial, cantidadErrores)
        }

        val invocacionMetodo = esInvocacionMetodo()
        if (invocacionMetodo != null) {
            return invocacionMetodo
        } else {
            backtracking(posicionInicial, cantidadErrores)
        }

        val asignacion = esAsignacion()
        if (asignacion != null) {
            return asignacion
        } else {
            backtracking(posicionInicial, cantidadErrores)
        }

        val arreglo = esArreglo()
        if (arreglo != null) {
            return arreglo
        } else {
            backtracking(posicionInicial, cantidadErrores)
        }

        val retorno = esRetorno()
        if (retorno != null) {
            return retorno
        } else {
            backtracking(posicionInicial, cantidadErrores)
        }

        val estructuraControl = esEstructuraControl()
        if (estructuraControl != null) {
            return estructuraControl
        } else {
            backtracking(posicionInicial, cantidadErrores)
        }

        return null
    }

    /**
     * Metodo para determinar si es una estructura de control
     * <EstructuraControl> ::= <SentenciaCondicional> | <SentenciaWhile> | <SentenciaFor>
     *
     * @return EstructuraControl si existe, null sino existe
     */
    private fun esEstructuraControl(): EstructuraControl? {
        val posicionInicial = posicionActual
        val cantidadErrores = listaErrores.size

        val sentenciaSi = esCondicional()
        if (sentenciaSi != null) {
            return sentenciaSi
        } else {
            backtracking(posicionInicial, cantidadErrores)
        }

        val sentenciaWhile = esCicloWhile()
        if (sentenciaWhile != null) {
            return sentenciaWhile
        } else {
            backtracking(posicionInicial, cantidadErrores)
        }

        val sentenciaFor = esCicloFor()
        if (sentenciaFor != null) {
            return sentenciaFor
        } else {
            backtracking(posicionInicial, cantidadErrores)
        }

        return null
    }

    /**
     * Metodo para Determinar si es una Asignacion
     * <Asignacion> ::= identificador operadorAsignación <Expresión> ["!"]
     *
     * @return Asignacion si existe, null sino existe
     */
    private fun esAsignacion(): Asignacion? {
        if (tokenActual?.categoria == IDENTIFICADOR) {
            val identificador = tokenActual
            siguienteToken()
            if (tokenActual?.categoria == OPERADOR_ASIGNACION) {
                siguienteToken()
                val posicionInicial = posicionActual
                val cantidadErrores = listaErrores.size

                val metodo = esInvocacionMetodo()
                if (metodo != null) {
                    return Asignacion(identificador!!, null, metodo)
                } else {
                    backtracking(posicionInicial, cantidadErrores)
                }

                val expresion = esExpresion()
                if (expresion != null) {
                    if (tokenActual?.categoria == FIN_SENTENCIA) {
                        siguienteToken()
                    } else {
                        reportarError("un fin de sentencia")
                    }
                    return Asignacion(identificador!!, expresion, metodo)

                } else {
                    reportarError("una expresion")
                }
            } else {
                reportarError("un operador de asignacion")
            }
        }

        return null
    }

    /**
     * Metodo para Determinar si es una Sentencia de Incremento o Decremento
     * <Incremento> ::= identificador operadorIncremento
     * <Decremento> ::= identificador operadorDecremento
     *
     * @return IncrementoDecremento si existe, null sino existe
     */
    private fun esIncrementoDecremento(): IncrementoDecremento? {
        if (tokenActual?.categoria == IDENTIFICADOR) {
            val identificador = tokenActual
            siguienteToken()
            if (tokenActual?.categoria == OPERADOR_INCREMENTO) {
                val incremento = tokenActual
                siguienteToken()
                return IncrementoDecremento(identificador!!, incremento!!)
            } else if (tokenActual?.categoria == OPERADOR_INCREMENTO) {
                val decremento = tokenActual
                siguienteToken()
                return IncrementoDecremento(identificador!!, decremento!!)
            }
        }
        return null
    }

    /**
     * Metodo para Determinar si es una Impresion
     * <InvocacionMetodo> ::=  identificador "[" [<ListaArgumentos>] "]" "!”
     *
     * @return InvocacionMetodo si existe, null sino existe
     */
    private fun esInvocacionMetodo(): InvocacionMetodo? {
        if (tokenActual?.categoria == IDENTIFICADOR_METODO) {
            val identificador = tokenActual
            siguienteToken()
            if (tokenActual?.categoria == PARENTESIS_IZQUIERDO) {
                siguienteToken()
            } else {
                reportarError("un parentesis Izquierdo")
            }
            val listaParametros = esListaArgumentos()
            if (tokenActual?.categoria == PARENTESIS_DERECHO) {
                siguienteToken()
            } else {
                reportarError("un parentesis Derecho")
            }
            if (tokenActual?.categoria == FIN_SENTENCIA) {
                siguienteToken()
            } else {
                reportarError("un fin de sentencia")
            }
            return InvocacionMetodo(identificador!!, listaParametros)
        }
        return null
    }

    /**
     * Metodo Para Determinar si es una SentenciaRetorno
     * <SentenciaRetorno> ::= devolver <Expresion>“!”
     *
     * @return Retorno si existe, null sino existe
     */
    private fun esRetorno(): Retorno? {
        if (tokenActual?.categoria == PALABRA_RESERVADA && tokenActual?.lexema == "devolver") {
            siguienteToken()

            val expresion = esExpresion()
            if (expresion != null) {
                if (tokenActual?.categoria == FIN_SENTENCIA) {
                    siguienteToken()
                } else {
                    reportarError("un final de sentencia")
                }
                return Retorno(expresion)
            } else {
                reportarError("una expresion, valor o indentificador")
            }
        }
        return null
    }

    /**
     * Metodo Para Determinar si es una Variable Lobal
     * <VariableLobal> ::= <TipoDato> identificador “=” <Expresion> “!” |
     *  <TipoDato> identificador “=” <InvocacionMetodo>
     *
     * @return VariableLocal si existe, null sino existe
     */
    private fun esVariableLocal(): VariableLocal? {
        if (tokenActual?.categoria == TIPO_DATO) {
            val tipo = tokenActual!!
            siguienteToken()

            if (tokenActual?.categoria == IDENTIFICADOR) {
                val identificador = tokenActual

                siguienteToken()
                if (tokenActual?.categoria == OPERADOR_ASIGNACION && tokenActual?.lexema == "=") {
                    siguienteToken()
                    val posicionInicial = posicionActual
                    val cantidadErrores = listaErrores.size

                    val metodo = esInvocacionMetodo()
                    if (metodo != null) {
                        return VariableLocal(tipo, identificador!!, null, metodo)
                    } else {
                        backtracking(posicionInicial, cantidadErrores)
                    }

                    val exp = esExpresion()
                    if (exp != null) {
                        if (tokenActual?.categoria == FIN_SENTENCIA) {
                            siguienteToken()
                        } else {
                            reportarError("un fin de sentencia")
                        }
                        return VariableLocal(tipo, identificador!!, exp, null)
                    } else {
                        reportarError("una asignacion de valor")
                    }
                } else if (tokenActual?.categoria == FIN_SENTENCIA) {
                    siguienteToken()
                } else {
                    reportarError("un fin de sentencia")
                }
                return VariableLocal(tipo, identificador!!, null, null)
            }
        }
        return null
    }

    /**
     * Metodo Para Determinar si es una Variable Global
     * <VariableGlobal> ::= [<ModificadorAcceso>] <TipoDato> identificador “=” <Expresion> “!” |
     * [<ModificadorAcceso>] <TipoDato> identificador “=” <InvocacionMetodo>
     *
     * @return VariableGlobal si existe, null sino existe
     */
    private fun esVariableGlobal(): VariableGlobal? {
        var modificadorAcceso: Token? = null
        if (tokenActual?.categoria == PALABRA_RESERVADA) {
            if (tokenActual?.lexema == "estrato1" || tokenActual?.lexema == "estrato6") {
                modificadorAcceso = tokenActual
                siguienteToken()
            }
        }
        if (tokenActual?.categoria == TIPO_DATO) {
            val tipo = tokenActual!!
            siguienteToken()

            if (tokenActual?.categoria == IDENTIFICADOR) {
                val identificador = tokenActual
                siguienteToken()
                if (tokenActual?.categoria == OPERADOR_ASIGNACION && tokenActual?.lexema == "=") {
                    siguienteToken()
                    val posicionInicial = posicionActual
                    val cantidadErrores = listaErrores.size

                    val metodo = esInvocacionMetodo()
                    if (metodo != null) {
                        return VariableGlobal(modificadorAcceso, tipo, identificador!!, null, metodo)
                    } else {
                        backtracking(posicionInicial, cantidadErrores)
                    }
                    val exp = esExpresion()
                    if (exp != null) {
                        if (tokenActual?.categoria == FIN_SENTENCIA) {
                            siguienteToken()
                        } else {
                            reportarError("un fin de sentencia")
                        }
                        return VariableGlobal(modificadorAcceso, tipo, identificador!!, exp, metodo)
                    }
                } else {
                    if (tokenActual?.categoria == FIN_SENTENCIA) {
                        siguienteToken()
                    } else {
                        reportarError("un fin de sentencia")
                    }
                    return VariableGlobal(modificadorAcceso, tipo, identificador!!, null, null)
                }
            } else {
                reportarError("un identificador")
            }
        }
        return null
    }

    /**
     * Metodo para Determinar si es una Expresion
     * <Expresion> ::= <ExpAritmetica> | <ExpRelacional> | <ExpLogica> | <ExpoCadena>
     *
     * @return Expresion si existe, null sino existe
     */
    private fun esExpresion(): Expresion? {
        val posicionInicial = posicionActual
        val cantidadErrores = listaErrores.size

        val expLogica = esExpresionLogica()
        if (expLogica != null) {
            return expLogica
        } else {
            backtracking(posicionInicial, cantidadErrores)
        }

        val expRelacional = esExpresionRelacional()
        if (expRelacional != null) {
            return expRelacional
        } else {
            backtracking(posicionInicial, cantidadErrores)
        }

        val expAritmetica = esExpresionAritmetica()
        if (expAritmetica != null) {
            return expAritmetica
        } else {
            backtracking(posicionInicial, cantidadErrores)
        }

        val expCadena = esExpresionCadena()
        if (expCadena != null) {
            return expCadena
        } else {
            backtracking(posicionInicial, cantidadErrores)
        }

        return null
    }

    /**
     * Metodo para Determinar si es una Expresion Aritmetica
     * <ExpAritmetica> ::= “[“ <ExpAritmetica> “]” [ operadorAritmetico <ExpAritmetica>] |
     * <ValorNumerico> [operadorAritmetico <ExpAritmetica>]
     *
     * @return ExpresionAritmetica si existe, null sino existe
     */
    private fun esExpresionAritmetica(): ExpresionAritmetica? {
        if (tokenActual?.categoria == PARENTESIS_IZQUIERDO) {
            siguienteToken()
            val izq: ExpresionAritmetica? = esExpresionAritmetica()
            if (izq != null) {
                if (tokenActual?.categoria == PARENTESIS_DERECHO) {
                    siguienteToken()
                    if (tokenActual?.categoria == OPERADOR_ARITMETICO) {
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
                if (tokenActual?.categoria == OPERADOR_ARITMETICO) {
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
     *
     * @return ExpresionRelacional si existe, null sino existe
     */
    private fun esExpresionRelacional(): ExpresionRelacional? {
        val izq = esExpresionAritmetica()
        if (izq != null) {
            if (tokenActual?.categoria == OPERADOR_RELACIONAL) {
                val operador = tokenActual
                siguienteToken()
                val der = esExpresionAritmetica()
                if (der != null) {
                    return ExpresionRelacional(izq, operador!!, der)
                } else {
                    reportarError("una expresion relacional correcta")
                }
            }
        }
        return null
    }

    /**
     * Metodo para Determinar si es una Expresion Logica
     * <ExpLogica> ::= <ExpLogica> [ OpLogicoBinario <ExpLogica>] | ValorLogico |  Negacion<ExpLogica>
     *
     * @return ExpresionLogica si existe, null sino existe
     */
    private fun esExpresionLogica(): ExpresionLogica? {
        if (tokenActual?.categoria == OPERADOR_LOGICO && tokenActual?.lexema == "¬") {
            val negacion = tokenActual
            siguienteToken()
            return ExpresionLogica(esExpresionLogica(), null, negacion)
        }
        //aqui
        val valor = esValorLogico()
        if (valor != null) {
            if (tokenActual?.categoria == OPERADOR_LOGICO) {
                val op = tokenActual
                siguienteToken()
                val der = esExpresionLogica()
                if (der != null) {
                    return ExpresionLogica(der, valor, op)
                } else {
                    reportarError("una expresion logica correcta (operador logico binario)")
                }
            }
            return ExpresionLogica(null, valor, null)
        }
        return null
    }

    /**
     * Metodo para Determinar si es una Expresion Cadena
     * <Exp Cadena> ::=  cadenaDeCaracteres [ "+" <Expresion> ]
     *
     * @return ExpresionCadena si existe, null sino existe
     */
    private fun esExpresionCadena(): ExpresionCadena? {
        if (tokenActual?.categoria == CADENA_CARACTERES) {
            val cadena = tokenActual
            siguienteToken()
            if (tokenActual?.categoria == OPERADOR_ARITMETICO && tokenActual?.lexema == "+") {
                siguienteToken()
                val expresion = esExpresion()
                if (expresion != null) {
                    return ExpresionCadena(cadena!!, expresion)
                } else {
                    reportarError("otra cadena")
                }
            } else {
                return ExpresionCadena(cadena!!, null)
            }
        }
        return null
    }

    /**
     *  Metodo para Determinar si es un valor logico
     *  <ValorLogico> ::= .true | .false | <ExpRelacional> | identificador
     *
     * @return ValorLogica si existe, null sino existe
     */
    private fun esValorLogico(): ValorLogico? {
        val posicionInicial = posicionActual
        val cantidadErrores = listaErrores.size

        val exp = esExpresionRelacional()
        if (exp != null) {
            return ValorLogico(null, exp)
        } else {
            backtracking(posicionInicial, cantidadErrores)
        }

        if (tokenActual?.categoria == BOOLEANO || tokenActual?.categoria == IDENTIFICADOR) {
            val valor = tokenActual
            siguienteToken()
            return ValorLogico(valor, null)
        }
        return null
    }

    /**
     * Metodo para Determinar si es un Valor Numerico
     * <ValorNumerico> ::= [<Signo>] real | [<Signo>] entero | [<Signo>] identificador
     *
     * @return ValorNumerico si existe, null sino existe
     */
    private fun esValorNumerico(): ValorNumerico? {
        var signo: Token? = null
        if (tokenActual?.categoria == OPERADOR_ARITMETICO && (tokenActual?.lexema == "-" || tokenActual?.lexema == "+")) {
            signo = tokenActual
            siguienteToken()
        }
        val esTipo = when (tokenActual?.categoria) {
            ENTERO, REAL, IDENTIFICADOR -> true
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
     * Metodo de la sentencia condicional
     * <SentenciaCondicional> ::= <SentenciaSi> [<SentenciaSino>]
     *
     * @return Condicional si existe, null sino existe
     */
    private fun esCondicional(): Condicional? {
        val sentenciasi = esEstructuraSi()
        if (sentenciasi != null) {
            val sentenciaSino = esEstructuraSino()
            return Condicional(sentenciasi, sentenciaSino)
        }
        return null
    }

    /**
     * Metodo de la Sentencia condicional si
     * <SentenciaSi> ::= wi “[“ <ExpLogica> ”]” “¿” [<ListaSentencia>] “?”
     *
     * @return EstructuraSi si existe, null sino existe
     */
    private fun esEstructuraSi(): EstructuraSi? {
        if (tokenActual?.categoria == PALABRA_RESERVADA && tokenActual?.lexema == "wi") {
            siguienteToken()
            if (tokenActual?.categoria == PARENTESIS_IZQUIERDO) {
                siguienteToken()
            } else {
                reportarError("un parentesis izquierdo")
            }
            val expLogica = esExpresionLogica()
            if (expLogica != null) {
                if (tokenActual?.categoria == PARENTESIS_DERECHO) {
                    siguienteToken()
                } else {
                    reportarError("un parentesis derecho")
                }
                if (tokenActual?.categoria == LLAVE_IZQUIERDO) {
                    siguienteToken()
                } else {
                    reportarError("una llave izquierdo")
                }
                val listaSentencia = esListaSentencia()
                if (tokenActual?.categoria == LLAVE_DERECHA) {
                    siguienteToken()
                } else {
                    reportarError("una llave derecha")
                }
                return EstructuraSi(expLogica, listaSentencia)
            } else {
                reportarError("una expresion logica")
            }
        }
        return null
    }

    /**
     * Metodo para Determinar si es una SentenciaSINO
     * <SentenciaSino> ::= wo “¿” [<ListaSentencia>] “?” | wo <SentenciaSi> [<SentenciaSino>]
     *
     * @return EstructuraSino si existe, null sino existe
     */
    private fun esEstructuraSino(): EstructuraSiNo? {
        if (tokenActual?.categoria == PALABRA_RESERVADA && tokenActual?.lexema == "wo") {
            siguienteToken()
            val sentenciaSi = esEstructuraSi()
            if (sentenciaSi != null) {
                val sentenciaSino = esEstructuraSino()
                return EstructuraSiNo(ArrayList(), sentenciaSi, sentenciaSino)
            } else {
                if (tokenActual?.categoria == LLAVE_IZQUIERDO) {
                    siguienteToken()
                } else {
                    reportarError("una llave izquierda")
                }
                val listaSentencia = esListaSentencia()
                if (tokenActual?.categoria == LLAVE_DERECHA) {
                    siguienteToken()
                } else {
                    reportarError("una llave derecha")
                }
                return EstructuraSiNo(listaSentencia, null, null)
            }
        }
        return null
    }

    /**
     * Metodo para Determinar si es una sentencia while
     * <SentenciaWhile> ::= durante “[“ <ExpLogica> ”]” “¿” [<ListaSentencia>] “?”
     *
     * @return CicloWhile si existe, null sino existe
     */
    private fun esCicloWhile(): CicloWhile? {
        if (tokenActual?.categoria == PALABRA_RESERVADA && tokenActual?.lexema == "durante") {
            siguienteToken()
            if (tokenActual?.categoria == PARENTESIS_IZQUIERDO) {
                siguienteToken()
            } else {
                reportarError("un parentesis izquierdo")
            }
            val expLogico = esExpresionLogica()
            if (expLogico != null) {
                if (tokenActual?.categoria == PARENTESIS_DERECHO) {
                    siguienteToken()
                } else {
                    reportarError("un parentesis derecho")
                }
                if (tokenActual?.categoria == LLAVE_IZQUIERDO) {
                    siguienteToken()
                } else {
                    reportarError("una llave izquierda")
                }
                val listaSentencia = esListaSentencia()
                if (tokenActual?.categoria == LLAVE_DERECHA) {
                    siguienteToken()
                    return CicloWhile(expLogico, listaSentencia)
                } else {
                    reportarError("una llave derecha")
                }
            } else {
                reportarError("una expresión lógica")
            }
        }
        return null
    }

    /**
     * Metodo para Determinar si es una sentencia FOR
     * <SentenciaFor> ::=  ciclo “[” <DeclaracionVariableLocal> “|” <ExpLogica> “|” <AsignacionCiclo> “]”
     *  “¿” [<ListaSentencia>] “?”
     *
     * @return CicloFor si existe, null sino existe
     */
    private fun esCicloFor(): CicloFor? {
        if (tokenActual?.categoria == PALABRA_RESERVADA && tokenActual?.lexema == "ciclo") {
            siguienteToken()
            if (tokenActual?.categoria == PARENTESIS_IZQUIERDO) {
                siguienteToken()
            } else {
                reportarError("un parentesis izquierdo")
            }
            val decVariableLocal = esVariableLocal()
            if (decVariableLocal != null) {
                if (tokenActual?.categoria == DOS_PUNTOS) {
                    siguienteToken()
                } else {
                    reportarError("un separador (|)")
                }
                val expLogica = esExpresionLogica()
                if (expLogica != null) {
                    if (tokenActual?.categoria == DOS_PUNTOS) {
                        siguienteToken()
                    } else {
                        reportarError("un separador (|)")
                    }
                    val asigCiclo = esIncrementoDecremento()
                    if (asigCiclo != null) {
                        if (tokenActual?.categoria == PARENTESIS_DERECHO) {
                            siguienteToken()
                        } else {
                            reportarError("un parentesis derecho")
                        }
                        if (tokenActual?.categoria == LLAVE_IZQUIERDO) {
                            siguienteToken()
                        } else {
                            reportarError("una llave izquierda")
                        }
                        val listaSentencia = esListaSentencia()
                        if (tokenActual?.categoria == LLAVE_DERECHA) {
                            siguienteToken()
                        } else {
                            reportarError("una llave derecha")
                        }
                        return CicloFor(decVariableLocal, expLogica, asigCiclo, listaSentencia)

                    } else {
                        reportarError("una asignacion")
                    }

                } else {
                    reportarError("una expresion logica")
                }

            }

        }
        return null
    }

    /**
     * Metodo Determinar si es un Arreglo
     * <Arreglo> ::= <tipoDato> “{“ “}” identificador “=”  {“ <listaArgumentos> “}” “!”
     *
     * @return Arreglo si existe, null sino existe
     */
    private fun esArreglo(): Arreglo? {
        if (tokenActual?.categoria == TIPO_DATO) {
            val tipo = tokenActual!!
            siguienteToken()

            if (tokenActual?.categoria == CORCHETE_IZQUIERDO) {
                siguienteToken()
                if (tokenActual?.categoria == CORCHETE_DERECHO) {
                    siguienteToken()
                } else {
                    reportarError("un corchete derecho")
                }
                if (tokenActual?.categoria == IDENTIFICADOR) {
                    val identificador = tokenActual!!
                    siguienteToken()
                    if (tokenActual?.categoria == OPERADOR_ASIGNACION && tokenActual?.lexema == "=") {
                        siguienteToken()
                        if (tokenActual?.categoria == CORCHETE_IZQUIERDO) {
                            siguienteToken()
                        } else {
                            reportarError("un corchete izquierdo")
                        }
                        val listaParametros = esListaArgumentos()
                        if (tokenActual?.categoria == CORCHETE_DERECHO) {
                            siguienteToken()
                        } else {
                            reportarError("un corchete derecho")
                        }
                        if (tokenActual?.categoria == FIN_SENTENCIA) {
                            siguienteToken()
                        } else {
                            reportarError("un fin de sentencia")
                        }
                        return Arreglo(tipo, identificador, listaParametros)

                    } else {
                        reportarError("un operador de asignación")
                    }
                } else {
                    reportarError("un identificador")
                }

            } else {
                reportarError("un corchete izquierdo")
            }
        }
        return null
    }
}