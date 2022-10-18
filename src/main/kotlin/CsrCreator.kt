import java.io.BufferedWriter
import java.io.OutputStream // ktlint-disable filename

fun OutputStream.writeCsv(channels: List<Channel>) {
    val writer = bufferedWriter()
    val sb = StringBuilder()

    writeHeader(channels, sb, writer)

    writer.flush()
}

private fun writeHeader(
    channels: List<Channel>,
    sb: StringBuilder,
    writer: BufferedWriter
) {
    sb.append("Sample,")
    channels.forEach {
        sb.append(it.name)
        sb.append(",")
    }
    sb.deleteAt(sb.length - 1)
    writer.write(sb.toString())
    writer.newLine()
}
