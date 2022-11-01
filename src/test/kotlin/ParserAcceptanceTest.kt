import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ParserAcceptanceTest {

    @Test
    fun `should load file and create channels`() {
        val fileName = "sample.ld"
        val parser = MotecParser(fileName)

        val channelThrottle = parser.getChannel(ChannelName.THROTTLE)
        val channelBrake = parser.getChannel(ChannelName.BRAKE)
        val channelSpeed = parser.getChannel(ChannelName.SPEED)
        val channelGear = parser.getChannel(ChannelName.GEAR)
        val channelLapDistPct = parser.getChannel(ChannelName.LAP_DIST_PCT)

        assertNotNull(channelThrottle)
        assertEquals(6420, channelThrottle.data.size)

        assertNotNull(channelBrake)
        assertEquals(6420, channelBrake.data.size)

        assertNotNull(channelSpeed)
        assertEquals(6420, channelSpeed.data.size)

        assertNotNull(channelGear)
        assertEquals(6420, channelGear.data.size)

        assertNotNull(channelLapDistPct)
        assertEquals(6420, channelLapDistPct.data.size)
    }
}
