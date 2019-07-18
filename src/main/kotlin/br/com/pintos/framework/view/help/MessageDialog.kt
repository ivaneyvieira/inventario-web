package br.com.pintos.framework.view.help

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import org.claspina.confirmdialog.ButtonOption
import org.claspina.confirmdialog.ConfirmDialog

object MessageDialog {
  val ui = UI.getCurrent()!!
  fun info(caption: String = "Informação", message: String) {
    ConfirmDialog.createInfo()
      .withCaption(caption)
      .withMessage(message)
      .withCloseButton(ButtonOption.caption("Fechar"))
      .open()
  }

  fun warning(caption: String = "Aviso", message: String) {
    ConfirmDialog.createWarning()
      .withCaption(caption)
      .withMessage(message)
      .withCloseButton(ButtonOption.caption("Fechar"))
      .open()
  }

  fun error(caption: String = "Erro", message: String) {
    ConfirmDialog.createError()
      .withCaption(caption)
      .withMessage(message)
      .withCloseButton(ButtonOption.caption("Fechar"))
      .open()
  }

  fun question(caption: String = "Questão", message: String, execYes: () -> Unit = {}, execNo: () -> Unit = {}) {
    ConfirmDialog.createQuestion()
      .withCaption(caption)
      .withMessage(message)
      .withYesButton(execYes, arrayOf(ButtonOption.caption("Sim")))
      .withNoButton(execNo, arrayOf(ButtonOption.caption("Não")))
      .open()
  }

  fun question(caption: String = "Questão", message: Component, execYes: (Component) -> Unit = {},
               execNo: (Component) -> Unit = {}) {
    ConfirmDialog.createQuestion()
      .withCaption(caption)
      .withMessage(message)
      .withYesButton({execYes(message)}, arrayOf(ButtonOption.caption("Sim")))
      .withNoButton({execNo(message)}, arrayOf(ButtonOption.caption("Não")))
      .open()
  }
}