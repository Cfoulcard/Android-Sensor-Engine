package com.christianfoulcard.android.androidsensorengine

import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, (2 + 2).toLong())
    }
}

class Point {
    var x: Double? = 0.5
    var y: Double? = 0.9
}

interface PointInterface {
    fun getX(): Double
    fun getY(): Double
}

class CleanPoint(): PointInterface {

    @Test
    fun setVariablesFromInterface() {
        getX()
        getY()
        Assert.assertNotNull(getX())
        Assert.assertEquals(0.6, getX(), 0.1)
        Assert.assertEquals(1.5, getY(), 0.0)
    }

    @Test
    fun getPlainPoint() {
        val x: Double? = Point().x
        x?.let { Assert.assertEquals(0.5, it, 0.0) }
    }

    override fun getX(): Double {
        return 0.7
    }

    override fun getY(): Double {
        return 1.5
    }

    data class Sword(
        val name: String,
        val type: String,
        val length: Int,
        val bossDrop: Boolean
    )

}