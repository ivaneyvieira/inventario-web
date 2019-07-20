package br.com.pintos.framework.view

import br.com.pintos.framework.view.vaadin10.LoggedInUserResolver
import br.com.pintos.framework.view.vaadin10.Session
import br.com.pintos.framework.view.vaadin10.User
import br.com.pintos.framework.view.vaadin10.VaadinOnKotlin
import br.com.pintos.framework.view.vaadin10.loggedInUserResolver
import br.com.pintos.framework.view.vaadin10.loginManager
import org.slf4j.LoggerFactory
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener

//@WebListener
class Bootstrap(val initDatabase: DatabaseInit): ServletContextListener {
  override fun contextInitialized(sce: ServletContextEvent?) {
    log.info("Starting up")
    initDatabase.config()
    Session.loginManager.listaUser = initDatabase.listaUser.toMutableSet()
    VaadinOnKotlin.loggedInUserResolver = object: LoggedInUserResolver {
      override fun isLoggedIn(): Boolean = Session.loginManager.isLoggedIn
      override fun getCurrentUserRoles(): Set<String> = Session.loginManager.getCurrentUserRoles()
    }

    log.info("Initialization complete")
  }

  override fun contextDestroyed(sce: ServletContextEvent?) {
    log.info("Shutting down")
    log.info("Destroying VaadinOnKotlin")
    VaadinOnKotlin.destroy()
    log.info("Shutdown complete")
  }

  companion object {
    private val log = LoggerFactory.getLogger(Bootstrap::class.java)
  }
}

interface DatabaseInit {
  val listaUser: Set<User>

  fun config()
}

