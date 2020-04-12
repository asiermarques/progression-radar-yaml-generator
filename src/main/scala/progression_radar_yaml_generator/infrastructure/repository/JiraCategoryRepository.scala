package progression_radar_yaml_generator.infrastructure.repository

import java.net.URI

import cats.effect.Sync
import cats.implicits._
import org.springframework.http.{HttpEntity, HttpHeaders, HttpMethod}
import org.springframework.web.client.RestTemplate
import progression_radar_yaml_generator.domain.{Category, CategoryRepository}
import progression_radar_yaml_generator.infrastructure.configuration.properties.JiraProperties
import progression_radar_yaml_generator.infrastructure.dto.{CategoriesJiraIssueDTO, CategoriesJiraResponseDTO}

class JiraCategoryRepository[F[_]: Sync](
  restTemplate: RestTemplate,
  config: JiraProperties
) extends CategoryRepository[F] {

  private val headers: HttpHeaders = new HttpHeaders()
  private val url: String =
    s"${config.url}/search?jql=project=${config.project}" +
      s"&startAt=%s&maxResults=${JiraCategoryRepository.ITEMS_PER_PAGE}" +
      "&fields=summary,description,status,labels"

  override def findAllCategories(): F[Seq[Category]] =
    for {
      request       <- createRequest
      categoriesDto <- Sync[F].delay(doRequest(request))
      categories    <- mapToDomainEntity(categoriesDto)
    } yield categories

  private def createRequest: F[HttpEntity[String]] = Sync[F].delay {
    headers.setBasicAuth(config.username, config.token)
    new HttpEntity[String](headers)
  }

  private def doRequest(
    request: HttpEntity[String],
    result: CategoriesJiraResponseDTO = CategoriesJiraResponseDTO(issues = Array.empty[CategoriesJiraIssueDTO]),
    position: Int = 0,
    requestCount: Int = 1
  ): CategoriesJiraResponseDTO =
    restTemplate
      .exchange(new URI(url.format(position)), HttpMethod.GET, request, classOf[CategoriesJiraResponseDTO])
      .getBody match {
      case _result: CategoriesJiraResponseDTO
          if _result.issues.length > 0 && requestCount < JiraCategoryRepository.MAX_PAGES_TO_REQUEST =>
        doRequest(
          request,
          result.copy(issues = result.issues :++ _result.issues),
          position + JiraCategoryRepository.ITEMS_PER_PAGE,
          requestCount + 1
        )
      case _result: CategoriesJiraResponseDTO if requestCount == JiraCategoryRepository.MAX_PAGES_TO_REQUEST =>
        result.copy(issues = result.issues :++ _result.issues)
      case _result: CategoriesJiraResponseDTO
          if _result.issues.length < JiraCategoryRepository.ITEMS_PER_PAGE ||
            requestCount >= JiraCategoryRepository.MAX_PAGES_TO_REQUEST =>
        result
    }

  private def mapToDomainEntity(responseDTO: CategoriesJiraResponseDTO): F[Seq[Category]] =
    Sync[F].delay(CategoriesJiraResponseDTO.toCategories(responseDTO))
}
object JiraCategoryRepository {
  final val ITEMS_PER_PAGE: Int       = 50
  final val MAX_PAGES_TO_REQUEST: Int = 100
}
