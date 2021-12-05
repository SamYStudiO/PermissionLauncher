package net.samystudio.permissionlauncher

import android.os.Build
import org.junit.Assert.*
import org.junit.Test
import java.lang.reflect.Field
import java.lang.reflect.Modifier

class StringKtTest {

    @Test
    fun `permission is useless above 10`() {
        setFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), 11)
        val permission = "permission" maxSdkVersion 10
        assertTrue(permission.isUselessPermission())
    }

    @Test
    fun `permission is useful until 10`() {
        setFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), 10)
        val permission = "permission" maxSdkVersion 10
        assertFalse(permission.isUselessPermission())
    }

    @Test
    fun `permission is useful below 10`() {
        setFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), 9)
        val permission = "permission" maxSdkVersion 10
        assertFalse(permission.isUselessPermission())
    }

    @Test
    fun `normalize permission`() {
        setFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), 11)
        val permission = "permission" maxSdkVersion 10
        assertEquals("permission", permission.normalizePermission())
    }

    @Throws(Exception::class)
    private fun setFinalStatic(field: Field, newValue: Any) {
        field.isAccessible = true

        val modifiersField = Field::class.java.getDeclaredField("modifiers")
        modifiersField.isAccessible = true
        modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())

        field.set(null, newValue)
    }
}
