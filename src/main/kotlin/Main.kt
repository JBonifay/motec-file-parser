class Main

fun main() {
    val parser = MotecParser()
    parser.parseFile("sample.ld")

    parser.channels.forEach {
        println(it)
    }
}
