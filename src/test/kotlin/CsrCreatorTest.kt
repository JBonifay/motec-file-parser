import Datatype.INVALID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class CsrCreatorTest {

    @Test
    fun shouldInsertHeaderWithChannelNames() {
        val channels = listOf(
            createFakeChannel("Brake"),
            createFakeChannel("AirTemp"),
            createFakeChannel("AirDensity")
        )
        val csvResult = ByteArrayOutputStream().apply { writeCsv(channels) }.toByteArray().let { String(it) }

        assertEquals(
            "Sample,Brake,AirTemp,AirDensity\n\n",
            csvResult
        )
    }

    @Test
    fun shouldInsertValueForOneChannel() {
        val channels = listOf(
            createFakeChannel("Brake", 10)
        )

        val csvResult = ByteArrayOutputStream().apply { writeCsv(channels) }.toByteArray().let { String(it) }

        assertEquals(
            "Sample,Brake\n1,10\n",
            csvResult
        )
    }

    @Test
    fun shouldInsertOneValueForTwoChannels() {
        val channels = listOf(
            createFakeChannel("Brake", 10),
            createFakeChannel("Gaz", 20)
        )

        val csvResult = ByteArrayOutputStream().apply { writeCsv(channels) }.toByteArray().let { String(it) }

        assertEquals(
            "Sample,Brake,Gaz\n1,10,20\n",
            csvResult
        )
    }

    @Test
    fun shouldInsertThreeValueForTwoChannels() {
        val channels = listOf(
            createFakeChannel("Brake", 10, 20, 30),
            createFakeChannel("Gaz", 40, 50, 60)
        )

        val csvResult = ByteArrayOutputStream().apply { writeCsv(channels) }.toByteArray().let { String(it) }

        assertEquals(
            """Sample,Brake,Gaz
                |1,10,40
                |2,20,50
                |3,30,60
                |
            """.trimMargin(),
            csvResult
        )
    }

    @Test
    fun shouldInsertValueForChannelsWithDataOfDifferentSize() {
        val channels = listOf(
            createFakeChannel("Brake", 10),
            createFakeChannel("Gaz", 40, 50, 60)
        )

        val csvResult = ByteArrayOutputStream().apply { writeCsv(channels) }.toByteArray().let { String(it) }
        assertEquals(
            """Sample,Brake,Gaz
                |1,10,40
                |2,,50
                |3,,60
                |
            """.trimMargin(),
            csvResult
        )
    }

    private fun createFakeChannel(channelName: String, vararg value: Int): Channel {
        return Channel(
            0,
            0,
            0,
            value.size,
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
        ).apply {
            for (v in value) {
                this.data.add(v)
            }
        }
    }
}
