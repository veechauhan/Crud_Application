package controllers

import org.specs2.mutable.Specification
import play.api.test.WithBrowser

class IntegrationSpec {
  class IntegrationSpec extends Specification {

    "Application" should {
      "work from within a browser" in new WithBrowser {

        browser.goTo("http://localhost:" + port)

        browser.pageSource must contain("Welcome to Play!")
      }
    }
  }
}


