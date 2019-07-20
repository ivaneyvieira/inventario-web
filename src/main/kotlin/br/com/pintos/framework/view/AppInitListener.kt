package br.com.pintos.framework.view

import br.com.pintos.framework.view.vaadin10.LoginScreen
import br.com.pintos.framework.view.vaadin10.Session
import br.com.pintos.framework.view.vaadin10.VokSecurity
import br.com.pintos.framework.view.vaadin10.loginManager
import com.vaadin.flow.server.ServiceInitEvent
import com.vaadin.flow.server.VaadinServiceInitListener

class AppInitListener : VaadinServiceInitListener {
    override fun serviceInit(initEvent: ServiceInitEvent) {
        initEvent.source.addUIInitListener { uiInitEvent ->
            uiInitEvent.ui.addBeforeEnterListener { enterEvent ->
                if (!Session.loginManager.isLoggedIn && enterEvent.navigationTarget != LoginScreen::class.java) {
                    enterEvent.rerouteTo(LoginScreen::class.java)
                } else {
                    VokSecurity.checkPermissionsOfView(enterEvent.navigationTarget)
                }
            }
        }
    }
}

// br.com.pintos.framework.view.AppInitListener