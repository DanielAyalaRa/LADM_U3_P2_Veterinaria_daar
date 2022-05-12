package mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.ui.registropropietario

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegistroPropietarioViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "REGISTRO DE PROPIETARIO"
    }
    val text: LiveData<String> = _text
}