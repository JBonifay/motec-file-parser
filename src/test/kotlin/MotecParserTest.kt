import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class MotecParserTest {

    private val motecParser = MotecParser()
    private val fileName = "sample.ld"

    @Test
    fun shouldFailIfFileDoesNotExist() {
        assertThrows(IllegalStateException::class.java, {
            val file = "fileDoesNotExist.ld"
            motecParser.parseFile(file)
        }, "Could not open file!")
    }

    @Test
    fun shouldNotThrowIfFileWasFound() {
        assertDoesNotThrow {
            motecParser.parseFile(fileName)
        }
    }

    @Test
    fun shouldParseHeader() {
        motecParser.parseFile(fileName)
        val header = motecParser.header

        assertEquals(64, header.ldmarker)
        assertEquals(0, header.unknown1)
        assertEquals(13384, header.channel_meta_ptr)
        assertEquals(54304, header.channel_data_ptr)
        assertEquals(20, header.unknown2.size)
        assertEquals(1762, header.event_ptr)
        assertEquals(24, header.unknown3.size)
        assertEquals(2, header.unknown_const_1)
        assertEquals(16960, header.unknown_const_2)
        assertEquals(15, header.unknown_const_3)
        assertEquals(12007, header.device_serial)
        assertEquals("ADL", header.device_type)
        assertEquals(420, header.device_version)
        assertEquals(128, header.unknown_const_4)
        assertEquals(21627210, header.num_channels)
        assertEquals(65596, header.unknown4)
        assertEquals("14/10/2022", header.date_string)
        assertEquals(16, header.unknown5.size)
        assertEquals("07:26:42", header.time_string)
        assertEquals(16, header.unknown6.size)
        assertEquals("Dani Elgarbay", header.driver)
        assertEquals("lamborghinievogt3", header.vehicleid)
        assertEquals(64, header.unknown7.size)
        assertEquals("barcelona gp", header.venue)
        assertEquals(64, header.unknown8.size)
        assertEquals(1024, header.unknown9.size)
        assertEquals(13764642, header.pro_logging_bytes)
        assertEquals(2, header.unknown10.size)
        assertEquals("Test", header.session)
        assertEquals("22S2-GTSPRINT-Sebring-Lambo-DE.sto", header.short_comment)
        assertEquals(126, header.unknown11.size)
    }

    @Test
    fun shouldParseEvent() {
        motecParser.parseFile(fileName)
        val event = motecParser.event

        kotlin.test.assertEquals("Test", event.name)
        kotlin.test.assertEquals("Test", event.session)
        kotlin.test.assertEquals("\nTime of day: 7:00 pm", event.comment)
        kotlin.test.assertEquals(4918, event.venue_addr)
    }

    @Test
    fun shouldParseVenue() {
        motecParser.parseFile(fileName)
        val venue = motecParser.venue
        assertEquals("barcelona gp", venue.name)
        assertEquals(1034, venue.unknown1.size)
        assertEquals(8020, venue.vehicle_addr)
    }
}
