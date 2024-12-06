package org.fmaghi.badgesimulator

import kotlin.random.Random

data class SimulationParameters(
    val nTotalBadges: Int,
    val nStickers: Int,
    val nUnpacksPerOrder: Int,
    val costPerBadge: Double,
    val pricePerOrder: Double,
    val nOrdersPerDay: Int,
    val nSimulations: Int
) {
    override fun toString(): String = """
            徽章总数=$nTotalBadges
            贴纸数=$nStickers
            拍一拆=$nUnpacksPerOrder
            模拟次数=$nSimulations
            单个徽章成本价=$costPerBadge
            每单售价=$pricePerOrder
            每日订单数=$nOrdersPerDay
        """.trimIndent()
}

data class SimulationResults(
    val unpackPerOrder: Double,
    val costPerOrder: Double,
    val profitPerOrder: Double
) {
    override fun toString(): String = """
            每单拆包数=$unpackPerOrder
            每单平均成本=$costPerOrder
            每单平均利润=$profitPerOrder
    """.trimIndent()
}


fun runSimulation(parameters: SimulationParameters): SimulationResults {

    val unpackCountList = mutableListOf<Int>()

    for (i in 1..parameters.nSimulations) {
        val rewardSticker = Random.nextInt(parameters.nStickers)

        var badges = createShuffledBox(parameters.nStickers, parameters.nTotalBadges)

        // take orders
        for (orderNo in 1..parameters.nOrdersPerDay) {
            var remainingUnpacks = parameters.nUnpacksPerOrder
            val unpackedStickers = mutableListOf<Int>()
            var unpackCount = 0

            // each order
            var iBadge = 0
            do {
                val newBadge = badges[iBadge]
                ++unpackCount
                --remainingUnpacks

                if (newBadge == rewardSticker)
                    ++remainingUnpacks

                // check duplicates
                if (unpackedStickers.remove(newBadge)) {
                    ++remainingUnpacks
                } else {
                    unpackedStickers.add(newBadge)
                }

                ++iBadge
                // create a new box if the current one is empty
                if (iBadge >= badges.size - 1) {
                    badges = createShuffledBox(parameters.nStickers, parameters.nTotalBadges)
                    iBadge = badges.size - 1
                }
            } while (remainingUnpacks > 0)

            unpackCountList.add(unpackCount)
        }

    }

    val unpackPerOrder = unpackCountList.average()
    val costPerOrder = parameters.costPerBadge * unpackPerOrder
    val profitPerOrder = parameters.pricePerOrder - costPerOrder

    return SimulationResults(unpackPerOrder, costPerOrder, profitPerOrder)
}

fun createShuffledBox(nStickers: Int, nTotalBadges: Int) : MutableList<Int> {
    val box = mutableListOf<Int>()
    val stickersPerType = nTotalBadges / nStickers
    for (stickerType in 0..<nStickers) {
        for (j in 1..stickersPerType) {

            box.add(stickerType)
        }
    }
    box.shuffle()
    return box
}