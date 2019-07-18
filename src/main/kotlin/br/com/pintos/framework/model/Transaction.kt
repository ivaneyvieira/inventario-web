package br.com.pintos.framework.model

import io.ebean.Ebean
import io.ebean.Query
import io.ebean.SqlQuery
import io.ebean.SqlUpdate
import javax.persistence.RollbackException

object Transaction {
  private fun inTransaction(): Boolean {
    return Ebean.currentTransaction() != null
  }

  @Throws(Exception::class)
  fun execTransacao(lambda: () -> Unit) {
    try {
      Ebean.beginTransaction()
      lambda()
      Ebean.commitTransaction()
    } catch (e: RollbackException) {
      Ebean.rollbackTransaction()
      throw e
    } catch (exception: Exception) {
      Ebean.rollbackTransaction()
      throw exception
    } catch (error: Error) {
      Ebean.rollbackTransaction()
      throw Exception(error)
    } catch (error: Throwable) {
      Ebean.rollbackTransaction()
      throw Exception(error)
    } finally {
      Ebean.endTransaction()
    }
  }

  fun commit() {
    if (inTransaction()) Ebean.commitTransaction()
    Ebean.beginTransaction()
  }

  fun variable(name: String, value: String?) {
    Ebean.currentTransaction()?.connection?.let { con ->
      val stmt = con.createStatement()
      val sql = "SET @$name := $value;"
      stmt.executeQuery(sql)
    }
  }

  fun rollback() {
    if (inTransaction()) Ebean.rollbackTransaction()
    Ebean.beginTransaction()
  }

  fun createSqlUpdate(sql: String): SqlUpdate? {
    return Ebean.createSqlUpdate(sql)
  }

  fun <T> find(javaClass: Class<T>): Query<T>? {
    return Ebean.find(javaClass)
  }

  fun createSqlQuery(sql: String): SqlQuery? {
    return Ebean.createSqlQuery(sql)
  }
}
