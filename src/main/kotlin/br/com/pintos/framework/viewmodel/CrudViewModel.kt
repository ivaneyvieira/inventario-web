package br.com.pintos.framework.viewmodel

import br.com.pintos.framework.model.SimpleBaseModel
import io.ebean.PagedList
import io.ebean.typequery.TQRootBean
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass

abstract class CrudViewModel<MODEL : SimpleBaseModel, Q : TQRootBean<MODEL, Q>, VO : EntityVo<MODEL>>
(view: IView, val crudClass: KClass<VO>) : ViewModel(view) {
  private var queryView: QueryView? = null
  private var pagedList: PagedList<MODEL>? = null
  var crudBean: VO? = null

  abstract fun update(bean: VO)
  abstract fun add(bean: VO)
  abstract fun delete(bean: VO)

  fun update() =
    exec {
      crudBean?.let { bean -> update(bean) }
    }

  fun add() =
    exec {
      crudBean?.let { bean -> add(bean) }
    }

  fun delete() =
    exec {
      crudBean?.let { bean -> delete(bean) }
    }

  //Query Lazy
  abstract val query: Q

  abstract fun MODEL.toVO(): VO

  open fun Q.filterString(text: String): Q {
    return this
  }

  open fun Q.filterInt(int: Int): Q {
    return this
  }

  open fun Q.filterDate(date: LocalDate): Q {
    return this
  }

  private fun Q.filterBlank(filter: String): Q {
    return if (filter.isBlank()) this
    else {
      val date = parserDate(filter)
      val int = filter.toIntOrNull()
      val q1 = or().filterString(filter)
      val q2 = date?.let { q1.filterDate(it) }
               ?: q1
      val q3 = int?.let { q2.filterInt(it) }
               ?: q2
      q3.endOr()
    }
  }

  open fun Q.orderQuery(): Q {
    return this
  }

  private fun parserDate(filter: String): LocalDate? {
    val frm = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return try {
      LocalDate.parse(filter, frm)
    } catch (e: Exception) {
      null
    }
  }

  private fun Q.makeSort(sorts: List<Sort>): Q {
    return if (sorts.isEmpty()) this.orderQuery()
    else {
      val orderByClause = sorts.joinToString { "${it.propertyName} ${if (it.descending) "DESC" else "ASC"}" }
      orderBy(orderByClause)
    }
  }

  open fun findQuery(): List<VO> =
    execList {
      val list = pagedList?.list.orEmpty()
      list.map { model ->
        val vo = model.toVO()
        vo.apply {
          entityVo = model
        }
      }
    }

  open fun countQuery(): Int =
    execInt {
      pagedList?.totalCount
      ?: 0
    }

  fun updateQueryView(queryView: QueryView) {
    if (this.queryView != queryView) {
      this.queryView = queryView
      pagedList = query.filterBlank(queryView.filter).makeSort(queryView.sorts).setFirstRow(queryView.offset)
        .setMaxRows(queryView.limit).findPagedList()
      pagedList?.loadCount()
    }
  }
}

data class Sort(val propertyName: String, val descending: Boolean = false)

abstract class EntityVo<MODEL : SimpleBaseModel> {
  open var entityVo: MODEL? = null
  var readOnly: Boolean = false

  fun toEntity(): MODEL? {
    return entityVo
           ?: findEntity()
  }

  abstract fun findEntity(): MODEL?
}

data class QueryView(val offset: Int, val limit: Int, val filter: String, val sorts: List<Sort>)