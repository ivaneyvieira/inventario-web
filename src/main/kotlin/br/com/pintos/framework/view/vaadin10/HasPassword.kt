package br.com.pintos.framework.view.vaadin10

interface HasPassword {
  var hashedPassword: String


  fun passwordMatches(password: String) = PasswordHash.validatePassword(password, hashedPassword)


  fun setPassword(password: String) {
    hashedPassword = PasswordHash.createHash(password)
  }
}