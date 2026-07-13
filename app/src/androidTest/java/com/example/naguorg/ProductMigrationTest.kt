package com.example.naguorg

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.naguorg.feature_products.data.ProductDatabase
import com.example.naguorg.feature_products.data.ProductDatabase.Companion.MIGRATION_2_3
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductMigrationTest {

    private val TEST_DB = "product-migration-test"

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        ProductDatabase::class.java
    )

    @Test
    fun migrate2To3() {

        // Create version 2 database
        helper.createDatabase(TEST_DB, 2).apply {

            execSQL("""
                INSERT INTO products
                (id, name, disc, image, category, MRP, DP, quantity, description)
                VALUES
                (1,'Soap',10,'soap.png','Health',100,90,20,'Natural soap')
            """.trimIndent())

            close()
        }

        // Run migration
        val db = helper.runMigrationsAndValidate(
            TEST_DB,
            3,
            true,
            MIGRATION_2_3
        )

        // Verify old data still exists
        val cursor = db.query("SELECT * FROM products WHERE id = 1")

        assertTrue(cursor.moveToFirst())

        assertEquals(
            "Soap",
            cursor.getString(cursor.getColumnIndexOrThrow("name"))
        )

        assertEquals(
            "Health",
            cursor.getString(cursor.getColumnIndexOrThrow("category"))
        )

        assertEquals(
            100,
            cursor.getInt(cursor.getColumnIndexOrThrow("MRP"))
        )

        cursor.close()

        // Verify index exists
        val indexCursor = db.query(
            "PRAGMA index_list(products)"
        )

        var found = false

        while (indexCursor.moveToNext()) {

            val indexName =
                indexCursor.getString(indexCursor.getColumnIndexOrThrow("name"))

            if (indexName == "index_products_category") {
                found = true
                break
            }
        }

        assertTrue(found)

        indexCursor.close()
    }
}