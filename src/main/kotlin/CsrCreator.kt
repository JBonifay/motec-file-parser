import java.io.BufferedWriter
import java.io.OutputStream

fun OutputStream.writeCsv(channels: LinkedHashMap<String, Channel>) {
    val writer = bufferedWriter()

    writeHeader(channels, writer)

    writeData(channels, writer)

    writer.flush()
}

private fun writeHeader(channels: LinkedHashMap<String, Channel>, writer: BufferedWriter) {
    val sb = StringBuilder()
    sb.append("Sample,")
    channels.values.forEachIndexed { index, it ->
        sb.append(it.name)
        if (index != channels.size - 1) {
            sb.append(",")
        }
    }
    writer.write(sb.toString())
    writer.newLine()
}

private fun writeData(channels: LinkedHashMap<String, Channel>, writer: BufferedWriter) {
    val sb = StringBuilder()
    val maxDataCount = channels.values.maxBy { it.data_count }.data_count

    for (i in 0 until maxDataCount) {
        sb.append(i + 1)
        sb.append(",")

        for ((index, chann) in channels.values.withIndex()) {
            sb.append(chann.data.getOrNull(i) ?: "")
            if (index != channels.size - 1) {
                sb.append(",")
            }
        }

        if (i != maxDataCount - 1) {
            sb.append("\n")
        }
    }

    writer.write(sb.toString())
    writer.newLine()
}
