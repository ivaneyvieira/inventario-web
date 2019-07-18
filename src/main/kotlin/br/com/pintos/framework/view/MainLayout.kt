package br.com.pintos.framework.view

import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.flexLayout
import com.vaadin.flow.component.dependency.StyleSheet
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.router.RouterLayout
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

@StyleSheet("css/shared-styles.css")
@Theme(value = Lumo::class, variant = Lumo.DARK)
class MainLayout : KComposite(), RouterLayout {
    private val root = ui {
        flexLayout {
            setSizeFull(); className = "main-layout"

            menu {
               // addView(SampleCrudView::class, SampleCrudView.VIEW_NAME, VaadinIcon.EDIT)
               // addView(AboutView::class, AboutView.VIEW_NAME, VaadinIcon.INFO_CIRCLE)
            }
        }
    }
}

