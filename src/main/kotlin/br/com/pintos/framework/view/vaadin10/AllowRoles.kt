package br.com.pintos.framework.view.vaadin10


@Target(AnnotationTarget.CLASS)
annotation class AllowRoles(vararg val roles: String)

@Target(AnnotationTarget.CLASS)
annotation class AllowAll

@Target(AnnotationTarget.CLASS)
annotation class AllowAllUsers