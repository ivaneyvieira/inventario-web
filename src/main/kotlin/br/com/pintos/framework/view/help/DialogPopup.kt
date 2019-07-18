package br.com.pintos.framework.view.help

import com.github.mvysny.karibudsl.v10.h2
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.binder.BeanValidationBinder
import kotlin.reflect.KClass

open class DialogPopup<BEAN : Any>(classBean: KClass<BEAN>) : Dialog() {
  val binder = BeanValidationBinder(classBean.java)
  val form = VerticalLayout().apply {
    setSizeFull()
  }
  private val btnOk: Button = Button("Confirma").apply {
    addThemeVariants(ButtonVariant.LUMO_PRIMARY)
  }
  private val btnCancel = Button("Cancela")
  private val toolBar = buildToolBar()

  fun show() {
    isOpened = true
  }

  fun initForm(configForm: (VerticalLayout) -> Unit) {
    configForm(form)
  }

  private fun buildToolBar(): Component {
    val tool = HorizontalLayout()
    tool.width = "100%"
    tool.isSpacing = true
    btnOk.addClickListener { this.btnOkClick() }
    btnCancel.addClickListener { this.btnCancelClick() }
    return tool
  }

  fun addClickListenerOk(listener: () -> Unit) =
    btnOk.addClickListener {
      listener()
    }

  fun addClickListenerCancel(listener: () -> Unit) =
    btnCancel.addClickListener {
      listener()
    }

  private fun btnCancelClick() {
    close()
  }

  private fun btnOkClick() {
    val status = binder.validate()
    if (!status.hasErrors()) close()
  }
}

fun VerticalLayout.grupo(caption: String = "", block: VerticalLayout.() -> Unit) {
  if (caption.isNotBlank()) h2(caption)
  verticalLayout {
    this.width = "100%"
    this.block()
  }
}

fun VerticalLayout.row(block: HorizontalLayout.() -> Unit) {
  horizontalLayout {
    this.width = "100%"
    this.block()
  }
}