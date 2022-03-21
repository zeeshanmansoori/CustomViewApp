//package com.google.customviewapp.tools
//
//import android.util.Log
//import org.docx4j.Docx4J
//import org.docx4j.XmlUtils
//import org.docx4j.convert.out.FOSettings
//import org.docx4j.convert.out.fo.renderers.FORendererApacheFOP
//import org.docx4j.fonts.BestMatchingMapper
//import org.docx4j.fonts.Mapper
//import org.docx4j.model.fields.FieldUpdater
//import org.docx4j.openpackaging.packages.WordprocessingMLPackage
//import java.io.File
//import java.io.FileOutputStream
//import java.io.OutputStream
//
//class ConvertOutPDFviaXSLFO(
//    private var inputfilepath: String,
//    private var outputfilepath: String
//) {
//
//
//    fun generatePdf() {
//
//
//        log( "generatePdf: Loading file from $inputfilepath")
//        val wordMLPackage: WordprocessingMLPackage? = WordprocessingMLPackage.load(
//            File(
//                inputfilepath
//            )
//        )
//        // Refresh the values of DOCPROPERTY fields
//        var updater: FieldUpdater? = null
//        //		updater = new FieldUpdater(wordMLPackage);
////		updater.update(true);
//
//        // Set up font mapper (optional)
////		Mapper fontMapper = new IdentityPlusMapper();  // Only for Windows, unless you have Microsoft's fonts installed
//        val fontMapper: Mapper = BestMatchingMapper() // Good for Linux (and OSX?)
//        wordMLPackage?.fontMapper = fontMapper
//
//
//        val foSettings: FOSettings? = FOSettings()
//        foSettings!!.setWmlPackage(wordMLPackage)
//        foSettings.settings
//
//        Log.d(
//            "zeeshan", "generatePdf:${
//                XmlUtils.marshaltoString(
//                    foSettings.apacheFopConfiguration
//                )
//            }"
//        )
//
//        foSettings.foDumpFile = File("$inputfilepath.fo")
//
//
//        val fopFactoryBuilder = FORendererApacheFOP.getFopFactoryBuilder(foSettings)
//
//
//        val fopFactory = fopFactoryBuilder.build()
//
//        val foUserAgent = FORendererApacheFOP.getFOUserAgent(foSettings, fopFactory)
//        foUserAgent.title = "my title zeeshan"
//
////	    foUserAgent.getRendererOptions().put("pdf-a-mode", "PDF/A-1b");
//
//        // PDF/A-1a, PDF/A-2a and PDF/A-3a require accessibility to be enabled
//        // see further https://stackoverflow.com/a/54587413/1031689
////	    foUserAgent.setAccessibility(true); // suppress "missing language information" messages from FOUserAgent .processEvent
//
//
//        // Document format:
//        // The default implementation of the FORenderer that uses Apache Fop will output
//        // a PDF document if nothing is passed via
//        // foSettings.setApacheFopMime(apacheFopMime)
//        // apacheFopMime can be any of the output formats defined in org.apache.fop.apps.MimeConstants eg org.apache.fop.apps.MimeConstants.MIME_FOP_IF or
//        // FOSettings.INTERNAL_FO_MIME if you want the fo document as the result.
//        //foSettings.setApacheFopMime(FOSettings.INTERNAL_FO_MIME);
//
//        // exporter writes to an OutputStream.
//
//        val os: OutputStream = FileOutputStream(outputfilepath)
//
//        Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL)
//        log( "generatePdf: Saved: $outputfilepath")
//
//        // Clean up, so any ObfuscatedFontPart temp files can be deleted
//        if (wordMLPackage?.mainDocumentPart?.fontTablePart != null) {
//            wordMLPackage.mainDocumentPart?.fontTablePart?.deleteEmbeddedFontTempFiles()
//        }
//
//    }
//
//}