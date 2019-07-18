package br.com.pintos.framework.view.help

import br.com.pintos.framework.viewmodel.IView
import br.com.pintos.framework.viewmodel.ViewModel
import br.com.pintos.framework.util.SystemUtils
import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.h2
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.BeforeLeaveEvent
import com.vaadin.flow.router.BeforeLeaveObserver
import com.vaadin.flow.server.StreamResource
import org.apache.commons.io.IOUtils
import org.vaadin.olli.BrowserOpener
import java.io.InputStream
import java.nio.charset.Charset
import java.time.LocalDateTime

abstract class LayoutView<V : ViewModel> : VerticalLayout(), View, IView {
  abstract val viewModel: V

  fun form(titleForm: String, block: (@VaadinDsl VerticalLayout).() -> Unit = {}) {
    setSizeFull()
    h2(titleForm)
    this.block()
  }

  override fun beforeEnter(event: BeforeEnterEvent) {
    updateView()
  }

  override fun beforeLeave(event: BeforeLeaveEvent) {

  }


  fun <T> Grid<T>.actionSelected(msgErro: String = "Selecione um item", action: (T) -> Unit) {
    this.selectedItems.firstOrNull()?.let { item -> action(item) }
    ?: showWarning(msgErro)
  }

  override fun showWarning(msg: String) {
    if (msg.isNotBlank()) MessageDialog.warning(message = msg)
  }

  override fun showError(msg: String) {
    if (msg.isNotBlank()) MessageDialog.error(message = msg)
  }

  override fun showInfo(msg: String) {
    if (msg.isNotBlank()) MessageDialog.info(message = msg)
  }

  open fun print(text: () -> String): BrowserOpener {
    val name = "${SystemUtils.md5(LocalDateTime.now().toString())}.txt"
    val factory: () -> InputStream = { IOUtils.toInputStream(text(), Charset.defaultCharset()) }
    val resource = StreamResource(name, factory)
    resource.setContentType("text/plain")
    val resourceUri = UI.getCurrent().session.resourceRegistry.registerResource(resource)
    val url = resourceUri.resourceUri.toURL()

    return BrowserOpener().apply {
      setUrl(url.toString())
    }
  }
}

interface View : BeforeLeaveObserver, BeforeEnterObserver


