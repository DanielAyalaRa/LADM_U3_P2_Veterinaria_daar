package mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "INICIO"
    }
    val text: LiveData<String> = _text
}