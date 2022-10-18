import Datatype.INVALID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class CsrCreatorTest {

    @Test
    fun write_csv() {
        val channels = listOf(
            createFakeChannel("Brake"),
            createFakeChannel("AirTemp"),
            createFakeChannel("AirDensity")
        )
        val csv = ByteArrayOutputStream().apply { writeCsv(channels) }.toByteArray().let { String(it) }

        assertEquals(
            "Sample,Brake,AirTemp,AirDensity\n",
            csv
        )
    }

    private fun createFakeChannel(channelName: String): Channel {
        return Channel(
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            INVALID,
            0,
            0,
            0,
            0,
            0,
            channelName,
            "",
            "",
            ByteArray(0)
        )
    }
}
