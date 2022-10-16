import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class MotecParser() {

    fun parseHeader(fileName: String): Header {
        val url = this::class.java.getResource(fileName)
        val file = File(
            url?.toURI() ?: throw IllegalStateException("Could not open file!")
        )

        val bb = ByteBuffer.wrap(file.readBytes())
            .order(ByteOrder.LITTLE_ENDIAN)

        val header = Header(
            bb.getInt(),
            bb.getInt(),
            bb.getInt(),
            bb.getInt(),
            readArray(20, bb),
            bb.getInt(),
            readArray(24, bb),
            bb.getShort(),
            bb.getShort(),
            bb.getShort(),
            bb.getInt(),
            readString(8, bb),
            bb.getShort(),
            bb.getShort(),
            bb.getInt(),
            bb.getInt(),
            readString(16, bb),
            readArray(16, bb),
            readString(16, bb),
            readArray(16, bb),
            readString(64, bb),
            readString(64, bb),
            readArray(64, bb),
            readString(64, bb),
            readArray(64, bb),
            readArray(1024, bb),
            bb.getInt(),
            readArray(2, bb),
            readString(64, bb),
            readString(64, bb),
            readArray(126, bb)
        )

        return header
    }

    private fun readArray(size: Int, bb: ByteBuffer): ByteArray {
        val tmp = ByteArray(size)
        bb.get(tmp)
        return tmp
    }

    private fun readString(size: Int, bb: ByteBuffer): String {
        val tmp = ByteArray(size)
        bb.get(tmp)
        return String(tmp, StandardCharsets.UTF_8).replace(0.toChar().toString(), "")
    }
}
