package com.jasongiedymin.util.db

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import com.mongodb.casbah.Imports._
import com.novus.salat.Context
import com.novus.salat.dao.SalatDAO
import com.mongodb.casbah.commons.TypeImports._
import com.novus.salat.dao.SalatMongoCursor
import com.mongodb.casbah.MongoCursorBase
import com.novus.salat.dao.ModelCompanion

/**
 * Class to use in place of SalatDAO so that the following instead
 * product 'Future'(s):
 *
 *  - findOne -> findOneFuture
 *  - find -> findFuture
 *  - findOneById -> findOneByIdFuture
 *
 * @param collection
 * @param mot
 * @param mid
 * @param ctx
 * @tparam ObjectType
 * @tparam ID
 */
abstract class MongoDAO[ObjectType <: AnyRef, ID <: Any]
(override val collection: MongoCollection)
(implicit mot: Manifest[ObjectType],mid: Manifest[ID], ctx: Context)
  extends SalatDAO[ObjectType, ID](collection)(mot, mid, ctx) {
  //ctx.registerClassLoader(current.classloader)
  dao =>

  def findOneFuture[A <% DBObject](t: A) = Future { collection.findOne(decorateQuery(t)).map(_grater.asObject(_)) }
  def findFuture[A <% DBObject, B <% DBObject](ref: A, keys: B) = SalatMongoCursor[ObjectType](_grater, collection.find(decorateQuery(ref), keys).asInstanceOf[MongoCursorBase].underlying)
  def findOneByIdFuture(id: ID) = Future { collection.findOneByID(id.asInstanceOf[AnyRef]).map(_grater.asObject(_)) }

}

/**
 * The Future variant of the Salat MondelCompanion trait.
 * @tparam ObjectType
 * @tparam ID
 */
trait MongoModelCompanion[ObjectType <: AnyRef, ID <: Any] extends ModelCompanion[ObjectType, ID] {
  override def dao: MongoDAO[ObjectType, ID]

  def findOneFuture[A <% DBObject](t: A) = dao.findOneFuture(t)
  def findFuture[A <% DBObject, B <% DBObject](ref: A, keys: B) = dao.findFuture(ref, keys)
  def findOneByIdFuture(id: ID) = dao.findOneByIdFuture(id)
}
