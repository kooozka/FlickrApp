package edu.pwr.kozanecki.flickrapp

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
class PhotosViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchPhotos success sets isLoading false and updates photos`() = runTest {
        val flickrApi = mockk<FlickrApi>()
        val mockedResponse = createMockFlickrResponse()
        coEvery { flickrApi.getPublicPhotos() } returns mockedResponse

        val viewModel = PhotosViewModel(flickrApi)

        advanceUntilIdle()

        assertFalse(viewModel.isLoading.value)
        assertNotNull(viewModel.photos.value)
        assert(viewModel.photos.value == mockedResponse)
    }

    @Test
    fun `fetchPhotos failure sets isLoading false and keeps photos null`() = runTest {
        val flickrApi = mockk<FlickrApi>()
        coEvery { flickrApi.getPublicPhotos() } throws Exception()

        val viewModel = PhotosViewModel(flickrApi)

        advanceUntilIdle()

        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.photos.value)
    }

    private fun createMockFlickrResponse(): FlickrResponse {
        val media1 = Media(m = "https://abc.jpg")
        val photo1 = Photo(
            title = "Dump Photo 1",
            link = "https://dp1",
            media = media1,
            date_taken = "2020.02.20",
            description = "Dump photo",
            published = "2020.12.12",
            author = "Dump author#1",
            author_id = "123",
            tags = "123"
        )
        val media2 = Media(m = "https://bcd.jpg")
        val photo2 = Photo(
            title = "Dump Photo 2",
            link = "https://dp1",
            media = media2,
            date_taken = "2020.02.20",
            description = "Dump photo",
            published = "2020.12.12",
            author = "Dump author#2",
            author_id = "123",
            tags = "123"
        )
        return FlickrResponse(
            title = "Dump response title",
            link = "https://dumpResponse",
            description = "Dump description",
            modified = "2024.01.03",
            generator = "Dump generator",
            items = listOf(photo1, photo2)
        )
    }
}