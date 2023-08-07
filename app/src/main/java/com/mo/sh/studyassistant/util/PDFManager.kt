package com.mo.sh.studyassistant.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.text.TextPaint
import androidx.documentfile.provider.DocumentFile
import com.mo.sh.studyassistant.R
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class PDFManager(private val context: Context) {

    init {
        PDFBoxResourceLoader.init(context)
    }

    suspend fun extractText(uri: Uri) = suspendCoroutine { continuation ->

        val document: PDDocument? = try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            PDDocument.load(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            continuation.resume(
                PDFResult.Failure(
                    context.getString(R.string.error_extracting_text)
                )
            )
            return@suspendCoroutine
        }
        try {
            val pdfStripper = PDFTextStripper()
            val text = pdfStripper.getText(document)
            if (text.length < 100)
                continuation.resume(
                    PDFResult.Failure(
                        context.getString(R.string.no_text_recognized)
                    )
                ) else continuation.resume(
                PDFResult.Success(
                    text.removeArabicCharacters()
                )
            )
        } catch (e: IOException) {
            continuation.resume(
                PDFResult.Failure(
                    context.getString(R.string.error_extracting_text)
                )
            )
        } finally {
            try {
                document?.close()
            } catch (e: IOException) {
                continuation.resume(
                    PDFResult.Failure(
                        context.getString(R.string.error_extracting_text)
                    )
                )
            }
        }
    }

    private fun String.removeArabicCharacters(): String {
        // Remove any arabic characters because the api doesn't support them
        val englishRegex = Regex("\\p{InArabic}")
        return replace(englishRegex, "")
    }

    fun writeTextToPdf(text: String, fileName: String, uri: Uri): Boolean {

        val pickedDir = DocumentFile.fromTreeUri(context, uri)
        val mimeType = "application/pdf"
        val newFile = pickedDir!!.createFile(mimeType, fileName)

        val document = PdfDocument()

        val pageWidth = 792
        val pageHeight = 1120
        val padding = 36f
        val contentWidth = pageWidth - 2 * padding
        val contentHeight = pageHeight - 2 * padding
        val lineHeight = 22F

        val paint = TextPaint()
        paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        paint.textSize = lineHeight
        paint.color = Color.BLACK

        val lines = "\n$text".split("\n")
        var lineIndex = 0
        var pageIndex = 0
        var code = false

        while (lineIndex < lines.size) {
            val pageInfo =
                PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageIndex + 1).create()
            val page = document.startPage(pageInfo)
            val canvas = page.canvas

            var y = padding

            while (lineIndex < lines.size && y + lineHeight <= contentHeight && y + lineHeight <= pageHeight - padding) {
                var lineText = ""
                val line = lines[lineIndex]
                val words = line.split(" ")
                for (word in words) {
                    val tempText = "$lineText$word "
                    val lineWidth = paint.measureText(tempText)
                    if (lineWidth <= contentWidth) {
                        lineText = tempText
                    } else {
                        canvas.drawFormatted(lineText, padding, y, paint)
                        y += lineHeight
                        lineText = "$word "
                    }
                }

                if (lineText.trim().startsWith("```")) code = !code

                if (lineIndex > 1 &&
                    lines[lineIndex].trim().startsWith("|") &&
                    !lines[lineIndex - 1].trim().startsWith("|")
                ) {
                    paint.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
                } else {
                    paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
                }
                if (!lineText.trim().startsWith("|---")) {
                    canvas.drawFormatted(lineText, padding, y, paint, true, code)
                }

                if (lineIndex + 1 < lines.size &&
                    lines[lineIndex].trim().startsWith("|") &&
                    !lines[lineIndex + 1].trim().startsWith("|")
                ) {
                    y += lineHeight
                }

                if (!lineText.trim().startsWith("|---")) y += lineHeight
                lineIndex++
            }

            document.finishPage(page)
            pageIndex++
        }

        return try {
            val outputStream = context.contentResolver.openOutputStream(newFile?.uri!!)
            document.writeTo(outputStream)
            outputStream?.close()
            document.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            document.close()
            false
        }
    }


    private fun Canvas.drawFormatted(
        line: String,
        padding: Float,
        y: Float,
        paint: Paint,
        keepPaint: Boolean = false,
        code: Boolean = false
    ) {
        if (line.startsWith("# ")) {
            paint.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
            paint.textSize = 30F
            drawFormatted(line.substring(2), padding, y, paint)
        } else if (line.startsWith("## ")) {
            paint.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
            paint.textSize = 26F
            drawFormatted(line.substring(3), padding, y, paint)
        } else if (line.startsWith("### ")) {
            paint.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
            paint.textSize = 24F
            drawFormatted(line.substring(4), padding, y, paint)
        } else if (line.startsWith("#### ")) {
            paint.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
            paint.textSize = 22F
            drawFormatted(line.substring(5), padding, y, paint)
        } else if (line.contains("**")) {
            val firstIndex = line.indexOf("**")
            val lastIndex = line.lastIndexOf("**")
            if (firstIndex == lastIndex && line.trim().startsWith("**")) {
                paint.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
                paint.textSize = 22F
                drawFormatted(line.replace("**", ""), padding, y, paint)
            } else if (firstIndex != -1 && lastIndex != -1 && firstIndex != lastIndex) {
                val prefixText = line.substring(0, firstIndex).replace("* ", "◉ ")
                val wrappedText = line.substring(firstIndex + 2, lastIndex) + ' '
                val suffixText = line.substring(lastIndex + 2)

                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textSize = 22F
                drawFormatted(prefixText, padding, y, paint)

                paint.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
                val newX = padding + paint.measureText(prefixText)
                drawText(wrappedText, newX, y, paint)

                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                drawFormatted(suffixText, newX + paint.measureText(wrappedText), y, paint)
            } else {
                drawFormatted(line.replace("**", ""), padding, y, paint)
            }
        } else if (line.trim().startsWith("* ")) {
            drawFormatted(line.replace("*", "◉"), padding, y, paint)
        } else if (line.startsWith("```")) {
            paint.typeface = Typeface.create(
                if (code) Typeface.SANS_SERIF else Typeface.MONOSPACE,
                if (code) Typeface.NORMAL else Typeface.ITALIC
            )
        } else if (line.contains('`')) {
            val firstIndex = line.indexOf('`')
            val lastIndex = line.replaceFirst("`", " ").indexOf('`')
            if (firstIndex != -1 && lastIndex != -1 && (lastIndex - firstIndex > 1)) {
                val prefixText = line.substring(0, firstIndex).replace("* ", "◉ ")
                val wrappedText = line.substring(firstIndex + 1, lastIndex) + ' '
                val suffixText = line.substring(lastIndex + 1)

                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textSize = 22F
                drawFormatted(prefixText, padding, y, paint)

                paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.ITALIC)
                val newX = padding + paint.measureText(prefixText)
                drawFormatted(wrappedText, newX, y, paint, code = true)

                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                drawFormatted(suffixText, newX + paint.measureText(wrappedText), y, paint)
            } else {
                drawFormatted(line.replace("`", ""), padding, y, paint)
            }
        } else if (line.trim().startsWith("|") && line.trim().endsWith("|")) {
            drawTableRow(line, padding, y, paint)
        } else {
            if (code && line.isNotBlank()) {
                val rect = RectF(
                    padding,
                    y - 17f,
                    paint.measureText(line) + padding,
                    y + 4f
                )
                paint.color = Color.parseColor("#E7E7E7")
                drawRect(rect, paint)
                paint.color = Color.BLACK
            } else {
                if (!keepPaint) {
                    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                    paint.textSize = 22F
                }
            }
            drawText(line, padding, y, paint)
        }
    }

    private fun Canvas.drawTableRow(text: String, x: Float, y: Float, paint: Paint) {
        val columnCount = text.count { it == '|' } - 1

        val padding = 4f
        val cellWidth = (width - (2 * x)) / columnCount.toFloat()
        val cellHeight = 22f

        val tablePaint = Paint()
        tablePaint.style = Paint.Style.FILL
        tablePaint.color = Color.parseColor("#DFDFDF")
        paint.textSize = 18f

        val columns = text.trim().split("|").map { it.trim() }.filter { it.isNotBlank() }

        var currentX = x
        for (column in columns) {
            val isBold = column.startsWith("#", ignoreCase = true)


            if (!isBold) {
                tablePaint.style = Paint.Style.FILL
                tablePaint.color = Color.parseColor("#F3F3F3")
                drawRect(
                    currentX, y,
                    currentX + cellWidth, y + cellHeight, tablePaint
                )
                tablePaint.style = Paint.Style.STROKE
                tablePaint.color = Color.BLACK
                drawRect(
                    currentX, y,
                    currentX + cellWidth, y + cellHeight, tablePaint
                )
            }

            paint.isFakeBoldText = isBold
            drawText(
                column.removePrefix("#").trim(),
                currentX + padding,
                y + paint.fontSpacing - padding * 2,
                paint
            )
            currentX += cellWidth

        }
    }


    sealed class PDFResult(val text: String) {
        data class Success(val content: String) : PDFResult(content)
        data class Failure(val message: String) : PDFResult(message)
    }

}