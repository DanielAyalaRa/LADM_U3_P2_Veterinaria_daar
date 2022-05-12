package mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.ui.consultamascota

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.R
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.clases.Mascota
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.clases.Propietario
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.databinding.ConsultaMascotaFragmentBinding
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.databinding.ConsultaPropietarioFragmentBinding
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.ui.consultapropietario.ConsultaPropietarioViewModel

class ConsultaMascotaFragment : Fragment() {

    private var _binding: ConsultaMascotaFragmentBinding? = null

    private val binding get() = _binding!!
    var baseRemota = FirebaseFirestore.getInstance()
    var coleccion2 = "mascota"
    var datalista = ArrayList<String>()
    var listaId = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(ConsultaMascotaViewModel::class.java)

        _binding = ConsultaMascotaFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val spinner: Spinner = binding.SpConsultasMascotas
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.buscarMascota,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        //mostrarDatosEnListView()

        binding.btnBuscar.setOnClickListener {
            var busqueda = binding.buscarMascota.text.toString()

            if (busqueda == "") {
               // mostrarDatosEnListView()
            } else {
                //mostrarMascotaFiltro(busqueda,binding.SpConsultasMascotas.selectedItem.toString())
            }
        }

        return  root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*fun mostrarDatosEnListView() {
        var listaFiltro = Mascota(requireContext()).mostrarTodos()
        var listaData = ArrayList<String>()
        var Data = Mascota(requireContext())

        listaIDs.clear()
        (0..listaFiltro.size-1).forEach {
            val pets = listaFiltro.get(it)
            Data.curp = pets.id_mascota
            Data.nombre = pets.nombre
            Data.raza = pets.raza
            Data.curp = pets.curp

            listaData.add(Data.nombreMascota())
            listaIDs.add(pets.id_mascota)
        }

        binding.lista.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,listaData)
    }

    fun mostrarMascotaFiltro(busqueda:String, filtro:String) {
        var listaFiltro = Mascota(requireContext()).buscarFiltroMascota(busqueda,filtro)
        var listaData = ArrayList<String>()
        var Data = Mascota(requireContext())

        listaIDs.clear()
        (0..listaFiltro.size-1).forEach {
            val pets = listaFiltro.get(it)
            Data.curp = pets.id_mascota
            Data.nombre = pets.nombre
            Data.raza = pets.raza
            Data.curp = pets.curp

            listaData.add(Data.nombreMascota())
            listaIDs.add(pets.id_mascota)
        }

        binding.lista.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,listaData)
    }*/

}