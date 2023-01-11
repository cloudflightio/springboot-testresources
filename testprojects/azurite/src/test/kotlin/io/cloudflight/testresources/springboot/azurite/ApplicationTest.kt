package io.cloudflight.testresources.springboot.azurite

import com.azure.storage.blob.BlobServiceClientBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class ApplicationTest(
    @Autowired private val blobServiceClientBuilder: BlobServiceClientBuilder
) {

    @Test
    fun accountName() {
        val blobServiceClient = blobServiceClientBuilder.buildClient()
        assertThat(blobServiceClient.accountName).isEqualTo("devstoreaccount1")
    }

    @Test
    fun `do some basic operations to show that azurite is running and working correctly`() {
        val blobServiceClient = blobServiceClientBuilder.buildClient()
        val containersBefore = blobServiceClient.listBlobContainers().stream().count()
        val container = blobServiceClient.createBlobContainer(UUID.randomUUID().toString())
        assertThat(container.listBlobs().stream()).isEmpty()
        assertThat(blobServiceClient.listBlobContainers().stream().count()).isEqualTo(containersBefore + 1)
        container.delete()
        assertThat(blobServiceClient.listBlobContainers().stream().count()).isEqualTo(containersBefore)
    }
}