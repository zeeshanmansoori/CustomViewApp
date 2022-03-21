package com.google.customviewapp//package com.google.customviewapp
//
//import android.content.Intent
//import android.os.Bundle
//import android.os.PersistableBundle
//import androidx.appcompat.app.AppCompatActivity
//import com.google.customviewapp.tools.ConvertOutPDFviaXSLFO
//import com.google.customviewapp.tools.log
//
//
//class WordToDocumentConverterActivity : AppCompatActivity(R.layout.word_to_pdf_activity) {
//
//    private val REQUEST_CODE_OPEN: Int = 1231
//
//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)
//
//        //val converter = ConvertOutPDFviaXSLFO()
//        chooseWord()
//    }
//
//
//    private fun chooseWord() {
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.type = "*/*"
//        val mimetypes = arrayOf(
//            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
//            "application/msword"
//        )
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
//        startActivityForResult(intent, REQUEST_CODE_OPEN)
//    }
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_OPEN) {
//            val uri = data?.data
//            log("file got $uri")
//            if (uri != null) {
//                val converter = ConvertOutPDFviaXSLFO(
//                    uri.toString(),
//                    externalCacheDir!!.absolutePath + "123filename.pdf"
//                )
//                converter.generatePdf()
//            }
//        }
//    }
//}