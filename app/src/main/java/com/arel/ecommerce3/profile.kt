package com.arel.ecommerce3

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import com.arel.ecommerce3.databinding.FragmentProfileBinding

class profile : Fragment() {

    lateinit var binding: FragmentProfileBinding
    lateinit var session: SessionLogin

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = SessionLogin(requireContext())
        binding.cardviewLogout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Yakin Anda ingin Logout?")
            builder.setCancelable(true)
            builder.setNegativeButton("Batal") { dialog, which -> dialog.cancel() }
            builder.setPositiveButton("Ya") { dialog, which ->
                session.logoutUser()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }
}