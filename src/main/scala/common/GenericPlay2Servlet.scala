package play.core.server.servlet

import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import play.api.Mode

/**
 * Mother class for all servlet implementations for Play2.
 */
abstract class GenericPlay2Servlet extends HttpServlet with ServletContextListener {

  /**
   * Classic "service" servlet method.
   */
  override protected def service(servletRequest: HttpServletRequest, servletResponse: HttpServletResponse) = {

    val requestHandler = getRequestHandler(servletRequest, servletResponse)

    Play2WarServer.handleRequest(requestHandler)
  }

  protected def getRequestHandler(servletRequest: HttpServletRequest, servletResponse: HttpServletResponse): RequestHandler

  override def contextInitialized(e: ServletContextEvent): Unit = {
    e.getServletContext.log("PlayServletWrapper > contextInitialized")
    val currentMode: String = Option(e.getServletContext.getInitParameter("play.mode")).getOrElse(Mode.Dev.toString)
    e.getServletContext.log(s"app runs in ${currentMode} mode")
    // Init or get singleton
    // val Dev, Test, Prod = Value
    Play2WarServer(Some(e.getServletContext.getContextPath), Mode.withName(currentMode))
  }

  override def contextDestroyed(e: ServletContextEvent): Unit = {
    e.getServletContext.log("PlayServletWrapper > contextDestroyed")

    Play2WarServer.stop(e.getServletContext)
  }

  override def destroy(): Unit = {
    getServletContext.log("PlayServletWrapper > destroy")

    Play2WarServer.stop(getServletContext)
  }

}
