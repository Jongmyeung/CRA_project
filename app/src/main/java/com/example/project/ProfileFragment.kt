package com.example.project

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.project.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentUserUid: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        currentUserUid = firebaseAuth.currentUser?.uid ?: ""

        getUserInfoFromFirestore()

        binding.btnForProfile.setOnClickListener {
            try {
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame_container, UserProfileFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            } catch (e : IllegalArgumentException) {
                Log.e("NavigationError", "Navigation destination not found: ${e.message}")
            }
        }

        binding.btnForEdit.setOnClickListener {
            try {
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame_container, EditProfileFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            } catch (e : IllegalArgumentException) {
                Log.e("NavigationError", "Navigation destination not found: ${e.message}")
            }
        }

        return binding.root
    }

    private fun getUserInfoFromFirestore() {
        firestore.collection("users").document(currentUserUid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userEmail = document.getString("email") ?: ""

                    try {
                        // QR 코드 생성
                        val barcodeEncoder = BarcodeEncoder()
                        val content = "Email: $userEmail"
                        val bitmap: Bitmap = barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 250, 250)

                        // ImageView에 QR 코드 설정
                        val imageViewQrCode: ImageView = binding.ivQrCode
                        imageViewQrCode.setImageBitmap(bitmap)
                    } catch (e: WriterException) {
                        val errorMessage = "QR 코드를 생성하는 중에 오류가 발생했습니다: ${e.message}"
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(),"사용자 정보를 가져오는 데 문제가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "사용자 정보를 가져오는 데 실패했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}