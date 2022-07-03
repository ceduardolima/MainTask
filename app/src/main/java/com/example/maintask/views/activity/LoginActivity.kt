package com.example.maintask.views.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.maintask.R
import com.example.maintask.utils.NetworkChecker
import com.example.maintask.viewmodel.CreateAccountViewModel
import com.example.maintask.viewmodel.LoginViewModel
import de.hdodenhof.circleimageview.CircleImageView

class LoginActivity : AppCompatActivity(), CreateAccountViewModel.Callbacks{

    private lateinit var navController: NavController
    private lateinit var dialog: AlertDialog
    override var selectedPhotoPath: Uri? = null

    // Executa a ação caso a permissão tenha sido aceita, caso não esteja aceita ele mostra a dialog
    private val requestGallery =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permition ->
            if (permition) {
                resultGallery.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            } else {
                showDialogPermition()
            }
        }

    // Ação de pegar a imagem da galeria
    private val resultGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.data != null) {
                selectedPhotoPath = result.data?.data!!

                val bitmap: Bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(
                        baseContext.contentResolver,
                        result.data?.data
                    )
                } else {
                    val source = ImageDecoder.createSource(
                        this.contentResolver,
                        result.data?.data!!
                    )
                    ImageDecoder.decodeBitmap(source)
                }
                val image = findViewById<CircleImageView>(R.id.create_account_photo)
                image.setImageBitmap(bitmap)
            }
        }

    companion object {
        private val PERMITION_GALLERY = android.Manifest.permission.READ_EXTERNAL_STORAGE
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        // Armazena o navHost
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.activity_login_navHostFragment) as NavHostFragment
        // Define o navController a partir do navHost
        navController = navHostFragment.navController
    }

    // Verifica, de forma generalizada, a permissão
    fun verifyPermition(permission: String) =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    // Verifica se a permissao da geria foi aceita, e trabalha nos 3 casos possiveis
    override fun verifyGalleryPermition() {
        val permissionGranted = verifyPermition(PERMITION_GALLERY)

        when {
            // Caso a permissão ja tenha sido aceita
            permissionGranted -> {
                resultGallery.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            }

            // Caso a permissão não tenha sido dada
            shouldShowRequestPermissionRationale(PERMITION_GALLERY) -> showDialogPermition()

            // A permissão nãi tinha sido aceita, porém ele aceitou no momento em que foi proposta
            else -> requestGallery.launch(PERMITION_GALLERY)
        }
    }

    private fun showDialogPermition() {
        // Cria uma dialog
        val builder = AlertDialog.Builder(this)
            .setTitle("Atenção")
            .setMessage("Precisamos do acesso a galeria do dispositivo, deseja permitir agora?")
            .setNegativeButton("Não") { _, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Sim") { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dialog.dismiss()
            }
        dialog = builder.create()
        dialog.show()
    }
}