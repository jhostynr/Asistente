package com.wonder.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.KeyEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_chat.*
import android.view.LayoutInflater
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.dialogflow.v2.*
import java.util.*
import android.widget.*
import android.widget.Toast
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.text.Editable
import com.google.cloud.dialogflow.v2.DetectIntentResponse
import com.google.cloud.dialogflow.v2.SessionName
import com.google.cloud.dialogflow.v2.SessionsClient
import com.google.cloud.dialogflow.v2.SessionsSettings
import com.google.common.collect.Lists
import kotlinx.android.synthetic.main.activity_chat.regresar
import kotlinx.android.synthetic.main.activity_fci_info.*

const val USUARIO = 0
const val BOT = 1
const val ENTRADA_DE_VOZ = 2

class ChatActivity : AppCompatActivity() {

    // Variables
    private var cliente: SessionsClient? = null
    private var sesion: SessionName? = null
    private val uuid: String = UUID.randomUUID().toString()
    private var asistente_voz: TextToSpeech?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        regresar.setOnClickListener{
            val regresar: Intent = Intent(this, MainActivity:: class.java)
            startActivity(regresar)
        }

        val scrollview = findViewById<ScrollView>(R.id.scroll_chat)
        scrollview.post {
            scrollview.fullScroll(ScrollView.FOCUS_DOWN)
        }

