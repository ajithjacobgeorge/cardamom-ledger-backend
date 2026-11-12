package com.cardamom.ledger.domain.enums;

/**
 * Standard cardamom plantation operational and capital expense categories:
 * - LABOR: Shade lopping, weeding, trashing, plucking daily wages.
 * - FERTILIZER: NPK mixtures, neem cake, cow dung compost, dolomite/lime.
 * - PEST_CONTROL: Thrips spray, borer control, fungicide (Bordeaux mixture for Azhukal/rot).
 * - IRRIGATION: Drip line maintenance, pump diesel/electricity, sprinkler sets.
 * - DRYING_FUEL: Firewood, biomass pellets, or diesel for bhatti curing kilns.
 * - TRANSPORT: Green capsule hauling to drying facility, cured produce to auction centers.
 * - MISC: Packing materials, hessian sacks, machinery servicing, estate permits.
 */
public enum ExpenseCategory {
    LABOR,
    FERTILIZER,
    PEST_CONTROL,
    IRRIGATION,
    DRYING_FUEL,
    TRANSPORT,
    MISC
}
