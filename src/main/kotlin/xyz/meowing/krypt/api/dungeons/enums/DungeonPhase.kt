package xyz.meowing.krypt.api.dungeons.enums

object DungeonPhase {
    enum class F7 {
        P1,
        P2,
        P3,
        P4,
        P5
        ;
    }

    enum class P3 {
        S1,
        S2,
        S3,
        S4
        ;

        val number: Int
            get() = ordinal + 1
    }
}