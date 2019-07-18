package br.com.pintos.framework.view.vaadin10

open class AccessRejectedException(message: String, val viewClass: Class<*>, val missingRoles: Set<String>):
  Exception(message)