package mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.ui.actualizarpropietario

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.clases.Propietario
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.databinding.ActivityActualizarMainBinding
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.databinding.FragmentRegistroPropietarioBinding

class ActualizarMainActivity : AppCompatActivity() {

    lateinit var binding: ActivityActualizarMainBinding
    var baseRemota = FirebaseFirestore.getInstance()
    var idSeleccion = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActualizarMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idSeleccion = intent.extras!!.getString("propietarioActualizar")!!

        baseRemota
            .collection("propietario")
            .document(idSeleccion)
            .get() //Obtiene 1 documento
            .addOnSuccessListener {
                binding.txtcurp.setText(it.getString("curp"))
                binding.txtnombrePropietario.setText(it.getString("nombre"))
                binding.txttelefono.setText(it.getString("telefono"))
                binding.txtedadPropietario.setText(it.getLong("edad").toString())
            }
            .addOnFailureListener {
                mensaje("ERROR: ${it.message!!}")
            }

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

            try {
                if (!(propietario.curp == "" || propietario.nombre == "" || propietario.telefono == "" || propietario.edad.toString() == "")) {
                    if (!(propietario.telefono.length == 10)) {
                        AlertDialog.Builder(this)
                            .setTitle("TELEFONO")
                            .setMessage("EL NÚMERO DEBEN SER 10 DIGITOS")
                            .setNeutralButton("ACEPTAR") { d, i -> }
                            .show()
                    } else {
                        propietario.actualizar(idSeleccion)
                        limpiarCampos()
                        finish()
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

    private fun mensaje(error : String) {
        AlertDialog.Builder(this)
            .setMessage(error)
            .show()
    }

    fun limpiarCampos() {
        binding.txtcurp.setText("")
        binding.txtnombrePropietario.setText("")
        binding.txttelefono.setText("")
        binding.txtedadPropietario.setText("")
    }
}