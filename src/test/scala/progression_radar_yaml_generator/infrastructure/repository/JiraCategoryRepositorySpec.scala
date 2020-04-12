package progression_radar_yaml_generator.infrastructure.repository

import java.net.URI

import cats.effect.IO
import org.springframework.http.{HttpEntity, HttpMethod, HttpStatus, ResponseEntity}
import org.springframework.web.client.RestTemplate
import progression_radar_yaml_generator.domain.Category
import progression_radar_yaml_generator.infrastructure.configuration.properties.JiraProperties
import progression_radar_yaml_generator.BaseUnitSpec
import progression_radar_yaml_generator.fixture.CategoriesJiraResponseDTOGenerator
import progression_radar_yaml_generator.infrastructure.dto.{CategoriesJiraIssueDTO, CategoriesJiraResponseDTO}

class JiraCategoryRepositorySpec extends BaseUnitSpec {

  var restTemplate: RestTemplate = mock[RestTemplate]
  var repository: JiraCategoryRepository[IO] = new JiraCategoryRepository[IO](restTemplate, new JiraProperties {
    url = "https://test.atlassian.net/rest/api/2"
    username = "asiermarques@test.com"
    token = "test"
    project = "TEST"
  })

  before {
    reset(restTemplate)
  }

  describe("Retrieve categories from Jira") {

    def setUpMockPaginationCalls(
      pageWithResultsNumber: Int,
      resultNumber: Int = JiraCategoryRepository.ITEMS_PER_PAGE
    ): Unit = {
      var invocationCounter: Int = 0
      when(
        restTemplate
          .exchange(any[URI], any[HttpMethod], any[HttpEntity[String]], any[Class[CategoriesJiraResponseDTO]])
      ).thenAnswer(
        new ResponseEntity[CategoriesJiraResponseDTO](
          if (invocationCounter < pageWithResultsNumber) {
            invocationCounter += 1
            CategoriesJiraResponseDTOGenerator.generate(resultNumber)
          } else CategoriesJiraResponseDTO(issues = Array.empty[CategoriesJiraIssueDTO]),
          HttpStatus.OK
        )
      )
      ()
    }

    describe("When there is more than one page of results") {

      it("should retrieve all the categories") {

        setUpMockPaginationCalls(2)

        val result: Seq[Category] = repository.findAllCategories().unsafeRunSync()

        verify(restTemplate, atMost(3))
          .exchange(any[URI], any[HttpMethod], any[HttpEntity[String]], any[Class[CategoriesJiraResponseDTO]])
        result.length shouldBe 2
        result.map(_.kpis.length).sum shouldBe JiraCategoryRepository.ITEMS_PER_PAGE * 2
      }

      it("should not request more than the max specified pages") {

        setUpMockPaginationCalls(JiraCategoryRepository.MAX_PAGES_TO_REQUEST * 2)

        val result: Seq[Category] = repository.findAllCategories().unsafeRunSync()

        verify(restTemplate, atMost(JiraCategoryRepository.MAX_PAGES_TO_REQUEST))
          .exchange(any[URI], any[HttpMethod], any[HttpEntity[String]], any[Class[CategoriesJiraResponseDTO]])
        result
          .map(_.kpis.length)
          .sum shouldBe JiraCategoryRepository.ITEMS_PER_PAGE * JiraCategoryRepository.MAX_PAGES_TO_REQUEST
      }

      it(
        "should not request more than the max specified pages and return all categories even if the last page doesn't have results"
      ) {

        setUpMockPaginationCalls(JiraCategoryRepository.MAX_PAGES_TO_REQUEST - 1)

        val result: Seq[Category] = repository.findAllCategories().unsafeRunSync()

        verify(restTemplate, atMost(JiraCategoryRepository.MAX_PAGES_TO_REQUEST))
          .exchange(any[URI], any[HttpMethod], any[HttpEntity[String]], any[Class[CategoriesJiraResponseDTO]])
        result
          .map(_.kpis.length)
          .sum shouldBe (JiraCategoryRepository.ITEMS_PER_PAGE * JiraCategoryRepository.MAX_PAGES_TO_REQUEST) - JiraCategoryRepository.ITEMS_PER_PAGE
      }
    }

    describe("When there is only a page of results") {

      it("should get all the categories in the page") {
        setUpMockPaginationCalls(1)

        val result: Seq[Category] = repository.findAllCategories().unsafeRunSync()

        verify(restTemplate, atMost(2))
          .exchange(any[URI], any[HttpMethod], any[HttpEntity[String]], any[Class[CategoriesJiraResponseDTO]])
        result.length shouldBe 2
        result.map(_.kpis.length).sum shouldBe JiraCategoryRepository.ITEMS_PER_PAGE
      }

      it("should get all the categories without an extra request if they are less than the expected per page number") {

        val resultNumber: Int = JiraCategoryRepository.ITEMS_PER_PAGE - 10
        setUpMockPaginationCalls(1, resultNumber)

        verify(restTemplate, atMost(1))
          .exchange(any[URI], any[HttpMethod], any[HttpEntity[String]], any[Class[CategoriesJiraResponseDTO]])
        val result: Seq[Category] = repository.findAllCategories().unsafeRunSync()
        result.length shouldBe 2
        result.map(_.kpis.length).sum shouldBe resultNumber
      }
    }

  }

}
