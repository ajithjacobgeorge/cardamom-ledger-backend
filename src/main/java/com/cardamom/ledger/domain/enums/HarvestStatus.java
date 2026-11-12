package com.cardamom.ledger.domain.enums;

/**
 * Lifecycle states of a harvested cardamom batch:
 * - RAW: Green pods freshly harvested from the field, awaiting curing.
 * - DRYING: Pods currently inside the bhatti/curing chamber undergoing thermal dehydration.
 * - STORED: Cured pods graded, packed in polythene-lined gunny bags, kept in dry storage.
 * - SOLD: Batch fully realized and sold via auction house or direct trade.
 */
public enum HarvestStatus {
    RAW,
    DRYING,
    STORED,
    SOLD
}
