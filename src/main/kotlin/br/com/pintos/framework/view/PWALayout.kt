package br.com.pintos.framework.view

import com.github.appreciated.app.layout.behaviour.Behaviour
import com.github.appreciated.app.layout.builder.AppLayoutBuilder
import com.github.appreciated.app.layout.builder.interfaces.NavigationElementContainer
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder
import com.github.appreciated.app.layout.component.menu.left.items.LeftHeaderItem
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem
import com.github.appreciated.app.layout.entity.Section
import com.github.appreciated.app.layout.router.AppLayoutRouterLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.theme.AbstractTheme

abstract class PWALayout: AppLayoutRouterLayout() {
  protected fun init(title: String = "", behaviour: Behaviour = Behaviour.LEFT_HYBRID,
                     initLayout: AppLayoutBuilder.() -> Unit) {
    val appLayoutBuilder = AppLayoutBuilder.get(behaviour)
      .apply {
        title(title)
        initLayout()
      }
    init(appLayoutBuilder.build())
  }

  protected fun AppLayoutBuilder.title(title: String) {
    withTitle(title)
  }

  protected fun AppLayoutBuilder.title(component: Component) {
    withTitle(component)
  }

  protected fun AppLayoutBuilder.icon(url: String) {
    withIcon(url)
  }

  protected fun AppLayoutBuilder.iconComponent(image: Component) {
    withIconComponent(image)
  }

  protected fun AppLayoutBuilder.appMenu(component: Component) {
    withAppMenu(component)
  }

  protected fun AppLayoutBuilder.appBar(component: Component) {
    withAppBar(component)
  }

  protected fun AppLayoutBuilder.appMenu(subMenu: LeftAppMenuBuilder.() -> Unit) {
    val leftAppMenu = LeftAppMenuBuilder.get()
    leftAppMenu.subMenu()
    appMenu(leftAppMenu.build())
  }

  protected fun LeftAppMenuBuilder.item(element: Component) {
    add(LeftNavigationItem(element))
  }

  protected fun LeftAppMenuBuilder.item(caption: String, icon: VaadinIcon, className: Class<out Component>) {
    add(LeftNavigationItem(caption, icon, className))
  }

  protected fun LeftAppMenuBuilder.item(caption: String, icon: Icon, className: Class<out Component>) {
    add(LeftNavigationItem(caption, icon, className))
  }

  protected fun LeftAppMenuBuilder.sectionHeader(element: Component) {
    addToSection(element, Section.HEADER)
  }

  protected fun LeftAppMenuBuilder.iconHeader(src: String) {
    addToSection(LeftHeaderItem(null, null, src), Section.HEADER)
  }

  protected fun LeftAppMenuBuilder.sectionFooter(element: Component) {
    addToSection(element, Section.FOOTER)
  }

  protected fun LeftAppMenuBuilder.section(element: Component) {
    addToSection(element, Section.DEFAULT)
  }

  protected fun LeftAppMenuBuilder.menuHeader(title: String, subtitle: String, src: String) {
    sectionHeader(LeftHeaderItem(title, subtitle, src))
  }
}

