package br.com.pintos.framework.view.vaadin10

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.Key.*
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.StyleSheet
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route("Login")
@PageTitle("Login")
@StyleSheet("css/shared-styles.css")
@AllowAll
class LoginScreen : KComposite() {

  private lateinit var username: TextField
  private lateinit var password: PasswordField

  private val root = ui {
    flexLayout {
      setSizeFull(); className = "login-screen"

      verticalLayout {
        // login info
        className = "login-information"
        h1("Login Information")
        span("""Log in as "admin"/"admin" to have full access. Log in with "user"/"user" to have read-only access.""")
      }

      flexLayout {
        setSizeFull()
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        alignItems = FlexComponent.Alignment.CENTER

        formLayout {
          width = "310px"
          formItem("Username") {
            username = textField {
              width = "15em"
              value = "admin"
              setId("username")
              focus()
            }
          }
          html("<br/>")
          formItem("Password") {
            password = passwordField {
              width = "15em"
              setId("password")
            }
          }
          html("<br/>")
          horizontalLayout {
            // buttons
            button("Login") {
              onLeftClick { login() }
              addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY)
            }
            button("Forgot password?") {
              onLeftClick { showNotification(Notification("Hint: try any password")) }
              addThemeVariants(ButtonVariant.LUMO_TERTIARY)
            }
          }
          addShortcut(ENTER.shortcut) { login() }
        }
      }
    }
  }

  private fun login() {
    if (Session.loginManager.signIn(username.value, password.value)) {
      ui.get().navigate("")
    } else {
      showNotification(Notification("Login failed. Please check your username and password and try again."))
      username.focus()
    }
  }

  private fun showNotification(notification: Notification) {
    // keep the notification visible a little while after moving the
    // mouse, or until clicked
    notification.duration = 2000
    notification.open()
  }
}

val Key.shortcut: KeyShortcut get() = KeyShortcut(this)