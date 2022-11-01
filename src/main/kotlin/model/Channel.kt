package model

data class Channel(
    val prev_addr: Int,
    val next_addr: Int,
    val data_addr: Int,
    val data_count: Int,
    val unknown1: Short,
    val datatype_type: Short,
    val datatype_size: Short,
    val datatype: Datatype,
    val sample_rate: Short,
    val offset: Short,
    val mul: Short,
    val scale: Short,
    val dec_places: Short,
    val name: String,
    val short_name: String,
    val unit: String,
    val unknown2: ByteArray

) {
    val data = arrayListOf<Int>()
}
