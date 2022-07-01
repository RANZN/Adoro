package com.hm.mmmhmm.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore.Images
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hm.mmmhmm.R
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.loadBitMap
import kotlinx.android.synthetic.main.fragment_view_templete_image.*
import java.io.File
import java.io.FileOutputStream


class ViewTemplateImage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_templete_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageBytes = arguments?.getString("Image").toString()
        val name = arguments?.getString("name").toString()
        val imageByteArray = Base64.decode(imageBytes, Base64.DEFAULT)
        showImage(imageByteArray)
        val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
        tvDownloadImg.setOnClickListener {
            download(bitmap, name)
        }
        tvShareImage.setOnClickListener {
            shareImage(bitmap)
        }
    }

    private fun shareImage(bitmap: Bitmap) {
        val bitmapUri: Uri = Uri.parse(Images.Media.insertImage(requireContext().contentResolver, bitmap, "image", null))
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
        intent.putExtra(Intent.EXTRA_SUBJECT, "Sharing Template")
        intent.putExtra(Intent.EXTRA_TEXT, "${SessionManager.getUserName()} Shared a Template")
        intent.type = "image/png"
        startActivity(Intent.createChooser(intent, "Share Image"))
    }

    private fun download(bitmap: Bitmap, name: String) {

        try {
            val path = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/Adoro")
            val file = File(path, "$name.jpg")
            if (file.exists()) {
                Toast.makeText(requireActivity(), "Already Downloaded", Toast.LENGTH_SHORT).show()
                tvDownloadImg.text = "Downloaded"
                return
            }
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            Toast.makeText(requireActivity(), "Downloaded", Toast.LENGTH_SHORT).show()
            tvDownloadImg.text = "Downloaded"
        } catch (e: Exception) {
            try {
                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "$name.jpg")
                if (file.exists()) {
                    Toast.makeText(requireActivity(), "Already Downloaded", Toast.LENGTH_SHORT).show()
                    tvDownloadImg.text = "Downloaded"
                    return
                }
                Images.Media.insertImage(requireContext().contentResolver, bitmap, name, null)
                Toast.makeText(requireActivity(), "Downloaded", Toast.LENGTH_SHORT).show()
                tvDownloadImg.text = "Downloaded"
            } catch (e: Exception) {
                Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showImage(imageByteArray: ByteArray) {
        ivShowPic.loadBitMap(
            imageByteArray,
            R.color.text_gray,
            R.color.text_gray,
            false
        )
    }


}