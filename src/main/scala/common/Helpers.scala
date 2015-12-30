package play.core.server.servlet

import java.util.Arrays

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.JavaConverters.enumerationAsScalaIteratorConverter

import javax.servlet.http.{Cookie => ServletCookie}
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import play.api.mvc.{Cookie, Cookies, Headers}

trait HTTPHelpers {

  def getPlayHeaders(request: HttpServletRequest): Headers = {

    import java.util.Collections

    val headerNames = request.getHeaderNames.asScala

    val allHeaders: Seq[(String, String)] = headerNames.flatMap { k =>
      val key = k.toString
      // /!\ It very important to COPY headers from request enumeration
      val headers = Collections.list(request.getHeaders(key)).asScala
      headers.asInstanceOf[Seq[String]].map(h ⇒ (key, h))
    }.toSeq

    new Headers(allHeaders)
  }

  final def getPlayCookies(request: HttpServletRequest): Cookies = {

    val cookies: Map[String, play.api.mvc.Cookie] = request.getCookies match {
      case null => Map.empty
      case _ => Arrays.asList(request.getCookies: _*).asScala.map {
        c =>
          c.getName -> getPlayCookie(c)
      }.toMap
    }

    new Cookies {
      def get(name: String) = cookies.get(name)

      def foreach[U](f: (Cookie) => U) { cookies.values.foreach(f) }

      override def toString() = cookies.toString()
    }
  }

  def getPlayCookie(c: ServletCookie): play.api.mvc.Cookie

  final def getServletCookies(flatCookie: String): Seq[ServletCookie] =
    Cookies.decodeSetCookieHeader(flatCookie).map(getServletCookie)

  def getServletCookie(pCookie: play.api.mvc.Cookie): ServletCookie
}

trait RichHttpServletRequest {

  def getRichInputStream: Option[java.io.InputStream]
}

trait RichHttpServletResponse {

  def getRichOutputStream: Option[java.io.OutputStream]

  def getHttpServletResponse: Option[HttpServletResponse]
}
