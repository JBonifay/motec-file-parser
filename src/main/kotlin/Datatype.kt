enum class Datatype {
    BEACON16, BEACON32, I16, I32, F16, F32, INVALID;

    companion object {
        fun fromTypeAndSize(type: Short, size: Short): Datatype {
            return when {
                type.toInt() == 0 ->
                    if (size.toInt() == 2) BEACON16
                    else if (size.toInt() == 4) BEACON32
                    else INVALID

                type.toInt() == 3 -> if (size.toInt() == 2) I16
                else if (size.toInt() == 4) I32
                else INVALID

                type.toInt() == 5 -> if (size.toInt() == 2) I16
                else if (size.toInt() == 4) I32
                else INVALID

                type.toInt() == 7 -> if (size.toInt() == 2) F16
                else if (size.toInt() == 4) F32
                else INVALID

                else -> {
                    INVALID
                }
            }
        }
    }
}
