import java.io.File

fun main() {
    val parser = MotecParser("sample.ld")
    val csv = File("Result.csv").outputStream().writeCsv(parser.channels)
}
