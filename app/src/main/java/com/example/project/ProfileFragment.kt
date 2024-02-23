package com.example.project

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.project.databinding.FragmentProfileBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        
        try {
            // QR 코드 생성
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.encodeBitmap("", BarcodeFormat.QR_CODE, 400, 400)

            // ImageView에 QR 코드 설정
            val imageViewQrCode: ImageView = binding.ivQrCode
            imageViewQrCode.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }

        binding.btnForProfile.setOnClickListener {

        }

        binding.btnForEdit.setOnClickListener {

        }

        return binding.root
    }

}