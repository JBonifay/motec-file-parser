import java.io.BufferedWriter
import java.io.OutputStream // ktlint-disable filename

fun OutputStream.writeCsv(channels: List<Channel>) {
    val writer = bufferedWriter()

    writeHeader(channels, writer)

    writeData(channels, writer)

    writer.flush()
}

private fun writeHeader(channels: List<Channel>, writer: BufferedWriter) {
    val sb = StringBuilder()
    sb.append("Sample,")
    channels.forEachIndexed { index, it ->
        sb.append(it.name)
        if (index != channels.size - 1) {
            sb.append(",")
        }
    }
    writer.write(sb.toString())
    writer.newLine()
}

private fun writeData(channels: List<Channel>, writer: BufferedWriter) {
    val sb = StringBuilder()
    val maxDataCount = channels.maxBy { it.data_count }.data_count

    for (i in 0 until maxDataCount) {
        sb.append(i + 1)
        sb.append(",")

        for ((index, chann) in channels.withIndex()) {
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
