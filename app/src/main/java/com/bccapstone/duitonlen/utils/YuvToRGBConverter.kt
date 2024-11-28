package com.bccapstone.duitonlen.utils
/*
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicYuvToRGB
import android.renderscript.Type
import androidx.camera.core.ImageProxy

class YuvToRgbConverter(context: Context) {
    private val renderScript = RenderScript.create(context)
    private lateinit var scriptYuvToRgb: ScriptIntrinsicYuvToRGB
    private lateinit var yuvBuffer: ByteArray
    private lateinit var inputAllocation: Allocation
    private lateinit var outputAllocation: Allocation
    private var pixelCount: Int = -1

    @Synchronized
    fun yuvToRgb(image: ImageProxy, output: Bitmap) {
        // Lazy initialization of the YuvToRgb converter
        if (!::scriptYuvToRgb.isInitialized) {
            scriptYuvToRgb = ScriptIntrinsicYuvToRGB.create(renderScript, Element.U8_4(renderScript))
        }

        // Get the YUV data
        val pixelCount = image.cropRect.width() * image.cropRect.height()
        val planes = image.planes

        if (this.pixelCount != pixelCount) {
            if (::yuvBuffer.isInitialized) {
                renderScript.destroy()
            }

            this.pixelCount = pixelCount
            yuvBuffer = ByteArray(planes[0].buffer.capacity())
        }

        // Copy YUV data to a single buffer
        planes[0].buffer.get(yuvBuffer, 0, planes[0].buffer.capacity())

        // Create Allocations for input and output
        if (!::inputAllocation.isInitialized) {
            val elemType = Type.Builder(renderScript, Element.YUV(renderScript))
                .setX(image.width)
                .setY(image.height)
                .setYuvFormat(ImageFormat.NV21)
                .create()
            inputAllocation = Allocation.createTyped(renderScript, elemType)
        }

        if (!::outputAllocation.isInitialized) {
            val elemType = Type.Builder(renderScript, Element.RGBA_8888(renderScript))
                .setX(image.width)
                .setY(image.height)
                .create()
            outputAllocation = Allocation.createTyped(renderScript, elemType)
        }

        // Convert YUV to RGB
        inputAllocation.copyFrom(yuvBuffer)
        scriptYuvToRgb.setInput(inputAllocation)
        scriptYuvToRgb.forEach(outputAllocation)
        outputAllocation.copyTo(output)
    }
}
*/