        val cajaMensajes = findViewById<EditText>(R.id.cajadetexto)
        cajaMensajes.setOnKeyListener { view, keyCode, event ->
            if (event.action === KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                        enviarMensaje(enviar)
                        true
                    }
                    else -> {
                    }
                }
            }
            false
        }

        // Pasamos un setOnClickListener al botón para enviar mensajes llamando al
        // método enviarMensaje() el cual crearemos más adelante
        enviar.setOnClickListener(this::enviarMensaje)

        // Al botón del microfono también le pasamos un setOnClickListener, este envia mensajes de audio con el
        // método enviarMensajeMicrofono() el cual crearemos más adelante
        microfono.setOnClickListener(this::enviarMensajeMicrofono)

        // Llamamos al método iniciarAsistente() el cual crearemos más adelante
        iniciarAsistente()

        // Llamamos al método iniciarAsistenteVoz() el cual crearemos más adelante
        iniciarAsistenteVoz()
    }

    // Función inicarAsistente
    private fun iniciarAsistente() {
        try {
            // Archivo JSON de configuración de la cuenta de Dialogflow (Google Cloud Platform)
            val config = resources.openRawResource(R.raw.credenciales)

            // Leemos las credenciales de la cuenta de Dialogflow (Google Cloud Platform)
            val credenciales = GoogleCredentials.fromStream(config)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"))

            // Leemos el 'projectId' el cual se encuentra en el archivo 'credenciales.json'
            val projectId = (credenciales as ServiceAccountCredentials).projectId

            // Construimos una configuración para acceder al servicio de Dialogflow (Google Cloud Platform)
            val generarConfiguracion: SessionsSettings.Builder = SessionsSettings.newBuilder()

            // Configuramos las sesiones que usaremos en la aplicación
            val configurarSesiones: SessionsSettings =
                generarConfiguracion.setCredentialsProvider(FixedCredentialsProvider.create(credenciales))
                    .build()
            cliente = SessionsClient.create(configurarSesiones)
            sesion = SessionName.of(projectId, uuid)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    // Función iniciarAsistenteVoz
    private fun iniciarAsistenteVoz() {

        asistente_voz = TextToSpeech(applicationContext,object : TextToSpeech.OnInitListener {
            override fun onInit(status: Int) {
                if (status != TextToSpeech.ERROR){
                    asistente_voz?.language=Locale("es")
                }
            }

        })

    }

    private fun enviarMensaje(view: View) {

        // Obtenemos el mensaje de la caja de texto y lo pasamos a String
        val mensaje = cajadetexto.text.toString()

        // Si el usuario no ha escrito un mensaje en la caja de texto y presiona el botón enviar, le mostramos
        // un Toast con un mensaje 'Ingresa tu mensaje ...'
        if (mensaje.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this@ChatActivity, getString(R.string.placeholder), Toast.LENGTH_LONG).show()
        }

        // Si el usuario agrego un mensaje a la caja de texto, llamamos al método agregarTexto()
        else {
            agregarTexto(mensaje, USUARIO)
            cajadetexto.setText("")

            // Enviamos la consulta del usuario al Bot
            val ingresarConsulta =
                QueryInput.newBuilder().setText(TextInput.newBuilder().setText(mensaje).setLanguageCode("es")).build()
            solicitarTarea(this@ChatActivity, sesion!!, cliente!!, ingresarConsulta).execute()
        }
    }

    private fun enviarMensajeMicrofono(view:View){

        // Llamamos al intento para reconocer voz del usuario y convertirla a texto
        val intento = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        // Definimos los modelos de reconocimiento de voz
        intento.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )

        // Le decimos que haga el reconocimiento de voz en el idioma local 'Locale.getDefault()'
        intento.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        // Si el usuario no habla algo, le mostramos el mensaje 'Di algo en el micrófono ...'
        intento.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            getString(R.string.mensajedevoz)
        )

        // Si todo va bien, enviamos el audio del usuario al Bot
        try {
            startActivityForResult(intento, ENTRADA_DE_VOZ)
        }

        // Si el dispositivo del usuario no es compatible con la función del micrófono
        // Le mostramos el mensaje 'Tu teléfono no es compatible con la función de micrófono ...'
        // en un Toast
        catch (a: ActivityNotFoundException) {
            Toast.makeText(
                applicationContext,
                getString(R.string.mensajedevoznoadmitido),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun agregarTexto(mensaje: String, type: Int) {

        // Coloco el FramLayout dentro de la variable layoutFrm
        val layoutFrm: FrameLayout

        // Según sea el rol, le cargamos el FrameLayout y llamamos a un método respectivo
        // Los métodos agregarTextoUsuario() y agregarTextoBot() los crearé más adelante
        when (type) {
            USUARIO -> layoutFrm = agregarTextoUsuario()
            BOT -> layoutFrm = agregarTextoBot()
            else -> layoutFrm = agregarTextoBot()
        }

        // Si el usuario hace clic en la caja de texto
        layoutFrm.isFocusableInTouchMode = true

        // Pasamos un LinearLayout
        linear_chat.addView(layoutFrm)

        // Mostramos los textos de los mensajes en un TextView
        val textview = layoutFrm.findViewById<TextView>(R.id.msg_chat)
        textview.setText(mensaje)


        // Si el usuario sale del modo escritura, ocultamos el teclado del dispositivo
        // El método 'ocultarTeclado()' lo crearemos más adelante
        configTeclado.ocultarTeclado(this)

        // Enfocamos el TextView Automáticamente
        layoutFrm.requestFocus()

        // Volvemos a cambiar el enfoque para editar el texto y continuar escribiendo
        cajadetexto.requestFocus()

        // Si es un cliente el que envía un mensaje al Bot, cargamos el método 'TexToSpeech'
        // 'TexToSpeech' junto a otras métodos procesa los mensajes de voz que seran enviados al Bot
        if(type!= USUARIO) asistente_voz?.speak(mensaje,TextToSpeech.QUEUE_FLUSH,null)

    }

    // Colocamos los mensajes del Usuario en el layout 'mensaje_usuario'
    fun agregarTextoUsuario(): FrameLayout {
        val inflater = LayoutInflater.from(this@ChatActivity)
        return inflater.inflate(R.layout.mensaje_usuario, null) as FrameLayout
    }

    // Colocamos los mensajes del Bot en el layout 'mensaje_bot'
    fun agregarTextoBot(): FrameLayout {
        val inflater = LayoutInflater.from(this@ChatActivity)
        return inflater.inflate(R.layout.mensaje_bot, null) as FrameLayout
    }

    fun validar(response: DetectIntentResponse?) {
        try {
            if (response != null) {

                // fulfillmentText retorna un String (Texto) al usuario en la pantalla
                // fulfillmentMessagesList (Objeto) retorna una lista de objetos
                var respuestaBot: String = ""

                if(response.queryResult.fulfillmentText==" ")
                    respuestaBot = response.queryResult.fulfillmentMessagesList[0].text.textList[0].toString()
                else
                    respuestaBot = response.queryResult.fulfillmentText

                // Pasamos el método agregarTexto()
                agregarTexto(respuestaBot, BOT)

            } else {
                // Mostramos un mensaje si el audio que envio el usuario no se entiende
                agregarTexto(getString(R.string.audio_no_se_entiende), BOT)
            }
        }catch (e:Exception){
            // Mostramos al usuario el texto 'Por Favor, ingresa un mensaje'
            agregarTexto(getString(R.string.ingresa_mensaje), BOT)
        }
    }

    override fun onActivityResult(codigoSolicitud: Int, codigoResultado: Int, datos: Intent?) {
        super.onActivityResult(codigoSolicitud, codigoResultado, datos)

        // Obtenemos el resultado de nuestra actividad principal
        // Si la variable codigoResultado esta ok y la variable datos no es null
        when(codigoSolicitud){
            ENTRADA_DE_VOZ->{
                if(codigoResultado == RESULT_OK
                    && datos != null){

                    // getStringArrayListExtra recupera datos extendidos del Intent
                    val resultado = datos.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

                    // El usuario puede agregar otro mensaje
                    cajadetexto.text = Editable.Factory.getInstance().newEditable(resultado?.get(0))

                    // El usuario puede hacer uso del micrófono
                    enviarMensaje(microfono)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Si la aplicación es cerrada, detenemos el asistente de voz
        if(asistente_voz !=null){
            asistente_voz?.stop()
            asistente_voz?.shutdown()
        }
    }

}