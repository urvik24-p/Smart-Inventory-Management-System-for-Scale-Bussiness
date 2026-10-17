package com.inventory.smartinventory.entity;

/**
 * Represents the lifecycle status of a single physical unit (serial number).
 *
 * available → unit is in stock and can be sold
 * sold      → unit has been sold (Stock Out)
 * damaged   → unit is defective and cannot be sold
 *
 * Stored in the database as a plain string to match the MySQL ENUM column.
 */
public enum SerialStatus {
    available,
    sold,
    damaged
}
