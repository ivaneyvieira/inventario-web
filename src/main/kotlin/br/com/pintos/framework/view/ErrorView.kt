package br.com.pintos.framework.view

import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.h1
import com.github.mvysny.karibudsl.v10.span
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.*
import javax.servlet.http.HttpServletResponse

class ErrorView: KComposite(), HasErrorParameter<NotFoundException> {
  private lateinit var explanation: Span
  private val root = ui {
    verticalLayout {
      h1("The view could not be found.")
      explanation = span()
    }
  }

  override fun setErrorParameter(event: BeforeEnterEvent, parameter: ErrorParameter<NotFoundException>): Int {
    explanation.text = ("Could not navigate to '${event.location.path}'.")
    return HttpServletResponse.SC_NOT_FOUND
  }
}
