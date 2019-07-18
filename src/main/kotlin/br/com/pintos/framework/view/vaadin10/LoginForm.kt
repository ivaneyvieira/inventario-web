package br.com.pintos.framework.view.vaadin10

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import javax.validation.constraints.NotBlank

class LoginForm(appName: String): VerticalLayout() {
  data class UsernamePassword(@field:NotBlank
                              var username: String = "",
                              @field:NotBlank
                              var password: String = "")

  private val binder = beanValidationBinder<UsernamePassword>()
  lateinit var usernameField: TextField
    private set
  lateinit var passwordField: PasswordField
    private set
  private var loginHandler: (username: String, password: String) -> Unit = {_, _ ->}

  fun onLogin(loginBlock: (username: String, password: String) -> Unit) {
    this.loginHandler = loginBlock
  }

  init {
    width = "500px"; isSpacing = false
    binder.bean = UsernamePassword()

    horizontalLayout {
      width = "100%"
      isSpacing = false
      content {align(between, baseline)}

      h3("Welcome")
      h4(appName)
    }
    horizontalLayout {
      width = "100%"
      usernameField = textField("Username") {
        isExpand = true; minWidth = "0px"
        prefixComponent = Icon(VaadinIcon.USER)
        bind(binder).asRequired()
          .trimmingConverter()
          .bind(UsernamePassword::username)
      }
      passwordField = passwordField("Password") {
        isExpand = true; minWidth = "0px"
        prefixComponent = Icon(VaadinIcon.LOCK)
        bind(binder).asRequired()
          .trimmingConverter()
          .bind(UsernamePassword::password)
      }
      button("Login") {
        setPrimary()
        onLeftClick {login()}
      }
    }
  }

  private fun login() {
    if(binder.validate().isOk) {
      loginHandler(binder.bean.username, binder.bean.password)
    }
  }
}

fun (@VaadinDsl HasComponents).loginForm(appName: String, block: (@VaadinDsl LoginForm).() -> Unit = {}) =
  init(LoginForm(appName), block)
