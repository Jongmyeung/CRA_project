package com.example.project

import android.app.Activity
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
import androidx.activity.result.contract.ActivityResultContracts
import com.example.project.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentUserUid: String
    private val qrCodeScanLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(result.resultCode, result.data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                    // 여기서 result.contents를 통해 QR 코드에서 읽은 내용을 사용할 수 있습니다.
                }
            }
        } else {
            Toast.makeText(requireContext(), "Scan failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        currentUserUid = firebaseAuth.currentUser?.uid ?: ""

        getUserInfoFromFirestore()

        binding.btnQrCodeCognize.setOnClickListener {
            startQrCodeScan()
        }

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

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if(currentUser != null) {
            val userId = currentUser.uid
            firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if(document != null && document.exists()) {
                        val userName = document.getString("name")

                        val sentenceName = "[${userName}님]"

                        binding.tv1Profile.text = sentenceName
                    }
                }
                .addOnFailureListener { exception ->

                }
        }

        return binding.root
    }

    private fun startQrCodeScan() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR Code")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()
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