package com.example.challengechapter5.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.challengechapter5.R
import com.example.challengechapter5.databinding.FragmentLoginBinding
import com.example.challengechapter5.adapter.DataStoreUser
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dataStoreUser: DataStoreUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        dataStoreUser = DataStoreUser.getInstance(requireContext().applicationContext)

        checkActiveAccount()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            loginAccount()
//            deactivateAccount()
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun checkActiveAccount(){
        dataStoreUser.statusUser.asLiveData().observe(viewLifecycleOwner, Observer {
            Log.d("DS", it.toString())
            if(it.toString() == "active"){
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        })
    }
    private fun loginAccount() {
        val email = binding.inputEmail.text.toString()
        val password = binding.inputPassword.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if(it.isSuccessful){

                        lifecycleScope.launch {
                            dataStoreUser.saveDataUser("active", email)
                        }
                        Toast.makeText(context, R.string.login_success, Toast.LENGTH_SHORT).show()

                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }else{
                        Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }else{
            Toast.makeText(context, R.string.input_cannot_empty, Toast.LENGTH_SHORT).show()
        }
    }


}