package br.com.pintos.framework.viewmodel

import br.com.pintos.framework.model.Transaction

abstract class ViewModel(private val view: IView) {
  private var inExcursion = false

  private fun updateView(exception: EViewModel? = null) {
    exception?.let { e ->
      e.message?.let {
        view.showError(e.message)
      }
    }
    view.updateView()
  }

  private fun updateModel() {
    view.updateModel()
  }

  @Throws(EViewModel::class)
  fun <T> execValue(block: () -> T): T? {
    var ret: T? = null
    transaction {
      try {
        if (inExcursion) ret = block()
        else {
          inExcursion = true
          updateModel()

          ret = block()

          updateView()
          inExcursion = false
        }
      } catch(e: EViewModel) {
        inExcursion = false
        updateView(e)
      }
    }
    return ret
  }

  @Throws(EViewModel::class)
  fun execString(block: () -> String): String {
    return execValue(block)
           ?: ""
  }

  @Throws(EViewModel::class)
  fun execInt(block: () -> Int): Int {
    return execValue(block)
           ?: 0
  }

  @Throws(EViewModel::class)
  fun exec(block: () -> Unit) {
    transaction {
      try {
        if (inExcursion) block()
        else {
          inExcursion = true
          updateModel()

          block()

          updateView()
          inExcursion = false
        }
      } catch(e: EViewModel) {
        updateView(e)
        inExcursion = false
        throw e
      }
    }
  }

  @Throws(EViewModel::class)
  fun <T> execList(block: () -> List<T>): List<T> {
    return execValue(block).orEmpty()
  }

  private fun <T> transaction(block: () -> T) {
    return try {
      block()
      Transaction.commit()
    } catch(ev: EViewModel) {
      Transaction.rollback()
    } catch (e: Throwable) {
      Transaction.rollback()
      throw e
    }
  }
}

class EViewModel(msg: String) : Exception(msg)

interface IView {
  fun updateView()

  fun updateModel()

  fun showWarning(msg: String)

  fun showError(msg: String)

  fun showInfo(msg: String)
}

