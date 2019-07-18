package br.com.pintos.framework.view.help

import br.com.pintos.framework.viewmodel.CrudViewModel
import br.com.pintos.framework.viewmodel.EntityVo

abstract class CrudLayoutView<C: EntityVo<*>, V: CrudViewModel<*, *, C>>: LayoutView<V>() {}