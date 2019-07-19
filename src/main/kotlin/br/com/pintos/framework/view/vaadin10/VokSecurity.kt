package br.com.pintos.framework.view.vaadin10

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.router.InternalServerError
import com.vaadin.flow.router.Route

object VokSecurity {

  fun checkPermissionsOfView(viewClass: Class<*>) {
    if(viewClass == InternalServerError::class.java) {
      // allow
      return
    }
    require(Component::class.java.isAssignableFrom(viewClass)) {"$viewClass is not a subclass of Component"}
    val route = requireNotNull(
      viewClass.getAnnotation(Route::class.java)) {"The view $viewClass is not annotated with @Route"}
    val user = checkNotNull(
      VaadinOnKotlin.loggedInUserResolver) {"The VaadinOnKotlin.loggedInUserResolver has not been set"}
    user.checkPermissionsOnClass(viewClass)
    if(route.layout != UI::class && route.layout.java.getAnnotation(Route::class.java) != null) {
      // recursively check for permissions on the parent layout
      checkPermissionsOfView(route.layout.java)
    }
  }

  fun install() {
    UI.getCurrent()
      .addBeforeEnterListener {e -> checkPermissionsOfView(e.navigationTarget)}
  }
}
