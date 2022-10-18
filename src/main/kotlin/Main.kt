import java.io.File

fun main() {
    val parser = MotecParser()
    parser.parseFile("sample.ld")
 
    val csv = File("Result.csv").outputStream().writeCsv(parser.channels)
    
}
