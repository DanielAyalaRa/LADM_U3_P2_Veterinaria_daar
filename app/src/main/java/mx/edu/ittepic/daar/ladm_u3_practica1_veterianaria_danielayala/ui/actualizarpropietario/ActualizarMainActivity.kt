package mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.ui.actualizarpropietario

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.clases.Propietario
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.databinding.ActivityActualizarMainBinding
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.databinding.FragmentRegistroPropietarioBinding

class ActualizarMainActivity : AppCompatActivity() {

    lateinit var binding: ActivityActualizarMainBinding
    var idSeleccion = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActualizarMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idSeleccion = this.intent.extras!!.getString("propietarioActualizar")!!
        var propietario = Propietario(this).mostrarPropietario(idSeleccion)

        binding.txtcurp.setText(propietario.curp)
        binding.txtnombrePropietario.setText(propietario.nombre)
        binding.txttelefono.setText(propietario.telefono)
        binding.txtedadPropietario.setText(propietario.edad.toString())

        binding.actualizar.setOnClickListener {
            var propietario = Propietario(this)

            try {
                propietario.curp = binding.txtcurp.text.toString()
                propietario.nombre = binding.txtnombrePropietario.text.toString()
                propietario.telefono = binding.txttelefono.text.toString()
                propietario.edad = binding.txtedadPropietario.text.toString().toInt()
            } catch (e:Exception) {
                AlertDialog.Builder(this)
                    .setTitle("ATENCIÓN")
                    .setMessage("HAY CAMPOS VACIOS")
                    .show()
                return@setOnClickListener
            }
            val regex =
                "^[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}$".toRegex()
            try {
                if (!(propietario.curp == "" || propietario.nombre == "" || propietario.telefono == "" || propietario.edad.toString() == "")) {
                    if (!regex.containsMatchIn(propietario.curp)) {
                        AlertDialog.Builder(this)
                            .setTitle("CURP")
                            .setMessage("NO CUMPLE CON LOS PARAMETROS DE UNA CURP")
                            .setNeutralButton("ACEPTAR") { d, i -> }
                            .show()
                    } else if (!(propietario.telefono.length == 10)) {
                        AlertDialog.Builder(this)
                            .setTitle("TELEFONO")
                            .setMessage("EL NÚMERO DEBEN SER 10 DIGITOS")
                            .setNeutralButton("ACEPTAR") { d, i -> }
                            .show()
                    } else {
                        propietario.actualizar(idSeleccion)
                        limpiarCampos()
                    }
                }
            }  catch(e:Exception) {
                AlertDialog.Builder(this)
                    .setTitle("ATENCIÓN")
                    .setMessage("HAY CAMPOS VACIOS")
                    .show()
            }
        }
    }

    fun limpiarCampos() {
        binding.txtcurp.setText("")
        binding.txtnombrePropietario.setText("")
        binding.txttelefono.setText("")
        binding.txtedadPropietario.setText("")
    }
}