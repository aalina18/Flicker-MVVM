package com.coding.flickertask

import com.coding.flickertask.util.buildPhotoUri
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals

class UtilsTest {

    @Test
    fun Build_Photo_Uri_Returns_Right_Url() {
        val expected = "https://farm1.static.flickr.com/10/101_aalina.jpg"
        // when
        val farm = 1
        val server = "10"
        val id = "101"
        val secret = "aalina"
        val result = buildPhotoUri(farm, server, id, secret)
        // then
        assertEquals(expected, result)
    }


    @Test
    fun Build_Photo_Uri_Returns_Wrong_Url() {
        val expected = "https://farm1.static.flickr.com/10/101_aalina.jpg"
        // when
        val farm = 1
        val server = "10"
        val id = "101"
        val secret = "hurain"
        val result = buildPhotoUri(farm, server, id, secret)
        // then
        assertNotEquals(expected, result)
    }
}