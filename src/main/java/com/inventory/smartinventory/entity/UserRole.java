package com.inventory.smartinventory.entity;

/**
 * Roles available in the system.
 *
 * ADMIN ? full access: manage products, stock in/out, view history, delete records
 * STAFF ? limited access: stock in/out and read-only views (no delete)
 */
public enum UserRole {
    ADMIN,
    STAFF
}
