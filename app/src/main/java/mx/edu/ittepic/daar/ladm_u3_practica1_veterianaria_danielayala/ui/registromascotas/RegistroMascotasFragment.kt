package mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.ui.registromascotas

import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.R
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.clases.Mascota
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.clases.Propietario
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.databinding.FragmentRegistroMascotasBinding
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.ui.actualizarmascota.ActualizarMascotas
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.ui.actualizarpropietario.ActualizarMainActivity


class RegistroMascotasFragment : Fragment() {

    private var _binding: FragmentRegistroMascotasBinding? = null
    var baseRemota = FirebaseFirestore.getInstance()
    var coleccion1 = "propietario"
    var coleccion2 = "mascota"
    var datalista = ArrayList<String>()
    var listaId = ArrayList<String>()

    private val binding get() = _binding!!

    var listaIDs = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(RegistroMascotasViewModel::class.java)

        _binding = FragmentRegistroMascotasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val spinner: Spinner = binding.SpRaza
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.razas,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        baseRemota
            .collection(coleccion2)
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
                        "Nombre: ${documento.getString("nombre")} -- Raza: ${
                            documento.getString(
                                "raza"
                            )
                        }\nPropietario: ${documento.getString("curp")}"
                    datalista.add(cadena)

                    listaId.add(documento.id.toString())
                }

                binding.listaMascotas.adapter = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    datalista
                )
                binding.listaMascotas.setOnItemClickListener { adapterView, view, indice, l ->
                    dialogoEliminarActualiza(indice)
                }
            }

        binding.insertarMascota.setOnClickListener {
            var id = binding.txtcurp.text.toString()
            if (id == "") {
                mensaje("ATENCIÓN","Debe agregar el propietario de la mascota")
                return@setOnClickListener
            } else {
                if (binding.txtnombreMascota.text.toString() == "") {
                    mensaje("ATENCIÓN","Debe agregar el nombre de la mascota")
                    return@setOnClickListener
                } else {
                    var mascota = Mascota(requireContext())

                    mascota.nombre = binding.txtnombreMascota.text.toString()
                    mascota.raza = binding.SpRaza.selectedItem.toString()
                    mascota.curp = binding.txtcurp.text.toString()

                    mascota.insertar()
                }
            }
        }
        binding.btnbuscar.setOnClickListener {
            var buscar = binding.txtbuscarPropietario.text.toString()
            mostrarPropietario(buscar)
        }
        binding.btnLimpiar.setOnClickListener {
            limpiarCampos()
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
                val otraVentana = Intent(requireActivity(), ActualizarMascotas::class.java)
                otraVentana.putExtra("mascotaActualizar", idSeleccionar)
                otraVentana.putExtra("mascotaCurp", propietario.curp)
                startActivity(otraVentana)
            }
            .setNegativeButton("CANCELAR") {d,i -> }
            .show()
    }

    fun mostrarPropietario(buscar:String) {
        var datalista2 = ArrayList<String>()
        var listaId2 = ArrayList<String>()

        baseRemota.collection(coleccion1)
            .whereEqualTo("nombre", buscar)
            .addSnapshotListener { query, error ->
                if (error != null) {
                    //SI HUBO UNA EXCEPCIÓN
                    mensaje2(error.message!!)
                    return@addSnapshotListener
                }
                datalista2.clear()
                listaId2.clear()
                for (documento in query!!) {
                    var cadena =
                        "Curp: ${documento.getString("curp")} -- Nombre: ${documento.getString("nombre")} -- Telefono: ${
                            documento.getString(
                                "telefono"
                            )
                        }-- Edad: ${documento.getLong("edad")}"
                    datalista2.add(cadena)

                    listaId2.add(documento.id.toString())
                }

                binding.listaPropietario.adapter = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    datalista2
                )
                binding.listaPropietario.setOnItemClickListener { adapterView, view, indice, l ->
                    var idSeleccionar = listaId2.get(indice)

                    baseRemota
                        .collection(coleccion1)
                        .document(idSeleccionar)
                        .get()
                        .addOnSuccessListener {
                            binding.txtcurp.setText(it.getString("curp"))
                        }
                        .addOnFailureListener {
                            mensaje2("ERROR: ${it.message!!}")
                        }
                }
            }
    }

    fun limpiarCampos() {
        binding.txtbuscarPropietario.setText("")
        binding.txtcurp.setText("")
        binding.txtnombreMascota.setText("")
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