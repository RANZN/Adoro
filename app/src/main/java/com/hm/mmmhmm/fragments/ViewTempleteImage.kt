package com.hm.mmmhmm.fragments

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.hm.mmmhmm.R
import com.hm.mmmhmm.helper.loadBitMap
import kotlinx.android.synthetic.main.fragment_view_templete_image.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


class ViewTempleteImage : Fragment() {



    var imageBytes:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_templete_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageBytes= getArguments()?.getString("Image").toString()
        if (imageBytes != "")
            showImage(view,imageBytes)
        tvDownloadImg.setOnClickListener(View.OnClickListener {
            download()
        })

        /*
    String bbb = getArguments().getString("Image");
    Bitmap bitmap = BitmapFactory.decodeFile(bbb);
    iv.setImageBitmap(bitmap);
     */
    }

    private fun download() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val imageByteArray = Base64.decode(imageBytes, Base64.DEFAULT)
                var bitmap = Glide.with(requireActivity().applicationContext).asBitmap()
                    .load(imageByteArray)
                    .apply(RequestOptions().override(100).downsample(DownsampleStrategy.CENTER_INSIDE).skipMemoryCache(true).diskCacheStrategy(
                        DiskCacheStrategy.NONE))
                    .submit().get()
                val wrapper = ContextWrapper(requireActivity().applicationContext)


                var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
                file = File(file, "img.jpg")
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
                out.flush()
                out.close()
                Toast.makeText(requireActivity(),"Saved",Toast.LENGTH_SHORT).show()
            }
            catch (e: Exception) {
                Toast.makeText(requireActivity(),e.toString(),Toast.LENGTH_SHORT).show()
                Log.d("vijay",e.toString())
            }
        }

    }

    private fun showImage(view: View, imageBytes: String) {
        val imageByteArray = Base64.decode(imageBytes, Base64.DEFAULT)
        ivShowPic.loadBitMap(
            imageByteArray,
            R.color.text_gray,
            R.color.text_gray,
            false
        )
    }


}