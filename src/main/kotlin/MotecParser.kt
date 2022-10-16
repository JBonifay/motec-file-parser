import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class MotecParser() {

    lateinit var header: Header
    lateinit var event: Event
    lateinit var venue: Venue
    lateinit var vehicle: Vehicle

    fun parseFile(fileName: String) {
        val buffer = createBuffer(fileName)

        header = parseHeader(buffer)
        event = parseEvent(buffer)
        venue = parseVenue(buffer)
        vehicle = parseVehicle(buffer)
    }

    private fun createBuffer(fileName: String): ByteBuffer {
        val file = openFile(fileName)
        return ByteBuffer.wrap(file.readBytes()).order(ByteOrder.LITTLE_ENDIAN)
    }

    private fun parseHeader(buffer: ByteBuffer) = Header(
        buffer.getInt(),
        buffer.getInt(),
        buffer.getInt(),
        buffer.getInt(),
        readBytes(20, buffer),
        buffer.getInt(),
        readBytes(24, buffer),
        buffer.getShort(),
        buffer.getShort(),
        buffer.getShort(),
        buffer.getInt(),
        readString(8, buffer),
        buffer.getShort(),
        buffer.getShort(),
        buffer.getInt(),
        buffer.getInt(),
        readString(16, buffer),
        readBytes(16, buffer),
        readString(16, buffer),
        readBytes(16, buffer),
        readString(64, buffer),
        readString(64, buffer),
        readBytes(64, buffer),
        readString(64, buffer),
        readBytes(64, buffer),
        readBytes(1024, buffer),
        buffer.getInt(),
        readBytes(2, buffer),
        readString(64, buffer),
        readString(64, buffer),
        readBytes(126, buffer)
    )

    private fun parseEvent(buffer: ByteBuffer): Event {
        if (!::header.isInitialized) {
            parseHeader(buffer)
        }

        return Event(
            readString(64, buffer),
            readString(64, buffer),
            readString(1024, buffer),
            buffer.getShort()
        )
    }

    private fun parseVenue(buffer: ByteBuffer): Venue {
        if (event.venue_addr == 0.toShort()) throw IllegalStateException()
        buffer.position(event.venue_addr.toInt())
        return Venue(
            readString(64, buffer),
            readBytes(1034, buffer),
            buffer.getShort()
        )
    }

    private fun parseVehicle(buffer: ByteBuffer): Vehicle {
        buffer.position(venue.vehicle_addr.toInt())
        return Vehicle(
            readString(64, buffer),
            readBytes(128, buffer),
            buffer.getInt(),
            readString(32, buffer),
            readString(32, buffer)
        )
    }

    private fun openFile(fileName: String): File {
        val url = this::class.java.getResource(fileName)
        val file = File(
            url?.toURI() ?: throw IllegalStateException("Could not open file!")
        )
        return file
    }

    private fun readBytes(size: Int, bb: ByteBuffer): ByteArray {
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
