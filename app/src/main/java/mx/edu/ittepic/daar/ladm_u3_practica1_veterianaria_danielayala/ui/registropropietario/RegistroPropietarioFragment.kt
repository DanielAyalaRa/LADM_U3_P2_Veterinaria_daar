package mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.ui.registropropietario

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.clases.Propietario
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.databinding.FragmentRegistroPropietarioBinding
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.ui.actualizarpropietario.ActualizarMainActivity

class RegistroPropietarioFragment : Fragment() {

    private var _binding: FragmentRegistroPropietarioBinding? = null
    var baseRemota = FirebaseFirestore.getInstance()
    var coleccion1 = "propietario"
    var datalista = ArrayList<String>()
    var listaId = ArrayList<String>()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(RegistroPropietarioViewModel::class.java)

        _binding = FragmentRegistroPropietarioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        baseRemota
            .collection(coleccion1)
            .addSnapshotListener { query, error ->
                if (error != null) {
                    //SI HUBO UNA EXCEPCIÓN
                    mensaje2(error.message!!)
                    return@addSnapshotListener
                }
                datalista.clear()
                listaId.clear()
                for (documento in query!!) {
                    var cadena =
                        "Nombre: ${documento.getString("nombre")} -- Telefono: ${
                            documento.getString(
                                "telefono"
                            )
                        }-- Edad: ${documento.getLong("edad")}"
                    datalista.add(cadena)

                    listaId.add(documento.id.toString())
                }

                binding.listaPropietario.adapter = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    datalista
                )
                binding.listaPropietario.setOnItemClickListener { adapterView, view, indice, l ->
                    dialogoEliminarActualiza(indice)
                }
            }

        binding.insertar.setOnClickListener {
            val c0 = binding.txtcurp.text.toString()
            val c1 = binding.txtnombrePropietario.text.toString()
            val c2 = binding.txttelefono.text.toString()
            val c3 = binding.txtedadPropietario.text.toString()

            val regex = "^[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}$".toRegex()

            if (!(c0 == "" || c1 == "" || c2 == "" || c3 == "")) {
                if (!regex.containsMatchIn(c0)) {
                    mensaje("CURP","NO CUMPLE CON LOS PARAMETROS DE UNA CURP")
                } else if(!(c2.length == 10)) {
                    mensaje("TELEFONO","EL NÚMERO DEBEN SER 10 DIGITOS")
                } else {
                    var propietario = Propietario(requireContext())

                    propietario.curp = binding.txtcurp.text.toString()
                    propietario.nombre = binding.txtnombrePropietario.text.toString()
                    propietario.telefono = binding.txttelefono.text.toString()
                    propietario.edad = binding.txtedadPropietario.text.toString().toInt()

                    propietario.insertar()
                    limpiarCampos()
                }
            } else {
                mensaje("ATENCIÓN","HAY CAMPOS VACIOS")
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun dialogoEliminarActualiza(posicion :Int) {
        var idSeleccionar = listaId.get(posicion)
        var propietario = Propietario(requireContext())

        AlertDialog.Builder(requireContext())
            .setTitle("Atención!!")
            .setMessage("¿QUÉ DESEAS HACER CON\n${datalista.get(posicion)}?")
            .setPositiveButton("ELIMINAR") {d,i ->
                propietario.eliminar(idSeleccionar)
            }
            .setNeutralButton("ACTUALIZAR") {d,i ->
                val otraVentana = Intent(requireActivity(), ActualizarMainActivity::class.java)
                otraVentana.putExtra("propietarioActualizar", idSeleccionar)
                startActivity(otraVentana)
            }
            .setNegativeButton("CANCELAR") {d,i -> }
            .show()
    }

    fun mostrarPropietario(idSeleccionar : String) : String {
        var cad = ""
        baseRemota
            .collection(coleccion1)
            .document(idSeleccionar)
            .get()
            .addOnSuccessListener {
                cad = "${it.getString("nombre")}, \nTelefono: ${it.getString("telefono")}, \n" +"Edad: ${it.getLong("edad")}"
            }
            .addOnFailureListener {
                mensaje2("ERROR: ${it.message!!}")
            }
        return  cad
    }

    fun limpiarCampos() {
        binding.txtcurp.setText("")
        binding.txtnombrePropietario.setText("")
        binding.txttelefono.setText("")
        binding.txtedadPropietario.setText("")
    }

    private fun mensaje(titulo : String,error : String) {
        AlertDialog.Builder(requireContext())
            .setTitle(titulo)
            .setMessage(error)
            .setNeutralButton("ACEPTAR") {d,i -> }
            .show()
    }

    private fun mensaje2(error : String) {
        AlertDialog.Builder(requireContext())
            .setMessage(error)
            .show()
    }
}