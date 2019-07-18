package br.com.pintos.framework.model


import io.ebean.annotation.Platform
import io.ebean.dbmigration.DbMigration
import java.io.IOException

object MainDbMigration {
  @Throws(IOException::class)
  @JvmStatic
  fun main() {
    // System.setProperty("ddl.migration.generate", "true")
    val home = System.getenv("HOME")
    val fileName = System.getenv("EBEAN_PROPS")
                   ?: "$home/ebean.properties"
    System.setProperty("ebean.props.file", fileName)
    System.setProperty("ddl.migration.name", "support end dating")
    //System.setProperty("ddl.migration.version", "1.42")
    //System.setProperty("ddl.migration.pendingDropsFor", "1.39")
    val migration = DbMigration.create()
    migration.setStrictMode(false)
    migration.setPlatform(Platform.MYSQL)
    System.setProperty("disableTestProperties", "true")
    migration.generateMigration()

    System.out.println("done")
  }
}
