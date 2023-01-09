package io.cloudflight.testresources.springboot.minio

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.stereotype.Service

@SpringBootApplication
class Application

@Service
class StorageService(@Value("\${minio.url}") val url: String) {


}