package com.example.challengechapter5.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.challengechapter5.R
import com.example.challengechapter5.databinding.FragmentUpdateProfileBinding
import com.example.challengechapter5.room.User
import com.example.challengechapter5.view.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*


class UpdateProfileFragment : DialogFragment() {

    private lateinit var binding : FragmentUpdateProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var firebaseAuth: FirebaseAuth
    private var id : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        firebaseAuth = FirebaseAuth.getInstance()

        setField(firebaseAuth.currentUser?.email!!)

        setFullScreen()

        binding.inputTanggalLahir.setOnClickListener {
            setInputBirthDate()
        }

        binding.btnUpdateProfile.setOnClickListener {
            updateProfile()
        }
    }

    private fun updateProfile() {
        val username = binding.inputUsername.text.toString()
        val fullName = binding.inputNamaLengkap.text.toString()
        val birthDate = binding.inputTanggalLahir.text.toString()
        val addressUser = binding.inputAlamat.text.toString()
        GlobalScope.async {
            userViewModel.updateDataUser(User(
                id,
                firebaseAuth.currentUser!!.email.toString(),
                username,
                fullName,
                birthDate,
                addressUser
            ))

            userViewModel.getDataUser(firebaseAuth.currentUser!!.email.toString())
        }
//        dismiss()
        findNavController().navigate(R.id.action_updateProfileFragment_to_profileFragment)
    }

    private fun setInputBirthDate() {
        val calendar = Calendar.getInstance()
        val mInputYear = calendar.get(Calendar.YEAR)
        val mInputMonth = calendar.get(Calendar.MONTH)
        val mInputDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                binding.inputTanggalLahir.setText(String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear+1, year))
            }, mInputYear, mInputMonth, mInputDay)
        datePickerDialog.show()
    }

    private fun DialogFragment.setFullScreen() {
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun setField(email : String){
        userViewModel.getDataUser(email)

        userViewModel.getDataUserProfile().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.profile = it
            id = it.id
        })
    }

}