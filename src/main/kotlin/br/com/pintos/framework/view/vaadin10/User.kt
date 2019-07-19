package br.com.pintos.framework.view.vaadin10

import com.vaadin.flow.component.UI
import java.io.Serializable

data class User(var id: Long? = null,
                var username: String = "",
                override var hashedPassword: String = "",
                var roles: String = ""): HasPassword

class LoginManager: Serializable {
  var user: User? = null
    private set
  var findByUsername: (String) -> User? = {null}
  val isLoggedIn: Boolean get() = user != null

  fun signIn(username: String, password: String): Boolean {
    val user = findByUsername(username) ?: return false
    if(!user.passwordMatches(password)) return false
    login(user)
    return true
  }

  private fun login(user: User) {
    check(this.user == null) {"An user is already logged in"}
    this.user = user
    UI.getCurrent()
      .page.reload()
  }

  fun logout() {
    Session.current.close()
    // The UI is recreated by the page reload, and since there is no user in the session (since it has been cleared),
    // the UI will show the LoginView.
    UI.getCurrent()
      .navigate("")
    UI.getCurrent()
      .page.reload()
  }

  fun getCurrentUserRoles(): Set<String> {
    val roles = user?.roles ?: return setOf()
    return roles.split(",")
      .toSet()
  }

  fun isUserInRole(role: String): Boolean = getCurrentUserRoles().contains(role)

  fun isAdmin(): Boolean = isUserInRole("admin")
}

val Session.loginManager: LoginManager get() = getOrPut {LoginManager()}