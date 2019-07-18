package br.com.pintos.framework.view.vaadin10

interface LoggedInUserResolver {

  fun isLoggedIn(): Boolean

  fun getCurrentUserRoles(): Set<String>

  fun hasRole(role: String): Boolean = getCurrentUserRoles().contains(role)

  fun checkPermissionsOnClass(viewClass: Class<*>) {
    val annotationClasses = listOf(AllowRoles::class.java, AllowAll::class.java, AllowAllUsers::class.java)
    val annotations: List<Annotation> = annotationClasses.mapNotNull { viewClass.getAnnotation(it) }
    if (annotations.isEmpty()) {
      throw AccessRejectedException("The view ${viewClass.simpleName} is missing one of the ${annotationClasses.map { it.simpleName }} annotation", viewClass, setOf())
    }
    require(annotations.size == 1) {
      "The view ${viewClass.simpleName} contains multiple security annotations which is illegal: $annotations"
    }
    val annotation = annotations[0]
    when(annotation) {
      is AllowAll -> {} // okay
      is AllowAllUsers -> if (!isLoggedIn()) throw AccessRejectedException("Cannot access ${viewClass.simpleName}, you're not logged in", viewClass, setOf())
      is AllowRoles -> {
        if (!isLoggedIn()) {
          throw AccessRejectedException("Cannot access ${viewClass.simpleName}, you're not logged in", viewClass, setOf())
        }
        val requiredRoles: Set<String> = annotation.roles.toSet()
        if (requiredRoles.isEmpty()) {
          throw AccessRejectedException("Cannot access ${viewClass.simpleName}, nobody can access it", viewClass, setOf())
        }
        val currentUserRoles: Set<String> = getCurrentUserRoles()
        if (requiredRoles.intersect(currentUserRoles).isEmpty()) {
          throw AccessRejectedException("Can not access ${viewClass.simpleName}, you are not ${requiredRoles.joinToString(" or ")}", viewClass, requiredRoles)
        }
      }
    }
  }
}

private var resolver: LoggedInUserResolver? = null

var VaadinOnKotlin.loggedInUserResolver: LoggedInUserResolver?
  get() = resolver
  set(value) { resolver = value }