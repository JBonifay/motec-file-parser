import Datatype.BEACON16
import Datatype.BEACON32
import Datatype.F16
import Datatype.F32
import Datatype.I16
import Datatype.I32
import Datatype.INVALID
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class MotecParser() {

    lateinit var header: Header
    lateinit var event: Event
    lateinit var venue: Venue
    lateinit var vehicle: Vehicle
    lateinit var channels: MutableList<Channel>

    fun parseFile(fileName: String) {
        val buffer = createBuffer(fileName)

        header = parseHeader(buffer)
        event = parseEvent(buffer)
        venue = parseVenue(buffer)
        vehicle = parseVehicle(buffer)
        channels = parseChannels(buffer)
        parseChannelsData(buffer)
    }

    private fun createBuffer(fileName: String): ByteBuffer {
        val file = File(fileName)
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

    private fun parseChannels(buffer: ByteBuffer): MutableList<Channel> {
        val channels = mutableListOf<Channel>()
        var addr = header.channel_meta_ptr
        while (addr != 0) {
            val channel = parseChannel(buffer, addr)
            addr = channel.next_addr
            channels.add(channel)
        }
        return channels
    }

    private fun parseChannel(buffer: ByteBuffer, addr: Int): Channel {
        buffer.position(addr)
        val prev_addr = buffer.getInt()
        val next_addr = buffer.getInt()
        val data_addr = buffer.getInt()
        val data_count = buffer.getInt()
        val unknown1 = buffer.getShort()
        val datatype_type = buffer.getShort()
        val datatype_size = buffer.getShort()
        return Channel(
            prev_addr,
            next_addr,
            data_addr,
            data_count,
            unknown1,
            datatype_type,
            datatype_size,
            Datatype.fromTypeAndSize(datatype_type, datatype_size),
            buffer.getShort(),
            buffer.getShort(),
            buffer.getShort(),
            buffer.getShort(),
            buffer.getShort(),
            readString(32, buffer),
            readString(8, buffer),
            readString(12, buffer),
            readBytes(40, buffer)
        )
    }

    private fun parseChannelsData(buffer: ByteBuffer) {
        for (channel in channels) {
            buffer.position(channel.data_addr)

            for (i in 0 until channel.data_count) {
                when (channel.datatype) {
                    BEACON16, I16 -> channel.data.add(buffer.getShort())
                    BEACON32, I32 -> channel.data.add(buffer.getInt())
                    F16 -> TODO()
                    F32 -> channel.data.add(buffer.getFloat())
                    INVALID -> TODO()
                }
            }
        }
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
