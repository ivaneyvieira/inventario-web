package br.com.pintos.framework.view.vaadin10

import com.vaadin.flow.component.UI
import java.io.Serializable

data class User(val id: Long,
                val username: String,
                val password: String,
                val roles: Set<String>)

class LoginManager: Serializable {
  var user: User? = null
    private set
  var listaUser: MutableSet<User> = mutableSetOf()
    set(value) {
      if(listaUser.isEmpty())
        listaUser.addAll(value)
    }
  val isLoggedIn: Boolean get() = user != null

  fun signIn(username: String, password: String): Boolean {
    val user = findByUsername(username) ?: return false
    if(user.password != password) return false
    login(user)
    return true
  }

  fun findByUsername(username: String): User? {
    return listaUser.firstOrNull {it.username == username}
  }

  private fun login(user: User) {
    check(this.user == null) {"An user is already logged in"}
    this.user = user
    UI.getCurrent()
      .page.reload()
  }

  fun logout() {
    Session.current.close()
    UI.getCurrent()
      .navigate("")
    UI.getCurrent()
      .page.reload()
  }

  fun getCurrentUserRoles(): Set<String> {
    return user?.roles ?: emptySet()
  }

  fun isUserInRole(role: String): Boolean = getCurrentUserRoles().contains(role)

  fun isAdmin(): Boolean = isUserInRole("admin")
}

val Session.loginManager: LoginManager get() = getOrPut {LoginManager()}