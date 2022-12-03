package com.example.themovietest.view

import android.Manifest.permission
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.themovietest.databinding.FragmentGalleryBinding
import com.example.themovietest.viewmodel.GalleryViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class GalleryFragment : Fragment() {
    private var storageRef: StorageReference? = null

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val REQUEST_SELECT_FILE = 100
    private var mCameraPhotoPath = ""
    var subidos = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            var allAreGranted = true
            for (b in result.values) {
                allAreGranted = allAreGranted && b
            }

            if (allAreGranted) {
                onSubirImagen()
            } else mostrarDialogo()
        }

        binding.btnSelectImage.setOnClickListener {
            solicitarPermisos()
        }

        val storage = Firebase.storage("gs://themovietest-a2307.appspot.com")
        storageRef = storage.reference
    }

    private fun solicitarPermisos() {
        val appPerms = arrayOf(
            permission.READ_EXTERNAL_STORAGE,
            permission.WRITE_EXTERNAL_STORAGE,
            permission.CAMERA
        )
        permissionLauncher.launch(appPerms)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != REQUEST_SELECT_FILE) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }
        var results: Array<Uri?>? = null
        val mArrayUri: ArrayList<Uri?>?
        if (resultCode == Activity.RESULT_OK) {
            val extras = data!!.extras
            if (extras != null) {
                if (mCameraPhotoPath != null) {
                    results = arrayOf(Uri.parse(mCameraPhotoPath))
                }
            } else {
                val dataString = data.dataString
                if (dataString != null) {
                    results = arrayOf(Uri.parse(dataString))
                } else {
                    if (data.clipData != null) {
                        mArrayUri = java.util.ArrayList()
                        for (index in 0 until data.clipData!!.itemCount) {
                            val item = data.clipData!!.getItemAt(index)
                            mArrayUri.add(item.uri)
                        }
                        results = arrayOfNulls(mArrayUri.size)
                        results = mArrayUri.toArray(results)
                    }
                }
            }
            for (index in results!!.indices) {
                val imagefile = results[index]
                val riversRef: StorageReference =
                    storageRef!!.child("fotos").child(imagefile!!.lastPathSegment!!)
                val uploadTask = riversRef.putFile(imagefile)
                if (index == results.size - 1) subidos = true
                uploadTask.addOnFailureListener {
                    // Handle unsuccessful uploads
                }.addOnSuccessListener {
                    if (subidos) binding.txvEstatus.text = "Imagenes Subidas!"
                    else binding.txvEstatus.text =
                        "Subiendo Imagenes!"
                }
            }
        } else {
            mostrarDialogo(false, "Seleccionar / Tomar Foto",
            "Revisar procesamiento despues de tomar o seleccionar imagen")
        }
    }

    private fun onSubirImagen() {
        var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent?.resolveActivity(activity!!.packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile();
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
            } catch (ex: IOException) {
                Log.e("No se creo", "" + ex);
            }

            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.absolutePath;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            } else {
                takePictureIntent = null
            }
        }

        val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.type = "image/*"
        contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        val intentArray: Array<Intent?> = takePictureIntent?.let { arrayOf(it) } ?: arrayOfNulls(0)

        val chooserIntent = Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        this.startActivityForResult(chooserIntent, REQUEST_SELECT_FILE);
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File = this.activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
    }

    private fun mostrarDialogo(esPermisos: Boolean = true, titulo: String? = "", contenido: String? = "") {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(if (esPermisos) "Necesita Aceptar los permisos para Continuar" else contenido)
            .setTitle(if (esPermisos) "Permisos Requeridos" else titulo)
        builder.setPositiveButton(
            "OK"
        ) { _, _ ->
            if (esPermisos)
                solicitarPermisos()
        }

        val dialog = builder.create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}