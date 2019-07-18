package br.com.pintos.framework.util

import com.vaadin.flow.server.StreamResource
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

fun ByteArray.makeResource(): StreamResource {
  val nome = md5()
  val factory: () -> InputStream = {
    ByteArrayInputStream(this)
  }
  val streamResource = StreamResource("$nome.jpg", factory)
  streamResource.cacheTime = 1000
  return streamResource
}

fun ByteArray.md5(): String {
  val md5Digest = MessageDigest.getInstance("MD5")
  val bytes = md5Digest.digest(this)
  return DatatypeConverter.printHexBinary(bytes)
}